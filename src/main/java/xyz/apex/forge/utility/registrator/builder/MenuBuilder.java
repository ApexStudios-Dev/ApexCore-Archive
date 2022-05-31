package xyz.apex.forge.utility.registrator.builder;

import com.tterrag.registrate.builders.BuilderCallback;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.DistExecutor;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.MenuEntry;
import xyz.apex.forge.utility.registrator.factory.MenuFactory;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public final class MenuBuilder<OWNER extends AbstractRegistrator<OWNER>, MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>, PARENT> extends RegistratorBuilder<OWNER, MenuType<?>, MenuType<MENU>, PARENT, MenuBuilder<OWNER, MENU, SCREEN, PARENT>, MenuEntry<MENU>>
{
	private final MenuFactory<MENU> menuFactory;
	@Nullable private final NonnullSupplier<MenuFactory.ScreenFactory<MENU, SCREEN>> screenFactory;

	public MenuBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback, MenuFactory<MENU> menuFactory, @Nullable NonnullSupplier<MenuFactory.ScreenFactory<MENU, SCREEN>> screenFactory)
	{
		super(owner, parent, registryName, callback, MenuType.class, MenuEntry::new, MenuEntry::cast);

		this.menuFactory = menuFactory;
		this.screenFactory = screenFactory;

		onRegister(this::registerScreenFactory);
	}

	private void registerScreenFactory(MenuType<MENU> containerType)
	{
		if(screenFactory != null)
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MenuScreens.register(containerType, screenFactory.get()::create));
	}

	@Override
	protected MenuType<MENU> createEntry()
	{
		var supplier = asSupplier();
		return IForgeContainerType.create((windowId, playerInventory, buffer) -> menuFactory.create(supplier.get(), windowId, playerInventory, buffer));
	}
}
