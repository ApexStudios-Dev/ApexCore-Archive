package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricPlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType())
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new FabricModLoader();

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
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean runningDataGen()
    {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public RegistryHelper registryHelper(String ownerId)
    {
        return FabricRegistryHelper.get(ownerId);
    }

    @Override
    public NetworkManager networkManager(String ownerId)
    {
        return FabricNetworkManager.get(ownerId);
    }
}
