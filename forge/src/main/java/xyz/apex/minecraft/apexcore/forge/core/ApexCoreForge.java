package xyz.apex.minecraft.apexcore.forge.core;

import net.minecraftforge.fml.common.Mod;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.forge.lib.EventBuses;

@Mod(ApexCore.ID)
public final class ApexCoreForge implements ApexCore
{
    public ApexCoreForge()
    {
        bootstrap();
        EventBuses.registerForJavaFML();
    }
}
