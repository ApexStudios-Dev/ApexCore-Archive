package xyz.apex.forge.apexcore.registrate.holder;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.MenuBuilder;
import xyz.apex.forge.apexcore.registrate.builder.factory.ForgeMenuFactory;
import xyz.apex.forge.apexcore.registrate.builder.factory.MenuFactory;
import xyz.apex.forge.apexcore.registrate.builder.factory.MenuScreenFactory;
import xyz.apex.forge.apexcore.registrate.entry.MenuEntry;

@SuppressWarnings("unchecked")
public interface MenuHolder<OWNER extends CoreRegistrate<OWNER> & MenuHolder<OWNER>>
{
	private OWNER self()
	{
		return (OWNER) this;
	}

	default <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(MenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return menu(self().currentName(), menuFactory.asForgeFactory(), screenFactory);
	}

	default <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(String name, MenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return menu(name, menuFactory.asForgeFactory(), screenFactory);
	}

	default <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(ForgeMenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return menu(self().currentName(), menuFactory, screenFactory);
	}

	default <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(String name, ForgeMenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return self().entry(name, callback -> new MenuBuilder<>(self(), self(), name, callback, menuFactory, screenFactory)).register();
	}
}
