package xyz.apex.minecraft.apexcore.forge.lib.hook;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;
import xyz.apex.minecraft.apexcore.forge.core.ModEvents;

import java.util.function.Supplier;

final class EntityHooksImpl implements EntityHooks
{
    @Override
    public boolean isFakePlayer(@Nullable Entity entity)
    {
        return entity instanceof FakePlayer;
    }

    @Override
    public void registerDefaultAttribute(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes)
    {
        ModEvents.active().addListener(EntityAttributeCreationEvent.class, event -> event.put(entityType.get(), defaultAttributes.get().build()));
    }

    @Override
    public <T extends Mob> void registerSpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        ModEvents.active().addListener(SpawnPlacementRegisterEvent.class, event -> event.register(entityType.get(), spawnPlacementType, heightmapType, spawnPredicate, SpawnPlacementRegisterEvent.Operation.AND));
    }
}
