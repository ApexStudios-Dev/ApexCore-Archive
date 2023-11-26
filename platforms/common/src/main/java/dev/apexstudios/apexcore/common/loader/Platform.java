package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.util.MenuHelper;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackType;

public interface Platform
{
    ModLoader modLoader();

    PhysicalSide physicalSide();

    boolean isProduction();

    boolean runningDataGen();

    PlatformFactory factory();

    MenuHelper menuHelper();

    static Platform get()
    {
        return ApexCore.INSTANCE;
    }

    static String gameVersion()
    {
        return SharedConstants.getCurrentVersion().getName();
    }

    static int resourceVersion(PackType packType)
    {
        return SharedConstants.getCurrentVersion().getPackVersion(packType);
    }

    static boolean isSnapshot()
    {
        return !SharedConstants.getCurrentVersion().isStable();
    }
}
