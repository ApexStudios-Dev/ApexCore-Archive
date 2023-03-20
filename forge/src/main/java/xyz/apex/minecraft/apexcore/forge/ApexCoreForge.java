package xyz.apex.minecraft.apexcore.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.forge.data.ApexCoreForgeData;

@Mod(ApexCore.ID)
public final class ApexCoreForge implements ApexCore
{
    public ApexCoreForge()
    {
        bootstrap();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ApexCoreForgeData::onGatherData);
    }
}
