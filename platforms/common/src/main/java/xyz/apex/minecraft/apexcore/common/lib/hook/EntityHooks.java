package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Hooks for various entity related things.
 */
@ApiStatus.NonExtendable
public interface EntityHooks
{
    /**
     * Returns true if given entity is a fake player.
     *
     * @param entity Entity to validate.
     * @return True if given entity is a fake player.
     */
    boolean isFakePlayer(@Nullable Entity entity);

    /**
     * Registers default attributes for given entity type.
     *
     * @param entityType        Entity type to register default attributes for.
     * @param defaultAttributes Default attributes to be registered.
     */
    void registerDefaultAttribute(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes);

    /**
     * Registers spawn placement details for the given entity type.
     *
     * @param entityType         Entity type to register spawn placement for.
     * @param spawnPlacementType Type of spawn placement.
     * @param heightmapType      Where on the height map this entity can spawn.
     * @param spawnPredicate     Predicate stating if the entity is allowed to spawn or not.
     * @param <T>                Type of entity.
     */
    <T extends Mob> void registerSpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate);

    /**
     * @return Global instance.
     */
    static EntityHooks get()
    {
        return Hooks.get().entity();
    }
}
