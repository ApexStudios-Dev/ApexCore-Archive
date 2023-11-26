package dev.apexstudios.apexcore.common.util;

import dev.apexstudios.apexcore.common.loader.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.MenuConstructor;

import java.util.function.Consumer;

public interface MenuHelper
{
    void openMenu0(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData);

    void openMenu0(ServerPlayer player, Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData);

    MenuProvider menuProvider0(Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData);

    MenuProvider wrapMenuProvider0(MenuProvider provider, Consumer<FriendlyByteBuf> extraData);

    static void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        helper().openMenu0(player, provider, extraData);
    }

    static void openMenu(ServerPlayer player, Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        helper().openMenu0(player, displayName, constructor, extraData);
    }

    static MenuProvider menuProvider(Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        return helper().menuProvider0(displayName, constructor, extraData);
    }

    static MenuProvider wrapMenuProvider(MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        return helper().wrapMenuProvider0(provider, extraData);
    }

    private static MenuHelper helper()
    {
        return Platform.get().menuHelper();
    }
}
