package xyz.apex.minecraft.apexcore.common.lib;

import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.ApiStatus;

/**
 * Basic hooks into common platform dependent elements.
 */
@ApiStatus.NonExtendable
public interface GameData extends Services.Service
{
    /**
     * @return The current side we are currently running on.
     */
    PhysicalSide getPhysicalSide();

    /**
     * @return Current version of Minecraft.
     */
    String getVersion();

    /**
     * @return True if running on a snapshot version of Minecraft.
     */
    boolean isSnapshot();

    /**
     * @return True if running in development environment.
     */
    boolean isDevelopment();

    /**
     * @return True if data generation is currently active.
     */
    boolean isDataGenActive();

    /**
     * @return True if running in a MinecraftForge environment.
     */
    boolean isForge();

    /**
     * @return True if running in a Fabric environment.
     */
    boolean isFabric();

    @Override
    default void bootstrap()
    {
        LogManager.getLogger().info("Bootstrapping ApexCore-GameData for Minecraft-{}{}", getVersion(), isDevelopment() ? "-dev" : "");
        Services.Service.super.bootstrap();
    }

    /**
     * @return Global instance.
     */
    static GameData get()
    {
        return Services.GAME_DATA;
    }
}
