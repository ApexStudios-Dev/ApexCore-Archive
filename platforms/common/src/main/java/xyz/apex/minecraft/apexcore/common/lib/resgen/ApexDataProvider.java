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
    private final ProviderType.ProviderContext context;

    private ApexDataProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String ownerId)
    {
        context = new ProviderType.ProviderContext(packOutput, registries, ownerId);
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
        var provider = providerType.create(context);
        providerType.provide(provider);
        return provider.run(cache);
    }

    @Override
    public String getName()
    {
        return "DataProvider for %s".formatted(ProviderType.providerTypes().stream().map(ProviderType::providerName).map(ResourceLocation::toString).collect(Collectors.joining(", ", "[", "]")));
    }

    // must be called from DataGeneration entry points per mod
    public static void register(String ownerId, Consumer<BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, DataProvider>> providerRegistrar)
    {
        providerRegistrar.accept((packOutput, registries) -> new ApexDataProvider(packOutput, registries, ownerId));
    }
}
