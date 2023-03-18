package xyz.apex.minecraft.apexcore.common.platform;

import java.util.ServiceLoader;

public interface Platform
{
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    String FORGE = "forge";
    String FABRIC = "fabricloader";
    String QUILT = "quilt";

    String getMinecraftVersion();

    boolean isSnapshot();

    boolean isDevelopment();

    boolean isDataGenActive();

    Side getPhysicalSide();

    Internals internals();
}
