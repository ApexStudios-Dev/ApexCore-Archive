package xyz.apex.minecraft.apexcore.shared.event.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import xyz.apex.minecraft.apexcore.shared.event.Event;
import xyz.apex.minecraft.apexcore.shared.event.EventType;
import xyz.apex.minecraft.apexcore.shared.event.SimpleCancelableEvent;

import java.util.List;

public interface ExplosionEvents
{
    EventType<Detonate> DETONATE = EventType.simple();
    EventType<Start> START = EventType.simple();

    interface Base extends Event
    {
        Level getLevel();
        BlockPos getPos();
        Explosion getExplosion();
    }

    record Detonate(Level level, BlockPos pos, Explosion explosion, List<Entity> affectedEntities, List<BlockPos> affectedBlocks) implements Base
    {
        @Override
        public Level getLevel()
        {
            return level;
        }

        @Override
        public BlockPos getPos()
        {
            return pos;
        }

        @Override
        public Explosion getExplosion()
        {
            return explosion;
        }
    }

    final class Start extends SimpleCancelableEvent implements Base
    {
        private final Level level;
        private final BlockPos pos;
        private final Explosion explosion;

        public Start(Level level, BlockPos pos, Explosion explosion)
        {
            super();

            this.level = level;
            this.pos = pos;
            this.explosion = explosion;
        }

        @Override
        public Level getLevel()
        {
            return level;
        }

        @Override
        public BlockPos getPos()
        {
            return pos;
        }

        @Override
        public Explosion getExplosion()
        {
            return explosion;
        }
    }
}
