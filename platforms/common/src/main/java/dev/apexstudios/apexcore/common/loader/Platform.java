package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.ApexCore;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackType;

public interface Platform
{
    ModLoader modLoader();

    PhysicalSide physicalSide();

    default String gameVersion()
    {
        return SharedConstants.VERSION_STRING;
    }

    default int resourceVersion(PackType packType)
    {
        return switch(packType)
        {
            case CLIENT_RESOURCES -> SharedConstants.RESOURCE_PACK_FORMAT;
            case SERVER_DATA -> SharedConstants.DATA_PACK_FORMAT;
        };
    }

    default boolean isSnapshot()
    {
        return SharedConstants.SNAPSHOT;
    }

    default boolean isProduction()
    {
        return !SharedConstants.IS_RUNNING_IN_IDE;
    }

    boolean runningDataGen();

    PlatformFactory factory();

    static Platform get()
    {
        return ApexCore.INSTANCE;
    }
}
