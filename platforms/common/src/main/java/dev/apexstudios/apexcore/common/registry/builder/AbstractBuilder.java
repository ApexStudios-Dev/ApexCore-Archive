package dev.apexstudios.apexcore.common.registry.builder;

import com.google.errorprone.annotations.DoNotCall;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
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

    protected AbstractBuilder(O owner, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, BiFunction<String, ResourceKey<T>, H> holderFactory, BuilderHelper helper)
    {
        this.owner = owner;
        this.parent = parent;
        this.registryType = registryType;
        this.helper = helper;

        registryKey = ResourceKey.create(registryType, new ResourceLocation(owner.ownerId(), registrationName));
        this.holderFactory = new LazyValue<>(() -> holderFactory.apply(owner.ownerId(), registryKey));

        onRegister(this::onRegister);
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
}
