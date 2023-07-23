package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;

import java.util.function.Supplier;

@ApiStatus.Internal
public final class EntityHooksImpl implements EntityHooks
{
    @Override
    public void registerDefaultAttributes(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes)
    {
        FabricDefaultAttributeRegistry.register(entityType.get(), defaultAttributes.get());
    }

    @Override
    public <T extends Mob> void registerSpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        SpawnPlacements.register(entityType.get(), spawnPlacementType, heightmapType, spawnPredicate);
    }
}
