package xyz.apex.minecraft.apexcore.forge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

import xyz.apex.minecraft.apexcore.shared.ApexCore;

@Mod(ApexCore.ID)
public final class ApexCoreForge
{
    public static final Logger LOGGER = LogManager.getLogger();

    public ApexCoreForge()
    {
        ApexCore.bootstrap();
    }
}
