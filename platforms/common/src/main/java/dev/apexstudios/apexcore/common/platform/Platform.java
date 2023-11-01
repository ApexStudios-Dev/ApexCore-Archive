package dev.apexstudios.apexcore.common.platform;

import dev.apexstudios.apexcore.common.util.Services;

public interface Platform
{
    String ID_FABRIC = "fabric-api";
    String ID_MCFORGE = "forge";
    String ID_NEOFORGE = "neoforge";

    Platform INSTANCE = Services.singleton(Platform.class);

    String id();

    ModLoader modLoader();

    PhysicalSide physicalSide();

    boolean isForge();

    boolean isNeo();

    boolean isFabric();

    PlatformTags tags();

    PlatformRegistries registries();
}
