package xyz.apex.minecraft.apexcore.common.registry.builder;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import xyz.apex.minecraft.apexcore.common.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;

import java.util.function.Supplier;

public final class MenuBuilder<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>, O extends AbstractRegistrar<O>, P> extends AbstractBuilder<MenuType<?>, MenuType<M>, O, P, MenuBuilder<M, S, O, P>>
{
    private final MenuFactory<M> menuFactory;

    public MenuBuilder(O owner, P parent, String registrationName, MenuFactory<M> menuFactory, Supplier<ScreenFactory<M, S>> screenFactorySupplier)
    {
        super(owner, parent, Registries.MENU, registrationName);

        this.menuFactory = menuFactory;

        onRegister(menuType -> EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
            var screenFactory = screenFactorySupplier.get();
            if(screenFactory != null) MenuRegistry.registerScreenFactory(menuType, screenFactory::create);
        }));
    }

    @Override
    protected MenuType<M> createEntry()
    {
        return MenuRegistry.ofExtended((containerId, inventory, data) -> menuFactory.create(safeSupplier.get(), containerId, inventory.player, data));
    }

    @Override
    protected MenuEntry<M> createRegistryEntry(RegistrySupplier<MenuType<M>> delegate)
    {
        return new MenuEntry<>(owner, delegate, registryKey, menuFactory);
    }

    @Override
    public MenuEntry<M> register()
    {
        return (MenuEntry<M>) super.register();
    }

    @FunctionalInterface
    public interface MenuFactory<M extends AbstractContainerMenu>
    {
        M create(MenuType<M> menuType, int containerId, Player player, FriendlyByteBuf data);
    }

    @FunctionalInterface
    public interface ScreenFactory<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>>
    {
        S create(M menu, Inventory inventory, Component displayName);
    }
}
