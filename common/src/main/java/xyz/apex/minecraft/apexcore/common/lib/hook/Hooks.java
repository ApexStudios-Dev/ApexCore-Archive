package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

import java.util.function.Supplier;

/**
 * Interface holding all known hooks
 */
@ApiStatus.NonExtendable
public interface Hooks extends Services.Service
{
    /**
     * Hooks for various entity related things.
     */
    EntityHooks entity();

    /**
     * Hooks for registering custom game rule types.
     */
    GameRuleHooks gameRules();

    /**
     * Various for registering new and modifying existing creative mode tabs.
     */
    CreativeModeTabHooks creativeModeTabs();

    /**
     * Hooks for various menu related things.
     */
    MenuHooks menu();

    /**
     * Hooks for registering various renderer elements.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    RegisterRendererHooks registerRenderer();

    /**
     * Hooks for registering block and item color handlers.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    RegisterColorHandlerHooks registerColorHandler();

    /**
     * Registers default attributes for given entity type.
     *
     * @param entityType        Entity type to register default attributes for.
     * @param defaultAttributes Default attributes to be registered.
     */
    void registerEntityDefaultAttribute(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes);

    /**
     * Registers spawn placement details for the given entity type.
     *
     * @param entityType         Entity type to register spawn placement for.
     * @param spawnPlacementType Type of spawn placement.
     * @param heightmapType      Where on the height map this entity can spawn.
     * @param spawnPredicate     Predicate stating if the entity is allowed to spawn or not.
     * @param <T>                Type of entity.
     */
    <T extends Mob> void registerEntitySpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate);

    /**
     * @return Global instance.
     */
    static Hooks get()
    {
        return Services.HOOKS;
    }
}
