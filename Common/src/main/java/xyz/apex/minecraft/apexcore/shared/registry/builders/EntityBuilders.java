package xyz.apex.minecraft.apexcore.shared.registry.builders;

import net.minecraft.world.entity.Entity;

import xyz.apex.minecraft.apexcore.shared.registry.entry.EntityEntry;

public interface EntityBuilders
{
    static <T extends Entity> EntityBuilder<T> builder(String modId, String registryName, EntityBuilder.EntityFactory<T> factory)
    {
        return new EntityBuilder<>(modId, registryName, factory);
    }

    static <T extends Entity> EntityEntry<T> simple(String modId, String registryName, EntityBuilder.EntityFactory<T> factory)
    {
        return builder(modId, registryName, factory).register();
    }
}
