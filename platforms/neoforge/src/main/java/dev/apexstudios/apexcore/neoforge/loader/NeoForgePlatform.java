package dev.apexstudios.apexcore.neoforge.loader;

import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

public final class NeoForgePlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist)
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new NeoForgeModLoader();

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
    public RegistryHelper registryHelper(String ownerId)
    {
        return NeoForgeRegistryHelper.get(ownerId);
    }

    @Override
    public NetworkManager networkManager(String ownerId)
    {
        return NeoForgeNetworkManager.get(ownerId);
    }
}
