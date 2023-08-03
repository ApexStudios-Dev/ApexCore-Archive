package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;

import java.util.function.Consumer;

@ApiStatus.Internal
public final class MenuHooksImpl implements MenuHooks
{
    @Override
    public void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraData)
    {
        player.openMenu(createMenuProvider(menuProvider, extraData));
    }

    @Override
    public MenuProvider createMenuProvider(MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraData)
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
                return menuProvider.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player)
            {
                return menuProvider.createMenu(syncId, inventory, player);
            }
        };
    }
}
