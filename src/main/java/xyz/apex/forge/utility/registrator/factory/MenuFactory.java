package xyz.apex.forge.utility.registrator.factory;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@SuppressWarnings("unused")
@FunctionalInterface
public interface MenuFactory<MENU extends AbstractContainerMenu>
{
	MENU create(MenuType<MENU> containerType, int windowId, Inventory playerInventory, FriendlyByteBuf buffer);

	static <MENU extends AbstractContainerMenu> MenuFactory<MENU> fromVanilla(VanillaFactory<MENU> menuFactory)
	{
		return (menuType, windowId, playerInventory, buffer) -> menuFactory.create(menuType, windowId, playerInventory);
	}

	static <MENU extends AbstractContainerMenu> MenuFactory<MENU> fromVanilla(MenuType.MenuSupplier<MENU> factory)
	{
		return fromVanilla(VanillaFactory.fromVanilla(factory));
	}

	@FunctionalInterface
	interface VanillaFactory<MENU extends AbstractContainerMenu>
	{
		MENU create(MenuType<MENU> containerType, int windowId, Inventory playerInventory);

		static <MENU extends AbstractContainerMenu> VanillaFactory<MENU> fromVanilla(MenuType.MenuSupplier<MENU> factory)
		{
			return (menuType, windowId, playerInventory) -> factory.create(windowId, playerInventory);
		}
	}

	@FunctionalInterface
	interface ScreenFactory<MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>>
	{
		SCREEN create(MENU menu, Inventory playerInventory, Component titleComponent);
	}
}
