package xyz.apex.minecraft.apexcore.forge.platform;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;

import xyz.apex.minecraft.apexcore.shared.event.events.ExplosionEvents;

final class ForgeEvents extends ForgePlatformHolder
{
    ForgeEvents(ForgePlatform platform)
    {
        super(platform);

        MinecraftForge.EVENT_BUS.addListener(this::onExplosionDetonate);
    }

    void onExplosionDetonate(ExplosionEvent.Detonate event)
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
}
