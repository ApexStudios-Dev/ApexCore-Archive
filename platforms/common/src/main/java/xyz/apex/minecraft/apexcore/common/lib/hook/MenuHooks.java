package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
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
     * @param player Player to open menu for.
     * @param title Title component for opened menu.
     * @param menuConstructor Menu constructor.
     * @param extraData Extra data to be written and sent to client.
     */
    InteractionResult openMenu(Player player, Component title, MenuConstructor menuConstructor, Consumer<FriendlyByteBuf> extraData);

    /**
     * Creates menu provider for given properties.
     * <p>
     * The {@code extraData} param is only used when running on Fabric. Forge has this
     * passed directly into their {@link net.minecraftforge.network.NetworkHooks#openScreen(ServerPlayer, MenuProvider, Consumer)} method.
     *
     * @param title Title component for opened menu.
     * @param menuConstructor Menu constructor.
     * @param extraData Extra data to be written.
     * @return Menu provider for given properties.
     */
    MenuProvider createMenuProvider(Component title, MenuConstructor menuConstructor, @PlatformOnly(PlatformOnly.FABRIC) Consumer<FriendlyByteBuf> extraData);

    /**
     * @return Global instance.
     */
    static MenuHooks get()
    {
        return ApexCore.MENU_HOOKS;
    }
}
