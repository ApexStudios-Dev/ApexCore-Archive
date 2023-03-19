package xyz.apex.minecraft.apexcore.forge.platform;

import net.minecraft.SharedConstants;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.platform.Side;
import xyz.apex.minecraft.apexcore.forge.hooks.ForgeHooks;

public final class ForgePlatform implements Platform
{
    private final ForgeHooks hooks;
    private final ForgeModLoader modLoader;

    public ForgePlatform()
    {
        hooks = new ForgeHooks(this);
        modLoader = new ForgeModLoader(this);
    }

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

    @Override
    public ForgeHooks hooks()
    {
        return hooks;
    }

    @Override
    public ForgeModLoader modLoader()
    {
        return modLoader;
    }
}
