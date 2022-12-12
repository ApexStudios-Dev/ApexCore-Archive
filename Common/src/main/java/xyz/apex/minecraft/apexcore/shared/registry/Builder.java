package xyz.apex.minecraft.apexcore.shared.registry;

import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import xyz.apex.minecraft.apexcore.shared.data.DataContext;
import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderType;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.data.providers.LanguageProvider;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.LazyLike;

import java.util.function.*;

// TODO: onRegister & onRegisterAfter
public interface Builder<T, R extends T, P, B extends Builder<T, R, P, B, E>, E extends RegistryEntry<R>> extends Supplier<E>, LazyLike<E>
{
    E register();
    P getParent();
    String getModId();
    String getName();
    ResourceLocation getRegistryName();
    ResourceKey<? extends Registry<T>> getRegistryType();
    ResourceKey<T> getRegistryKey();

    default R getEntry()
    {
        return get().get();
    }

    default Supplier<R> asSupplier()
    {
        return this::getEntry;
    }

    @SuppressWarnings("unchecked")
    default <D extends DataProvider> B setData(ProviderType<? extends D> providerType, BiConsumer<DataContext<R, E>, D> consumer)
    {
        Generators.setDataGenerator(getModId(), getName(), getRegistryType(), providerType, provider -> consumer.accept(new DataContext<>(get()), provider));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    default <D extends DataProvider> B addMiscData(ProviderType<? extends D> providerType, Consumer<? extends D> consumer)
    {
        Generators.addDataGenerator(getModId(), providerType, consumer);
        return (B) this;
    }

    B tag(ProviderType<? extends TagsProvider<T>> providerType, TagKey<T>... tags);

    B removeTag(ProviderType<? extends TagsProvider<T>> providerType, TagKey<T>... tags);

    default B lang(Function<T, String> langKeyProvider)
    {
        return lang(langKeyProvider, (p, t) -> p.getAutomaticName(t, getRegistryType()));
    }

    default B lang(Function<T, String> langKeyProvider, String name)
    {
        return lang(langKeyProvider, (p, s) -> name);
    }

    default B lang(Function<T, String> langKeyProvider, BiFunction<LanguageProvider, Supplier<? extends T>, String> localizedNameProvider)
    {
        return setData(ProviderTypes.LANGUAGE, (ctx, provider) -> provider.add(langKeyProvider.apply(ctx.get()), localizedNameProvider.apply(provider, ctx)));
    }

    default P build()
    {
        register();
        return getParent();
    }

    @FunctionalInterface
    interface RegistryEntryFactoryEX<T, R extends T, E extends RegistryEntry<R>>
    {
        E create(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName);
    }

    @FunctionalInterface
    interface RegistryEntryFactory<R, E extends RegistryEntry<R>>
    {
        E create(ResourceLocation registryName);
    }
}
