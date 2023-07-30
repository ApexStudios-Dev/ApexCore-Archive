package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PlatformOnly;

import java.util.function.Consumer;

/**
 * Hooks for various menu related things.
 */
@ApiStatus.NonExtendable
public interface MenuHooks
{
    /**
     * Tells client to open screen bound to the given menu constructor.
     *
     * @param player                Player to open menu for.
     * @param displayName           Display name for this menu.
     * @param clientMenuConstructor Client menu constructor.
     * @param extraData             Extra data to be written and sent to client.
     * @param <T>                   Type of menu.
     */
    <T extends AbstractContainerMenu> void openMenu(ServerPlayer player, Component displayName, ClientMenuConstructor<T> clientMenuConstructor, Consumer<FriendlyByteBuf> extraData);

    /**
     * Internal method used to construct correct menu type for current platform.
     * <p>
     * Should not be used, use {@link MenuEntry} instead.
     *
     * @param networkMenuConstructor Constructor for opening menu from networks.
     * @param <T>                    Type of menu.
     * @return New menu type instance.
     */
    @ApiStatus.Internal
    <T extends AbstractContainerMenu> MenuType<T> create(NetworkMenuConstructor<T> networkMenuConstructor);

    /**
     * Creates menu provider for given properties.
     * <p>
     * The {@code extraData} param is only used when running on Fabric. Forge has this
     * passed directly into their {@link net.minecraftforge.network.NetworkHooks#openScreen(ServerPlayer, MenuProvider, Consumer)} method.
     *
     * @param displayName           Display name for this menu.
     * @param clientMenuConstructor Constructor used to open menu.
     * @param extraData             Extra data to be written.
     * @param <T>                   Type of menu.
     * @return Menu provider for given properties.
     */
    <T extends AbstractContainerMenu> MenuProvider createMenuProvider(Component displayName, ClientMenuConstructor<T> clientMenuConstructor, @PlatformOnly(PlatformOnly.FABRIC) Consumer<FriendlyByteBuf> extraData);

    /**
     * @return Global instance.
     */
    static MenuHooks get()
    {
        return ApexCore.MENU_HOOKS;
    }

    /**
     * Constructor for opening menus from networks.
     *
     * @param <T> Type of menu.
     */
    @FunctionalInterface
    interface NetworkMenuConstructor<T extends AbstractContainerMenu>
    {
        /**
         * Returns new menu instance for given properties.
         *
         * @param windowId        Internal window id for this menu.
         * @param playerInventory Player inventory.
         * @param extraData       Extra data sent from server.
         * @return New menu instance.
         */
        T create(int windowId, Inventory playerInventory, FriendlyByteBuf extraData);
    }

    /**
     * Constructor for opening menus for clients.
     *
     * @param <T> Type of menu.
     */
    @FunctionalInterface
    interface ClientMenuConstructor<T extends AbstractContainerMenu>
    {
        /**
         * Returns new menu instance for given properties.
         *
         * @param windowId        Internal window id for this menu.
         * @param playerInventory Player inventory.
         * @return New menu instance.
         */
        T create(int windowId, Inventory playerInventory);
    }
}
