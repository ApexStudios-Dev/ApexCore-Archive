package dev.apexstudios.apexcore.common.registry.builder;

import com.google.errorprone.annotations.DoNotCall;
import dev.apexstudios.apexcore.common.generator.ProviderTypes;
import dev.apexstudios.apexcore.common.generator.common.LanguageGenerator;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.covers1624.quack.util.LazyValue;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractBuilder<O extends AbstractRegister<O>, P, T, R extends T, H extends DeferredHolder<T, R>, B extends AbstractBuilder<O, P, T, R, H, B>>
{
    protected final O owner;
    protected final P parent;
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceKey<T> registryKey;
    private final Supplier<H> holderFactory;
    private final BuilderHelper helper;
    // null to not have any translation
    private OptionalLike<String> translationValue = () -> LanguageGenerator.toEnglishName(registrationName());

    protected AbstractBuilder(O owner, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, BiFunction<String, ResourceKey<T>, H> holderFactory, BuilderHelper helper)
    {
        this.owner = owner;
        this.parent = parent;
        this.registryType = registryType;
        this.helper = helper;

        registryKey = ResourceKey.create(registryType, new ResourceLocation(owner.ownerId(), registrationName));
        this.holderFactory = new LazyValue<>(() -> holderFactory.apply(owner.ownerId(), registryKey));

        onRegister(value -> {
            ProviderTypes.LANGUAGE.addListener(ownerId(), lang -> translationValue.ifPresent(translation -> lang.with(translationKeyLookup(value), translation)));
            onRegister(value);
        });
    }

    public final O owner()
    {
        return owner;
    }

    public final P parent()
    {
        return parent;
    }

    public final ResourceKey<? extends Registry<T>> registryType()
    {
        return registryType;
    }

    public final ResourceKey<T> registryKey()
    {
        return registryKey;
    }

    public final ResourceLocation registryName()
    {
        return registryKey.location();
    }

    public final String registrationName()
    {
        return registryName().getPath();
    }

    public final String ownerId()
    {
        return owner.ownerId();
    }

    public final H holder()
    {
        return holderFactory.get();
    }

    public final H register()
    {
        return helper.register(registryType, holderFactory, this::createValue);
    }

    public final P build()
    {
        register();
        return parent;
    }

    public final B onRegister(Consumer<R> listener)
    {
        owner.addRegisterListener(registryType, registryName().getPath(), listener);
        return self();
    }

    public final <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> listener)
    {
        return onRegister(value -> {
            if(owner.isRegistered(dependencyType))
                listener.accept(value);
            else
                owner.addRegisterListener(dependencyType, () -> listener.accept(value));
        });
    }

    public final B noLang()
    {
        this.translationValue = OptionalLike.empty();
        return self();
    }

    public final B defaultLang()
    {
        return lang(LanguageGenerator.toEnglishName(registrationName()));
    }

    public final B lang(String translationValue)
    {
        this.translationValue = OptionalLike.of(translationValue);
        return self();
    }

    @ApiStatus.OverrideOnly
    @DoNotCall
    protected void onRegister(R value)
    {
    }

    protected final B self()
    {
        return (B) this;
    }

    @DoNotCall protected abstract R createValue();

    @DoNotCall protected abstract String translationKeyLookup(R value);

    protected final OptionalLike<String> translationValue()
    {
        return translationValue;
    }
}
