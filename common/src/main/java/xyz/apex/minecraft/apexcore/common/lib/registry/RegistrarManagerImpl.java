package xyz.apex.minecraft.apexcore.common.lib.registry;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.function.Consumer;

final class RegistrarManagerImpl implements RegistrarManager
{
    private static final Map<String, RegistrarManager> REGISTRAR_MANAGER_MAP = Maps.newHashMap();

    private final Map<ResourceKey<? extends Registry<?>>, Registrar<?>> registrarMap = Maps.newConcurrentMap();
    private final String ownerId;
    private boolean registered = false;

    private RegistrarManagerImpl(String ownerId)
    {
        this.ownerId = ownerId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Registrar<T> get(ResourceKey<? extends Registry<T>> registryType)
    {
        return (Registrar<T>) registrarMap.computeIfAbsent(registryType, $ -> new RegistrarImpl<>(this, registryType));
    }

    @Override
    public String getOwnerId()
    {
        return ownerId;
    }

    @Override
    public <T> void addListener(ResourceKey<? extends Registry<T>> registryType, Consumer<Registrar<T>> listener)
    {
        RegistryApi.get().addListener(registryType, ownerId, listener);
    }

    @Override
    public boolean isRegistered()
    {
        return registered;
    }

    @Override
    public void register()
    {
        if(registered) return;

        BuiltInRegistries.REGISTRY.registryKeySet().forEach(registryType -> {
            var registry = registrarMap.get(registryType);
            if(registry != null) registry.register();
        });

        registered = true;
    }

    public static RegistrarManager get(String ownerId)
    {
        return REGISTRAR_MANAGER_MAP.computeIfAbsent(ownerId, RegistrarManagerImpl::new);
    }
}
