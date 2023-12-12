package dev.apexstudios.apexcore.neoforge.loader;

import dev.apexstudios.apexcore.common.util.MenuHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuConstructor;
import net.neoforged.neoforge.network.NetworkHooks;

import java.util.function.Consumer;

final class NeoForgeMenuHelper implements MenuHelper
{
    @Override
    public void openMenu0(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        NetworkHooks.openScreen(player, provider, extraData);
    }

    @Override
    public void openMenu0(ServerPlayer player, Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        NetworkHooks.openScreen(player, new SimpleMenuProvider(constructor, displayName), extraData);
    }

    @Override
    public MenuProvider menuProvider0(Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        return new SimpleMenuProvider(constructor, displayName);
    }

    @Override
    public MenuProvider wrapMenuProvider0(MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        return provider;
    }
}