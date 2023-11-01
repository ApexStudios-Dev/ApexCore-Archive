package dev.apexstudios.apexcore.fabric.platform;

import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.platform.*;
import net.fabricmc.loader.api.FabricLoader;

public final class PlatformImpl implements Platform
{
    private final PhysicalSide physicalSide = switch (FabricLoader.getInstance().getEnvironmentType()) {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    private final ModLoader modLoader = new ModLoaderImpl();
    private final PlatformRegistries registries = new PlatformRegistriesImpl();
    private final PlatformTags tags = new PlatformTagsImpl();

    @Override
    public String id()
    {
        return ID_FABRIC;
    }

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
    public boolean isForge()
    {
        return false;
    }

    @Override
    public boolean isNeo()
    {
        return false;
    }

    @Override
    public boolean isFabric()
    {
        return true;
    }

    @Override
    public PlatformTags tags()
    {
        return tags;
    }

    @Override
    public PlatformRegistries registries()
    {
        return registries;
    }

    public void onInitialize()
    {
        ApexCore.LOGGER.info("Initializing Fabric Platform!");
        ((ModLoaderImpl) modLoader).loadMods();
        ((PlatformRegistriesImpl) registries).register();
    }
}
