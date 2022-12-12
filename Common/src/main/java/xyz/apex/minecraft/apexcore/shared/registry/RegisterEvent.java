package xyz.apex.minecraft.apexcore.shared.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.event.Event;

public final class RegisterEvent<T> extends Event
{
    public final Registry<T> registry;
    public final ResourceKey<T> registryKey;
    public final T value;

    public RegisterEvent(Registry<T> registry, ResourceKey<T> registryKey, T value)
    {
        this.registry = registry;
        this.registryKey = registryKey;
        this.value = value;
    }

    public ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registry.key();
    }

    public ResourceLocation getRegistryName()
    {
        return registryKey.location();
    }

    public String getModId()
    {
        return getRegistryName().getNamespace();
    }

    public String getName()
    {
        return getRegistryName().getPath();
    }
}
