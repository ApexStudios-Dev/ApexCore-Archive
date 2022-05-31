package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullConsumer;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public final class MenuEntry<MENU extends AbstractContainerMenu> extends RegistryEntry<MenuType<MENU>> implements MenuConstructor, NonnullSupplier<MenuType<MENU>>
{
	public MenuEntry(AbstractRegistrator<?> registrator, RegistryObject<MenuType<MENU>> delegate)
	{
		super(registrator, delegate);
	}

	public MenuType<MENU> asMenuType()
	{
		return get();
	}

	public MENU create(int windowId, Inventory playerInventory, @Nullable FriendlyByteBuf extraData)
	{
		return asMenuType().create(windowId, playerInventory, extraData);
	}

	// wrapper for lambda method references
	public MENU create(int windowId, Inventory playerInventory, Player player)
	{
		return create(windowId, playerInventory, (FriendlyByteBuf) null);
	}

	public MENU create(int windowId, Inventory playerInventory)
	{
		return create(windowId, playerInventory, (FriendlyByteBuf) null);
	}

	public MenuProvider asNamedProvider(Component titleComponent)
	{
		return new SimpleMenuProvider(this, titleComponent);
	}

	public void open(ServerPlayer player, MenuProvider menuProvider, NonnullConsumer<FriendlyByteBuf> extraData)
	{
		NetworkHooks.openGui(player, menuProvider, extraData);
	}

	public void open(ServerPlayer player, MenuProvider menuProvider)
	{
		open(player, menuProvider, NonnullConsumer.noop());
	}

	public void open(ServerPlayer player, Component titleComponent, NonnullConsumer<FriendlyByteBuf> extraData)
	{
		open(player, asNamedProvider(titleComponent), extraData);
	}

	public void open(ServerPlayer player, Component titleComponent)
	{
		open(player, asNamedProvider(titleComponent), NonnullConsumer.noop());
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return create(windowId, playerInventory, player);
	}

	public static <MENU extends AbstractContainerMenu> MenuEntry<MENU> cast(RegistryEntry<MenuType<MENU>> registryEntry)
	{
		return cast(MenuEntry.class, registryEntry);
	}

	public static <MENU extends AbstractContainerMenu> MenuEntry<MENU> cast(com.tterrag.registrate.util.entry.RegistryEntry<MenuType<MENU>> registryEntry)
	{
		return cast(MenuEntry.class, registryEntry);
	}
}
