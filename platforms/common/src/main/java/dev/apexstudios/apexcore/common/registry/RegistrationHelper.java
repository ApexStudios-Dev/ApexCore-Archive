package dev.apexstudios.apexcore.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@FunctionalInterface
public interface RegistrationHelper
{
    <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> valueKey, T value);
}
