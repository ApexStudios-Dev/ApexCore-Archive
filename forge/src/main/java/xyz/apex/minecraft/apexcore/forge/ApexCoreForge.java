package xyz.apex.minecraft.apexcore.forge;

import net.minecraftforge.fml.common.Mod;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.forge.platform.ForgeModLoader;

@Mod(ApexCore.ID)
public final class ApexCoreForge implements ApexCore
{
    public ApexCoreForge()
    {
        ForgeModLoader.onPreLaunch();
    }
}
