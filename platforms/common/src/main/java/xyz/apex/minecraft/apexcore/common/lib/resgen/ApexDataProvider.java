package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class ApexDataProvider implements DataProvider
{
    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> registries;

    private ApexDataProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
    {
        this.packOutput = packOutput;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        return CompletableFuture.allOf(ProviderType
                .providerTypes()
                .stream()
                .map(providerType -> provide(providerType, cache))
                .toArray(CompletableFuture[]::new)
        );
    }

    private <P extends DataProvider> CompletableFuture<?> provide(ProviderType<P> providerType, CachedOutput cache)
    {
        var provider = providerType.create(packOutput, registries);
        providerType.provide(provider);
        return provider.run(cache);
    }

    @Override
    public String getName()
    {
        return "DataProvider for %s".formatted(ProviderType.providerTypes().stream().map(ProviderType::providerName).map(ResourceLocation::toString).collect(Collectors.joining(", ", "[", "]")));
    }

    // must be called from DataGeneration entry points per mod
    public static void register(Consumer<BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, DataProvider>> providerRegistrar)
    {
        providerRegistrar.accept(ApexDataProvider::new);
    }
}
