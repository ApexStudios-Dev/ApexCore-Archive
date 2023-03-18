package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import xyz.apex.minecraft.apexcore.common.platform.Internals;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;

import java.util.Map;

final class ForgeInternals implements Internals
{
    private final Map<String, ModInternals> modInternals = Maps.newHashMap();

    @Override
    public <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return modInternals.computeIfAbsent(ownerId, ModInternals::new).deferredRegister(registryType);
    }

    private static final class ModInternals
    {
        private final String ownerId;
        private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries = Maps.newHashMap();

        private ModInternals(String ownerId)
        {
            this.ownerId = ownerId;
        }

        @SuppressWarnings("unchecked")
        private <T> ForgeDeferredRegister<T> deferredRegister(ResourceKey<? extends Registry<T>> registryType)
        {
            return (ForgeDeferredRegister<T>) registries.computeIfAbsent(registryType, $ -> new ForgeDeferredRegister<>(ownerId, registryType));
        }
    }
}
