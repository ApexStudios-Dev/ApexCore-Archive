package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Factory interface for constructing new Entity entries.
 *
 * @param <T> Type of Entity to be constructed.
 */
@FunctionalInterface
public interface EntityFactory<T extends Entity> extends EntityType.EntityFactory<T>
{
    /**
     * Returns new Entity instance with given properties.
     *
     * @param entityType EntityType for newly constructed Entity.
     * @param level Level Entity exists within.
     * @return Newly constructed Entity with given properties.
     */
    @Override
    T create(EntityType<T> entityType, Level level);
}
