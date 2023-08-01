package xyz.apex.minecraft.apexcore.neoforge.lib.hook;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;

import java.util.function.Consumer;

@ApiStatus.Internal
public final class MenuHooksImpl implements MenuHooks
{
    @Override
    public void openMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraData)
    {
        NetworkHooks.openScreen(player, menuProvider, extraData);
    }

    @Override
    public MenuProvider createMenuProvider(MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraData)
    {
        return menuProvider;
    }
}
