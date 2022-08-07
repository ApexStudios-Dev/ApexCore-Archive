package xyz.apex.forge.apexcore.registrate.builder.factory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@FunctionalInterface
public interface MenuFactory<MENU extends AbstractContainerMenu>
{
	MENU create(MenuType<MENU> menuType, int windowId, Inventory inventory);

	default ForgeMenuFactory<MENU> asForgeFactory()
	{
		return (menuType, windowId, inventory, buffer) -> create(menuType, windowId, inventory);
	}
}