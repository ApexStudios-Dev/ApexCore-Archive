package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.MenuEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.MenuFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.ScreenFactory;

import java.util.function.Supplier;

/**
 * MenuType Builder implementation.
 * <p>
 * Used to build and register MenuType entries.
 *
 * @param <O> Type of Registrar.
 * @param <M> Type of Menu [Entry].
 * @param <S> Type of Screen.
 * @param <P> Type of Parent.
 */
public final class MenuBuilder<O extends AbstractRegistrar<O>, M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>, P> extends AbstractBuilder<O, P, MenuType<?>, MenuType<M>, MenuBuilder<O, M, S, P>, MenuEntry<M>>
{
    private final MenuFactory<M> menuFactory;
    private final Supplier<Supplier<ScreenFactory<M, S>>> screenFactory;

    @ApiStatus.Internal
    public MenuBuilder(O registrar, P parent, String registrationName, MenuFactory<M> menuFactory, Supplier<Supplier<ScreenFactory<M, S>>> screenFactory)
    {
        super(registrar, parent, Registries.MENU, registrationName);

        this.menuFactory = menuFactory;
        this.screenFactory = screenFactory;
    }

    @Override
    protected void onRegister(MenuType<M> entry)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> MenuScreens.register(entry, screenFactory.get().get()));
    }

    @Override
    protected MenuEntry<M> createRegistryEntry()
    {
        return new MenuEntry<>(registrar, registryKey);
    }

    @Override
    protected MenuType<M> createEntry()
    {
        return ApexCore.INSTANCE.createMenuType(menuFactory, asSupplier());
    }

    @Override
    protected String getDescriptionId(MenuEntry<M> entry)
    {
        return registryName().toLanguageKey("menu_type");
    }
}
