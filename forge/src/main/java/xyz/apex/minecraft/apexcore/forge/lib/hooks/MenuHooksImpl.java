package xyz.apex.minecraft.apexcore.forge.lib.hooks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;

import java.util.function.Consumer;

final class MenuHooksImpl implements MenuHooks
{
    @Override
    public <T extends AbstractContainerMenu> void openMenu(ServerPlayer player, Component displayName, ClientMenuConstructor<T> clientMenuConstructor, Consumer<FriendlyByteBuf> extraData)
    {
        NetworkHooks.openScreen(player, createMenuProvider(displayName, clientMenuConstructor, extraData), extraData);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> create(NetworkMenuConstructor<T> networkMenuConstructor)
    {
        return IForgeMenuType.create(networkMenuConstructor::create);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuProvider createMenuProvider(Component displayName, ClientMenuConstructor<T> clientMenuConstructor, Consumer<FriendlyByteBuf> extraData)
    {
        return new SimpleMenuProvider((windowId, playerInventory, player) -> clientMenuConstructor.create(windowId, playerInventory), displayName);
    }
}
