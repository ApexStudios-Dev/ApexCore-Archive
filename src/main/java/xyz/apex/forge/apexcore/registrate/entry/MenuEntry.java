package xyz.apex.forge.apexcore.registrate.entry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

import java.util.function.Consumer;

public final class MenuEntry<MENU extends AbstractContainerMenu> extends RegistryEntry<MenuType<MENU>>
{
	public MenuEntry(CoreRegistrate<?> owner, RegistryObject<MenuType<MENU>> delegate)
	{
		super(owner, delegate);
	}

	public MENU create(int windowId, Inventory inventory)
	{
		return get().create(windowId, inventory);
	}

	public MENU create(int windowId, Inventory inventory, @Nullable FriendlyByteBuf buffer)
	{
		return buffer == null ? create(windowId, inventory) : get().create(windowId, inventory, buffer);
	}

	public MenuConstructor asProvider()
	{
		return (windowId, inventory, player) -> create(windowId, inventory);
	}

	public void open(ServerPlayer player, Component displayName)
	{
		open(player, displayName, asProvider());
	}

	public void open(ServerPlayer player, Component displayName, Consumer<FriendlyByteBuf> buffer)
	{
		open(player, displayName, asProvider(), buffer);
	}

	public void open(ServerPlayer player, Component displayName, MenuConstructor provider)
	{
		NetworkHooks.openScreen(player, new SimpleMenuProvider(provider, displayName));
	}

	public void open(ServerPlayer player, Component displayName, MenuConstructor provider, Consumer<FriendlyByteBuf> buffer)
	{
		NetworkHooks.openScreen(player, new SimpleMenuProvider(provider, displayName), buffer);
	}

	public static <MENU extends AbstractContainerMenu> MenuEntry<MENU> cast(com.tterrag.registrate.util.entry.RegistryEntry<MenuType<MENU>> registryEntry)
	{
		return cast(MenuEntry.class, registryEntry);
	}
}
