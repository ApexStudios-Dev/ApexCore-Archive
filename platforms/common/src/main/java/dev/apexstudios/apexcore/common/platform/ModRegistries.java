package dev.apexstudios.apexcore.common.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface ModRegistries
{
    String ownerId();

    default ResourceLocation id(String registrationName)
    {
        return new ResourceLocation(ownerId(), registrationName);
    }

    default <T, R extends T> Supplier<R> register(ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> registryEntryFactory)
    {
        return PlatformRegistries.get().register(registryType, id(registrationName), registryEntryFactory);
    }

    default <T, R extends T> Holder.Reference<R> registerForHolder(ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> registryEntryFactory)
    {
        return PlatformRegistries.get().registerForHolder(registryType, id(registrationName), registryEntryFactory);
    }

    static ModRegistries get(String ownerId)
    {
        return PlatformRegistries.get().forMod(ownerId);
    }
}
