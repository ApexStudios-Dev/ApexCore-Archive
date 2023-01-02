package xyz.apex.minecraft.apexcore.shared.registry.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

import java.util.function.Consumer;

public final class MenuEntry<M extends AbstractContainerMenu> extends RegistryEntry<MenuType<M>>
{
    public MenuEntry(AbstractRegistrar<?> owner, ResourceKey<? super MenuType<M>> registryKey)
    {
        super(owner, Registries.MENU, registryKey);
    }

    public boolean is(@Nullable MenuType<?> other)
    {
        return isPresent() && get() == other;
    }

    public void open(ServerPlayer player, Component displayName, Consumer<FriendlyByteBuf> data)
    {
        // TODO:
    }

    public void open(ServerPlayer player, Component displayName)
    {
        open(player, displayName, data -> {});
    }
}
