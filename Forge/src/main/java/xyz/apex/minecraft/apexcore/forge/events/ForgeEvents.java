package xyz.apex.minecraft.apexcore.forge.events;

import net.minecraft.core.BlockPos;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import xyz.apex.minecraft.apexcore.shared.ApexCore;

@Mod.EventBusSubscriber(modid = ApexCore.ID)
public final class ForgeEvents
{
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event)
    {
        var explosion = event.getExplosion();
        var pos = new BlockPos(explosion.getPosition());

        var evt = new xyz.apex.minecraft.apexcore.shared.event.events.ExplosionEvent.Detonate(event.getLevel(), pos, explosion, event.getAffectedEntities(), event.getAffectedBlocks());
        xyz.apex.minecraft.apexcore.shared.event.events.ExplosionEvent.DETONATE.post(evt);
    }
}
