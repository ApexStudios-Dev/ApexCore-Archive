package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
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
     * @param menuProvider Menu provider.
     * @param extraData Extra data to be written and sent to client.
     */
    void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraData);

    /**
     * Creates menu provider for given properties.
     * <p>
     * The {@code extraData} param is only used when running on Fabric. Forge has this
     * passed directly into their {@link net.minecraftforge.network.NetworkHooks#openScreen(ServerPlayer, MenuProvider, Consumer)} method.
     *
     * @param menuProvider Menu provider.
     * @param extraData Extra data to be written.
     * @return Menu provider for given properties.
     */
    MenuProvider createMenuProvider(MenuProvider menuProvider, @PlatformOnly(PlatformOnly.FABRIC) Consumer<FriendlyByteBuf> extraData);

    /**
     * @return Global instance.
     */
    static MenuHooks get()
    {
        return ApexCore.MENU_HOOKS;
    }
}
