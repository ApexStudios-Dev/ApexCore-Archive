package xyz.apex.minecraft.apexcore.shared.registry;

import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import xyz.apex.minecraft.apexcore.shared.data.DataContext;
import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderType;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.data.providers.Language;
import xyz.apex.minecraft.apexcore.shared.data.providers.Tag;
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

    @SuppressWarnings("unchecked")
    private B self()
    {
        return (B) this;
    }

    default R getEntry()
    {
        return get().get();
    }

    default Supplier<R> asSupplier()
    {
        return this::getEntry;
    }

    default <D extends DataProvider> B clearData(ProviderType<? extends D> providerType)
    {
        Generators.setDataGenerator(getModId(), getName(), getRegistryType(), providerType, provider -> { });
        return self();
    }

    default <D extends DataProvider> B setData(ProviderType<? extends D> providerType, BiConsumer<DataContext<R, E>, D> consumer)
    {
        Generators.setDataGenerator(getModId(), getName(), getRegistryType(), providerType, provider -> consumer.accept(new DataContext<>(get()), provider));
        return self();
    }

    default <D extends DataProvider> B addMiscData(ProviderType<? extends D> providerType, Consumer<? extends D> consumer)
    {
        Generators.addDataGenerator(getModId(), providerType, consumer);
        return self();
    }

    B tag(ProviderType<? extends Tag<T>> providerType, TagKey<T>... tags);

    B removeTag(ProviderType<? extends Tag<T>> providerType, TagKey<T>... tags);

    default B lang(Function<R, String> langKeyProvider)
    {
        return lang(langKeyProvider, (ctx, provider) -> Language.toEnglishName(getName()));
    }

    default B lang(Function<R, String> langKeyProvider, String name)
    {
        return lang(langKeyProvider, (p, s) -> name);
    }

    default B lang(Function<R, String> langKeyProvider, BiFunction<R, Language, String> localizedNameProvider)
    {
        return setData(ProviderTypes.LANGUAGE, (ctx, provider) -> provider.add(langKeyProvider.apply(ctx.get()), localizedNameProvider.apply(ctx.get(), provider)));
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
