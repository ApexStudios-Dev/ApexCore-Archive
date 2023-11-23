package dev.apexstudios.apexcore.mcforge.loader;

import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.registry.generic.MenuFactory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.extensions.IForgeMenuType;

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

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuFactory<T> menuFactory)
    {
        return IForgeMenuType.create(menuFactory::create);
    }
}
