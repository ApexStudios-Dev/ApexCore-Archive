package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * Helper interface used to register entries to backing platform registries.
 */
@FunctionalInterface
public interface RegistryHelper
{
    /**
     * Registers new entry with given name for the provided registry type.
     *
     * @param registryType Type of Registry to register new entry for.
     * @param registryName Name of newly registered entry.
     * @param entryFactory Factory used to construct the registered entry.
     * @param <T> Type of Registry.
     * @param <R> Type of entry.
     */
    <T, R extends T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> entryFactory);
}
