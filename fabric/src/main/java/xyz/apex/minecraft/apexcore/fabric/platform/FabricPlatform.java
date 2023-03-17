package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.platform.Side;

public final class FabricPlatform implements Platform
{
    @Override
    public String getMinecraftVersion()
    {
        return SharedConstants.VERSION_STRING;
    }

    @Override
    public boolean isSnapshot()
    {
        return SharedConstants.SNAPSHOT;
    }

    @Override
    public boolean isDevelopment()
    {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isDataGenActive()
    {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public Side getPhysicalSide()
    {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> Side.CLIENT;
            case SERVER -> Side.SERVER;
        };
    }
}
