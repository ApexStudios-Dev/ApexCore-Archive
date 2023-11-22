package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.network.NetworkManager;
import net.minecraft.world.item.CreativeModeTab;

public interface PlatformFactory
{
    RegistryHelper registryHelper(String ownerId);

    NetworkManager networkManager(String ownerId);

    CreativeModeTab.Builder creativeModeTabBuilder();

    static PlatformFactory get()
    {
        return Platform.get().factory();
    }
}
