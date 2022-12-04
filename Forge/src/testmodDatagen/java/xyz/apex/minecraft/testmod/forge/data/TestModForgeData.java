package xyz.apex.minecraft.testmod.forge.data;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import xyz.apex.minecraft.testmod.shared.TestMod;

@Mod.EventBusSubscriber(modid = TestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TestModForgeData
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
    }
}
