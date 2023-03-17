package xyz.apex.minecraft.apexcore.forge.platform;

import net.minecraft.SharedConstants;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.platform.Side;

public final class ForgePlatform implements Platform
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
        return !FMLEnvironment.production;
    }

    @Override
    public boolean isDataGenActive()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public Side getPhysicalSide()
    {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> Side.CLIENT;
            case DEDICATED_SERVER -> Side.SERVER;
        };
    }
}
