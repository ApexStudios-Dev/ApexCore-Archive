package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;

import java.util.function.Consumer;

@ApiStatus.Internal
public final class MenuHooksImpl implements MenuHooks
{
    @Override
    public InteractionResult openMenu(Player player, Component title, MenuConstructor menuConstructor, Consumer<FriendlyByteBuf> extraData)
    {
        // no need to check instance for ServerPlayer
        // as the method returns empty optional if on wrong side or menu was not opened
        // only ServerPlayer returns a none empty optional
        var openedMenu = player.openMenu(createMenuProvider(title, menuConstructor, extraData)).isPresent();
        // see InteractionResultHelper
        // if menu opened successfully we return `succeedAndSwingArmBothSides`
        // if menu did not open we return `noActionTaken`
        return openedMenu ? InteractionResult.sidedSuccess(player.level().isClientSide) : InteractionResult.PASS;
    }

    @Override
    public MenuProvider createMenuProvider(Component title, MenuConstructor menuConstructor, Consumer<FriendlyByteBuf> extraData)
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
                return title;
            }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player)
            {
                return menuConstructor.createMenu(syncId, inventory, player);
            }
        };
    }
}
