package xyz.apex.minecraft.apexcore.common.platform;

import xyz.apex.minecraft.apexcore.common.hooks.Hooks;
import xyz.apex.minecraft.apexcore.common.util.ServiceUtil;

public interface Platform
{
    Platform INSTANCE = ServiceUtil.lookup(Platform.class);

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

    static void bootstrap() {}
}
