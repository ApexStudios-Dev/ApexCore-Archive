package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.util.MenuHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

final class FabricMenuHelper implements MenuHelper
{
    @Override
    public void openMenu0(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        player.openMenu(wrapMenuProvider0(provider, extraData));
    }

    @Override
    public void openMenu0(ServerPlayer player, Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        player.openMenu(menuProvider0(displayName, constructor, extraData));
    }

    @Override
    public MenuProvider menuProvider0(Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        return new ExtendedScreenHandlerFactory()
        {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer)
            {
                extraData.accept(buffer);
            }

            @Override
            public Component getDisplayName()
            {
                return displayName;
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
            {
                return constructor.createMenu(windowId, inventory, player);
            }
        };
    }

    @Override
    public MenuProvider wrapMenuProvider0(MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        return new ExtendedScreenHandlerFactory()
        {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer)
            {
                extraData.accept(buffer);
            }

            @Override
            public Component getDisplayName()
            {
                return provider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
            {
                return provider.createMenu(windowId, inventory, player);
            }
        };
    }
}
