package dev.apexstudios.apexcore.mcforge.loader;

import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.util.MenuHelper;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;

public final class McForgePlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist)
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new McForgeModLoader();
    private final PlatformFactory factory = new McForgeFactory();
    private final MenuHelper menuHelper = new McForgeMenuHelper();

    @Override
    public ModLoader modLoader()
    {
        return modLoader;
    }

    @Override
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public boolean isProduction()
    {
        return FMLEnvironment.production;
    }

    @Override
    public boolean runningDataGen()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public PlatformFactory factory()
    {
        return factory;
    }

    @Override
    public MenuHelper menuHelper()
    {
        return menuHelper;
    }
}
