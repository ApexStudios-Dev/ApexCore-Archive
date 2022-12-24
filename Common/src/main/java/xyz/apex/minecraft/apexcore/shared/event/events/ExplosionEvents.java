package xyz.apex.minecraft.apexcore.shared.event.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import xyz.apex.minecraft.apexcore.shared.event.Event;
import xyz.apex.minecraft.apexcore.shared.event.SimpleEvent;

import java.util.List;

public interface ExplosionEvents
{
    Event<Detonate> DETONATE = new SimpleEvent<>();

    record Detonate(Level level, BlockPos pos, Explosion explosion, List<Entity> affectedEntities, List<BlockPos> affectedBlocks) {}
}
