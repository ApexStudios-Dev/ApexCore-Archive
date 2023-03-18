package xyz.apex.minecraft.apexcore.common.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;

public interface Internals
{
    <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType);
}
