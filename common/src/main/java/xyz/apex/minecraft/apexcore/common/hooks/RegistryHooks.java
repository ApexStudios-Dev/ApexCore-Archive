package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;

public interface RegistryHooks extends PlatformHolder
{
    <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType);

    void openMenu(ServerPlayer player, MenuEntry.ExtendedMenuProvider menuProvider);

    <T extends AbstractContainerMenu> MenuType<T> menuType(MenuEntry.MenuFactory<T> clientMenuConstructor);

    static RegistryHooks getInstance()
    {
        return Hooks.getInstance().registry();
    }
}
