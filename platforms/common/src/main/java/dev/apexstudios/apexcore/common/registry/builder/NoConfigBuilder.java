package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NoConfigBuilder<O extends AbstractRegister<O>, P, T, R extends T, H extends DeferredHolder<T, R>> extends AbstractBuilder<O, P, T, R, H, NoConfigBuilder<O, P, T, R, H>>
{
    private final Supplier<R> valueFactory;
    // defaults -> <registry_type>.<owner_id>.<registration_name>
    private Function<R, String> translationKey = value -> Util.makeDescriptionId(registryType.location().getPath(), registryName());

    @ApiStatus.Internal
    public NoConfigBuilder(O owner, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, BiFunction<String, ResourceKey<T>, H> holderFactory, BuilderHelper helper, Supplier<R> valueFactory)
    {
        super(owner, parent, registryType, registrationName, holderFactory, helper);

        this.valueFactory = valueFactory;

        // no translation by default
        noLang();
    }

    public NoConfigBuilder<O, P, T, R, H> defaultLangKey()
    {
        return langKey(registryType().location().getPath());
    }

    public NoConfigBuilder<O, P, T, R, H> langKey(String translationType)
    {
        return langKey(value -> registryName().toLanguageKey(translationType));
    }

    public NoConfigBuilder<O, P, T, R, H> langKey(Function<R, String> translationKey)
    {
        this.translationKey = translationKey;
        return this;
    }

    @Override
    protected R createValue()
    {
        return valueFactory.get();
    }

    @Override
    protected String translationKeyLookup(R value)
    {
        return translationKey.apply(value);
    }
}
