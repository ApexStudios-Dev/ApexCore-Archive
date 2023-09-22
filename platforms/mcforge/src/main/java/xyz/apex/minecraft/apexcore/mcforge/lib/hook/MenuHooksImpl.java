package xyz.apex.minecraft.apexcore.mcforge.lib.hook;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;

import java.util.function.Consumer;

@ApiStatus.Internal
public final class MenuHooksImpl implements MenuHooks
{
    @Override
    public InteractionResult openMenu(Player player, Component title, MenuConstructor menuConstructor, Consumer<FriendlyByteBuf> extraData)
    {
        if(player instanceof ServerPlayer sPlayer)
            sPlayer.openMenu(createMenuProvider(title, menuConstructor, extraData), extraData);
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    @Override
    public MenuProvider createMenuProvider(Component title, MenuConstructor menuConstructor, Consumer<FriendlyByteBuf> extraData)
    {
        return new SimpleMenuProvider(menuConstructor, title);
    }
}
