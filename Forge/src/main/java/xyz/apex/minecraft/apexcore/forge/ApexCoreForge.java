package xyz.apex.minecraft.apexcore.forge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

import xyz.apex.minecraft.apexcore.forge.platform.ForgeEvents;
import xyz.apex.minecraft.apexcore.shared.ApexCore;
import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.registry.RegisterEvent;

@Mod(ApexCore.ID)
public final class ApexCoreForge
{
    public static final Logger LOGGER = LogManager.getLogger();

    public ApexCoreForge()
    {
        ApexCore.bootstrap();
        ((ForgeEvents) GamePlatform.INSTANCE.events()).register();

        GamePlatform.EVENTS.registerEvent(event -> {
            LOGGER.info("Registered {} with key {}", event.getRegistryType(), event.registryKey);
        }, RegisterEvent.class);
    }
}
