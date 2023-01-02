package xyz.apex.minecraft.apexcore.shared.registry.entry;

import dev.architectury.registry.menu.MenuRegistry;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

import java.util.function.Consumer;

public final class MenuEntry<M extends AbstractContainerMenu> extends RegistryEntry<MenuType<M>> implements MenuConstructor
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
        MenuRegistry.openExtendedMenu(player, asProvider(displayName), data);
    }

    public void open(ServerPlayer player, Component displayName)
    {
        open(player, displayName, data -> {});
    }

    public MenuProvider asProvider(Component displayName)
    {
        return new SimpleMenuProvider(this, displayName);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player)
    {
        return get().create(containerId, inventory);
    }
}
