package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.PlatformOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.DelegatedRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Main RegistryEntry class for all Menu entries.
 *
 * @param <T> Type of menu.
 */
public final class MenuEntry<T extends AbstractContainerMenu> extends DelegatedRegistryEntry<MenuType<T>> implements MenuConstructor
{
    private final MenuHooks.ClientMenuConstructor<T> clientMenuConstructor;

    private MenuEntry(RegistryEntry<MenuType<T>> delegate, MenuHooks.ClientMenuConstructor<T> clientMenuConstructor)
    {
        super(delegate);

        this.clientMenuConstructor = clientMenuConstructor;
    }

    /**
     * Sends server packet to client to open the screen bound to this menu type.
     *
     * @param player      Player to open screen for.
     * @param displayName Display name for this menu.
     * @param extraData   Extra data to be written before senting packet.
     */
    public void open(ServerPlayer player, Component displayName, Consumer<FriendlyByteBuf> extraData)
    {
        MenuHooks.get().openMenu(player, displayName, clientMenuConstructor, extraData);
    }

    /**
     * Returns this menu type as a menu provider.
     * <p>
     * The {@code extraData} param is only used when running on Fabric. Forge has this
     * passed directly into their {@link net.minecraftforge.network.NetworkHooks#openScreen(ServerPlayer, MenuProvider, Consumer)} method.
     *
     * @param displayName Display name for this menu provider.
     * @param extraData   Extra data to be written when this menu provider is used.
     * @return Menu provider for this menu type.
     */
    public MenuProvider asProvider(Component displayName, @PlatformOnly(PlatformOnly.FABRIC) Consumer<FriendlyByteBuf> extraData)
    {
        return MenuHooks.get().createMenuProvider(displayName, clientMenuConstructor, extraData);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
    {
        return clientMenuConstructor.create(windowId, playerInventory);
    }

    /**
     * Registers menu type & screen factory for given properties.
     *
     * @param registrar              Registrar to register for.
     * @param registrationName       Registration name of menu to register.
     * @param networkMenuConstructor Constructor used when opening menu from networks.
     * @param clientMenuConstructor  Constructor used when opening menu for clients.
     * @param screenFactory          Factory used when opening the menu screen.
     * @param <T>                    Type of menu.
     * @param <S>                    Type of screen.
     * @return Registered menu type.
     */
    public static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> MenuEntry<T> register(Registrar<MenuType<?>> registrar, String registrationName, MenuHooks.NetworkMenuConstructor<T> networkMenuConstructor, MenuHooks.ClientMenuConstructor<T> clientMenuConstructor, Supplier<Supplier<MenuScreens.ScreenConstructor<T, S>>> screenFactory)
    {
        var delegate = registrar.register(registrationName, () -> MenuHooks.get().create(networkMenuConstructor));
        delegate.addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> MenuScreens.register(value, screenFactory.get().get())));
        return new MenuEntry<>(delegate, clientMenuConstructor);
    }

    /**
     * Registers menu type & screen factory for given properties.
     *
     * @param ownerId                Owner ID to register for.
     * @param registrationName       Registration name of menu to register.
     * @param networkMenuConstructor Constructor used when opening menu from networks.
     * @param clientMenuConstructor  Constructor used when opening menu for clients.
     * @param screenFactory          Factory used when opening the menu screen.
     * @param <T>                    Type of menu.
     * @param <S>                    Type of screen.
     * @return Registered menu type.
     */
    public static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> MenuEntry<T> register(String ownerId, String registrationName, MenuHooks.NetworkMenuConstructor<T> networkMenuConstructor, MenuHooks.ClientMenuConstructor<T> clientMenuConstructor, Supplier<Supplier<MenuScreens.ScreenConstructor<T, S>>> screenFactory)
    {
        var registrar = RegistrarManager.get(ownerId).get(Registries.MENU);
        return register(registrar, registrationName, networkMenuConstructor, clientMenuConstructor, screenFactory);
    }
}
