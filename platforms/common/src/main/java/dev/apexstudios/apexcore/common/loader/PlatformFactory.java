package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.registry.generic.MenuFactory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;

public interface PlatformFactory
{
    RegistryHelper registryHelper(String ownerId);

    NetworkManager networkManager(String ownerId);

    CreativeModeTab.Builder creativeModeTabBuilder();

    <T extends AbstractContainerMenu> MenuType<T> menuType(MenuFactory<T> menuFactory);

    static PlatformFactory get()
    {
        return Platform.get().factory();
    }
}
