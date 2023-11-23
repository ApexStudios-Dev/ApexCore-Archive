package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.registry.generic.MenuFactory;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;

final class FabricFactory implements PlatformFactory
{
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

    @Override
    public CreativeModeTab.Builder creativeModeTabBuilder()
    {
        return FabricItemGroup.builder();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuFactory<T> menuFactory)
    {
        return new ExtendedScreenHandlerType<>(menuFactory::create);
    }
}
