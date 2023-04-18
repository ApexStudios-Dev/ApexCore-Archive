package xyz.apex.minecraft.apexcore.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import xyz.apex.utils.events.Event;

public final class FakePlayerEvent implements Event
{
    public final Entity entity;
    public final boolean platformConsidersFakePlayer;
    private boolean isFakePlayer = false;

    public FakePlayerEvent(Entity entity, boolean platformConsidersFakePlayer)
    {
        this.entity = entity;
        this.platformConsidersFakePlayer = platformConsidersFakePlayer;
    }

    public void markAsFakePlayer()
    {
        isFakePlayer = true;
    }

    public boolean markedAsFakePlayer()
    {
        return isFakePlayer;
    }

    public boolean assumedToBeFakePlayer()
    {
        return platformConsidersFakePlayer || isFakePlayer_Default(entity);
    }

    // if this returns true, the entity will be considered as a fake player to our mods
    public boolean shouldBeConsideredAsFakePlayer()
    {
        return markedAsFakePlayer() || assumedToBeFakePlayer();
    }

    static boolean isFakePlayer_Default(Entity entity)
    {
        return entity instanceof ServerPlayer && entity.getClass() != ServerPlayer.class;
    }
}
