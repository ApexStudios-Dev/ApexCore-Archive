package xyz.apex.minecraft.apexcore.data;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import xyz.apex.minecraft.apexcore.shared.ApexCore;

@Mod.EventBusSubscriber(modid = ApexCore.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ApexCoreForgeData
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
    }
}
