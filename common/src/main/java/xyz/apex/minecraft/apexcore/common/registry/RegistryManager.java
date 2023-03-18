package xyz.apex.minecraft.apexcore.common.registry;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import xyz.apex.minecraft.apexcore.common.platform.Platform;

import java.util.Map;

public final class RegistryManager
{
    private static final Map<String, RegistryManager> INSTANCES = Maps.newHashMap();

    private final String ownerId;
    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries = Maps.newHashMap();

    private RegistryManager(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    @SuppressWarnings("unchecked")
    public <T> DeferredRegister<T> getRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        return (DeferredRegister<T>) registries.computeIfAbsent(registryType, $ -> Platform.INSTANCE.internals().deferredRegister(ownerId, registryType));
    }

    public static RegistryManager get(String ownerId)
    {
        return INSTANCES.computeIfAbsent(ownerId, RegistryManager::new);
    }
}
