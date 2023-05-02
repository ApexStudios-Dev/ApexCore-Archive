package xyz.apex.minecraft.apexcore.fabric.lib.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryApi;

import java.util.Optional;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class RegistryApiImpl implements RegistryApi
{
    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<? extends T> registryEntryFactory)
    {
        var registry = findRegistry(registryType).orElseThrow();
        Registry.registerForHolder(registry, registryName, registryEntryFactory.get());
    }

    @Override
    public <T> Optional<Holder<T>> getDelegate(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey)
    {
        return findRegistry(registryType).flatMap(registry -> registry.getHolder(registryKey));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> Optional<Registry<T>> findRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        return BuiltInRegistries.REGISTRY.getOptional((ResourceKey) registryType);
    }
}
