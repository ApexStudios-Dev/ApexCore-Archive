package dev.apexstudios.apexcore.common.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

final class DataProviderWrapper<T extends ResourceGenerator> implements DataProvider
{
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final ProviderType<T> providerType;
    private final String ownerId;
    private final PackOutput output;

    DataProviderWrapper(String ownerId, PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ProviderType<T> providerType)
    {
        this.ownerId = ownerId;
        this.output = output;
        this.registries = registries;
        this.providerType = providerType;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        return registries.thenAccept(lookup -> ((ProviderTypeImpl<T>) providerType).provideFor(ownerId, output, cache, lookup));
    }

    @Override
    public String getName()
    {
        return "%s/%s".formatted(ownerId, providerType.name());
    }
}
