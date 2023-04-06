package xyz.apex.minecraft.apexcore.common.platform;

import xyz.apex.minecraft.apexcore.common.hooks.Hooks;
import xyz.apex.utils.core.ServiceHelper;

import java.nio.file.Path;

public interface Platform
{
    Platform INSTANCE = ServiceHelper.singleton(Platform.class);

    String FORGE = "forge";
    String FABRIC = "fabricloader";
    String QUILT = "quilt";

    String getMinecraftVersion();

    boolean isSnapshot();

    boolean isDevelopment();

    boolean isDataGenActive();

    Side getPhysicalSide();

    Hooks hooks();

    ModLoader modLoader();

    Path getGameDir();

    Path getConfigDir();

    static void bootstrap() {}
}
