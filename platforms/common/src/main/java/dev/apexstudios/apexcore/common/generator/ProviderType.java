package dev.apexstudios.apexcore.common.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public sealed interface ProviderType<T extends ResourceGenerator> permits ProviderTypeImpl
{
    ResourceLocation name();

    void addListener(String ownerId, Consumer<T> listener);

    void addListener(String ownerId, BiConsumer<T, HolderLookup.Provider> listener);

    boolean hasListeners(String ownerId);

    static <T extends ResourceGenerator> ProviderType<T> register(String ownerId, String providerName, Factory<T> providerFactory)
    {
        return ProviderTypeImpl.register(new ResourceLocation(ownerId, providerName), providerFactory);
    }

    static Collection<ProviderType<?>> providerTypes()
    {
        return ProviderTypeImpl.PROVIDER_TYPES_VIEW;
    }

    @FunctionalInterface
    interface Factory<T extends ResourceGenerator>
    {
        T create(String ownerId, PackOutput output);
    }
}
