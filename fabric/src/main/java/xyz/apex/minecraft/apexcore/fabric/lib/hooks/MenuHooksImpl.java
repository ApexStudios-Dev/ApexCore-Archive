package xyz.apex.minecraft.apexcore.fabric.lib.hooks;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;

import java.util.function.Consumer;

final class MenuHooksImpl implements MenuHooks
{
    @Override
    public <T extends AbstractContainerMenu> void openMenu(ServerPlayer player, Component displayName, ClientMenuConstructor<T> clientMenuConstructor, Consumer<FriendlyByteBuf> extraData)
    {
        player.openMenu(createMenuProvider(displayName, clientMenuConstructor, extraData));
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> create(NetworkMenuConstructor<T> networkMenuConstructor)
    {
        return new ExtendedScreenHandlerType<>(networkMenuConstructor::create);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuProvider createMenuProvider(Component displayName, ClientMenuConstructor<T> clientMenuConstructor, Consumer<FriendlyByteBuf> extraData)
    {
        return new ExtendedScreenHandlerFactory()
        {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf data)
            {
                extraData.accept(data);
            }

            @Override
            public Component getDisplayName()
            {
                return displayName;
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
            {
                return clientMenuConstructor.create(windowId, playerInventory);
            }
        };
    }
}
