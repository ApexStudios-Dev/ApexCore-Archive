package dev.apexstudios.apexcore.neoforge.loader;

import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.registry.generic.MenuFactory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

final class NeoForgeFactory implements PlatformFactory
{
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

    @Override
    public CreativeModeTab.Builder creativeModeTabBuilder()
    {
        return CreativeModeTab.builder();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuFactory<T> menuFactory)
    {
        return IMenuTypeExtension.create(menuFactory::create);
    }
}
