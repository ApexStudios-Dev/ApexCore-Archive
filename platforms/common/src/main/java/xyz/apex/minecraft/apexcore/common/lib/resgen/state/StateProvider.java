package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public final class StateProvider<T, S extends StateHolder<T, S>> implements DataProvider
{
    public static final ProviderType<StateProvider<Block, BlockState>> BLOCK = register(new ResourceLocation(ApexCore.ID, "states/block"), Block::getStateDefinition, Registries.BLOCK, block -> block.builtInRegistryHolder().key());

    private final ProviderType.ProviderContext context;
    private final Function<T, StateDefinition<T, S>> stateDefinitionFunction;
    private final ResourceKey<? extends Registry<T>> registryType;
    @Nullable private final Function<T, ResourceKey<T>> intrinsicKeyExtractor;
    private final Supplier<Registry<T>> registry;
    private final Map<ResourceLocation, StateBuilder<T, S>> builders = Maps.newHashMap();

    private StateProvider(ProviderType.ProviderContext context, Function<T, StateDefinition<T, S>> stateDefinitionFunction, ResourceKey<? extends Registry<T>> registryType, @Nullable Function<T, ResourceKey<T>> intrinsicKeyExtractor)
    {
        this.context = context;
        this.stateDefinitionFunction = stateDefinitionFunction;
        this.registryType = registryType;
        this.intrinsicKeyExtractor = intrinsicKeyExtractor;

        registry = Suppliers.memoize(() -> RegistryHooks.findVanillaRegistry(registryType).orElseThrow());
    }

    public StateBuilder<T, S> builder(String registryName)
    {
        return builder(new ResourceLocation(registryName));
    }

    public StateBuilder<T, S> builder(ResourceLocation registryName)
    {
        return builders.computeIfAbsent(registryName, name -> new StateBuilder<>(Objects.requireNonNull(registry.get().get(name)), stateDefinitionFunction));
    }

    public StateBuilder<T, S> builder(T element)
    {
        return builders.computeIfAbsent(getKey(element).location(), $ -> new StateBuilder<>(element, stateDefinitionFunction));
    }

    public ResourceKey<T> getKey(T element)
    {
        if(intrinsicKeyExtractor != null)
            return intrinsicKeyExtractor.apply(element);

        return registry.get().getResourceKey(element).orElseThrow();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        return CompletableFuture.allOf(builders
                .entrySet()
                .stream()
                .map(entry -> generate(output, entry.getKey(), entry.getValue()))
                .toArray(CompletableFuture[]::new)
        );
    }

    private CompletableFuture<?> generate(CachedOutput output, ResourceLocation statePath, StateBuilder<T, S> builder)
    {
        var path = context
                .packOutput()
                .getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(statePath.getNamespace())
                .resolve("%sstates".formatted(registryType.location().getPath()))
                .resolve("%s.json".formatted(statePath.getPath()));

        return DataProvider.saveStable(output, builder.toJson(), path);
    }

    @Override
    public String getName()
    {
        return "States for %s".formatted(registryType.location());
    }

    public static <T, S extends StateHolder<T, S>> ProviderType<StateProvider<T, S>> register(ResourceLocation providerName, Function<T, StateDefinition<T, S>> stateDefinitionFunction, ResourceKey<? extends Registry<T>> registryType, Function<T, ResourceKey<T>> intrinsicKeyExtractor)
    {
        return ProviderType.register(providerName, context -> new StateProvider<>(context, stateDefinitionFunction, registryType, intrinsicKeyExtractor));
    }

    public static <T, S extends StateHolder<T, S>> ProviderType<StateProvider<T, S>> register(ResourceLocation providerName, Function<T, StateDefinition<T, S>> stateDefinitionFunction, ResourceKey<? extends Registry<T>> registryType)
    {
        return ProviderType.register(providerName, context -> new StateProvider<>(context, stateDefinitionFunction, registryType, null));
    }
}
