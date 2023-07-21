package xyz.apex.minecraft.apexcore.neoforge.lib.hook;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;
import xyz.apex.minecraft.apexcore.neoforge.core.ModEvents;

import java.util.function.Supplier;

final class EntityHooksImpl implements EntityHooks
{
    @Override
    public void registerDefaultAttributes(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes)
    {
        ModEvents.active().addListener(EntityAttributeCreationEvent.class, event -> event.put(entityType.get(), defaultAttributes.get().build()));
    }

    @Override
    public <T extends Mob> void registerSpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        ModEvents.active().addListener(SpawnPlacementRegisterEvent.class, event -> event.register(entityType.get(), spawnPlacementType, heightmapType, spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND));
    }
}
