package xyz.apex.minecraft.apexcore.forge.platform;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;

import xyz.apex.minecraft.apexcore.shared.event.EventType;
import xyz.apex.minecraft.apexcore.shared.event.events.ExplosionEvents;

final class ForgeEvents extends ForgePlatformHolder
{
    ForgeEvents(ForgePlatform platform)
    {
        super(platform);

        MinecraftForge.EVENT_BUS.addListener(this::onExplosionDetonate);
        MinecraftForge.EVENT_BUS.addListener(this::onExplosionStart);
    }

    private void onExplosionStart(ExplosionEvent.Start event)
    {
        var explosion = event.getExplosion();

        processEvent(ExplosionEvents.START, new ExplosionEvents.Start(
                event.getLevel(),
                new BlockPos(explosion.getPosition()),
                explosion
        ), event);
    }

    private void onExplosionDetonate(ExplosionEvent.Detonate event)
    {
        var explosion = event.getExplosion();

        ExplosionEvents.DETONATE.post(new ExplosionEvents.Detonate(
                event.getLevel(),
                new BlockPos(explosion.getPosition()),
                explosion,
                event.getAffectedEntities(),
                event.getAffectedBlocks()
        ));
    }

    public static <T extends xyz.apex.minecraft.apexcore.shared.event.Event> boolean processEvent(EventType<T> event, T val, Event forgeEvent)
    {
        var canceled = event.post(val);
        if(canceled && forgeEvent.isCancelable()) forgeEvent.setCanceled(canceled);
        return canceled;
    }
}
