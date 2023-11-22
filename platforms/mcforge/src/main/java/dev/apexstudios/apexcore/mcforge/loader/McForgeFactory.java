package dev.apexstudios.apexcore.mcforge.loader;

import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import net.minecraft.world.item.CreativeModeTab;

final class McForgeFactory implements PlatformFactory
{
    @Override
    public RegistryHelper registryHelper(String ownerId)
    {
        return McForgeRegistryHelper.get(ownerId);
    }

    @Override
    public NetworkManager networkManager(String ownerId)
    {
        return McForgeNetworkManager.get(ownerId);
    }

    @Override
    public CreativeModeTab.Builder creativeModeTabBuilder()
    {
        return CreativeModeTab.builder();
    }
}
