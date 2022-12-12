package xyz.apex.minecraft.apexcore.shared.platform;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import xyz.apex.minecraft.apexcore.shared.event.EventManager;

import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.Set;

public final class GamePlatform
{
    // public for internal usages only
    // modders should make use of the static method below
    @ApiStatus.Internal
    public static final Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    public static final EventManager EVENTS = new EventManager();

    public static PlatformEvents events()
    {
        return INSTANCE.events();
    }

    public static boolean isDevelopment()
    {
        return INSTANCE.isDevelopment();
    }

    public static boolean isDataGeneration()
    {
        return INSTANCE.isDataGeneration();
    }

    public static boolean isClient()
    {
        return INSTANCE.isClient();
    }

    public static boolean isDedicatedServer()
    {
        return INSTANCE.isDedicatedServer();
    }

    public static Platform.Type getPlatformType()
    {
        return INSTANCE.getPlatformType();
    }

    public static Logger getLogger()
    {
        return INSTANCE.getLogger();
    }

    public static boolean isRunningOn(Platform.Type platform)
    {
        return INSTANCE.isRunningOn(platform);
    }

    public static boolean isForge()
    {
        return INSTANCE.isForge();
    }

    public static boolean isFabric()
    {
        return INSTANCE.isFabric();
    }

    public static Path getGameDir()
    {
        return INSTANCE.getGameDir();
    }

    public static Path getModsDir()
    {
        return INSTANCE.getModsDir();
    }

    public static Set<String> getMods()
    {
        return INSTANCE.getMods();
    }

    public static boolean isModInstalled(String modId)
    {
        return INSTANCE.isModInstalled(modId);
    }
}
