package xyz.apex.minecraft.apexcore.common.lib.resgen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class ApexDataProvider implements DataProvider, ProviderLookup
{
    private final ProviderType.ProviderContext context;
    private final Set<ProviderType<?>> generated = Sets.newHashSet();
    private final Map<ProviderType<?>, DataProvider> providerMap = Maps.newHashMap();

    private ApexDataProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String ownerId)
    {
        context = new ProviderType.ProviderContext(packOutput, registries, ownerId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P extends DataProvider> P lookup(ProviderType<P> providerType)
    {
        return (P) providerMap.computeIfAbsent(providerType, p -> p.create(context));
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
        var ownerId = context.ownerId();

        // has this provider already generated or have no listeners
        if(!generated.add(providerType) || !providerType.hasListeners(ownerId))
            return CompletableFuture.completedFuture(null);

        // linked list to ensure all parents run first
        var futures = Lists.<CompletableFuture<?>>newLinkedList();
        providerType.parents().stream().map(parent -> provide(parent, cache)).forEach(futures::add);

        var provider = lookup(providerType);
        providerType.provide(ownerId, provider, this);
        futures.addLast(provider.run(cache)); // run this provider last
        ApexCore.LOGGER.debug("Executing Provider: '{}'", providerType.providerName());
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName()
    {
        return "DataProvider for %s".formatted(ProviderType
                .providerTypes()
                .stream()
                .filter(providerType -> providerType.hasListeners(context.ownerId()))
                .map(ProviderType::providerName)
                .map(ResourceLocation::toString)
                .collect(Collectors.joining(", ", "[", "]"))
        );
    }

    // must be called from DataGeneration entry points per mod
    public static void register(String ownerId, Consumer<BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, DataProvider>> providerRegistrar)
    {
        ProviderTypes.bootstrap();
        providerRegistrar.accept((packOutput, registries) -> new ApexDataProvider(packOutput, registries, ownerId));
    }
}
