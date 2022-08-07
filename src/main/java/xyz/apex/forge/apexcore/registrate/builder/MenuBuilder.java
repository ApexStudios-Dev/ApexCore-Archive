package xyz.apex.forge.apexcore.registrate.builder;

import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.factory.ForgeMenuFactory;
import xyz.apex.forge.apexcore.registrate.builder.factory.MenuScreenFactory;
import xyz.apex.forge.apexcore.registrate.entry.MenuEntry;
import xyz.apex.forge.apexcore.registrate.holder.MenuHolder;

public final class MenuBuilder<
		OWNER extends CoreRegistrate<OWNER> & MenuHolder<OWNER>,
		MENU extends AbstractContainerMenu,
		SCREEN extends Screen & MenuAccess<MENU>,
		PARENT
> extends AbstractBuilder<OWNER, MenuType<?>, MenuType<MENU>, PARENT, MenuBuilder<OWNER, MENU, SCREEN, PARENT>, MenuEntry<MENU>>
{
	private final ForgeMenuFactory<MENU> menuFactory;

	public MenuBuilder(OWNER owner, PARENT parent, String name, BuilderCallback callback, ForgeMenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		super(owner, parent, name, callback, ForgeRegistries.Keys.MENU_TYPES, MenuEntry::new, MenuEntry::cast);

		this.menuFactory = menuFactory;

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> onRegister(menuType -> OneTimeEventReceiver.addModListener(FMLClientSetupEvent.class, event -> event.enqueueWork(() -> {
			var factory = screenFactory.get();
			MenuScreens.register(menuType, factory::create);
		}))));
	}

	@Override
	protected MenuType<MENU> createEntry()
	{
		return IForgeMenuType.create((windowId, inventory, buffer) -> menuFactory.create(asSupplier().get(), windowId, inventory, buffer));
	}
}