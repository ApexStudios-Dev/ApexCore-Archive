package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@FunctionalInterface
public interface RegistryHelper
{
    <T, R extends T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> entryFactory);
}
