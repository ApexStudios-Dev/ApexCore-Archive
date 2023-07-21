package xyz.apex.minecraft.apexcore.fabric.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;

@Mixin(PlayerList.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinPlayerList
{
    @Inject(
            method = "placeNewPlayer",
            at = @At("TAIL")
    )
    private void ApexCore$placeNewPlayer(Connection connection, ServerPlayer player, CallbackInfo ci)
    {
        PlayerEvents.LOGGED_IN.post().handle(player);
    }

    @Inject(
            method = "remove",
            at = @At("TAIL")
    )
    private void ApexCore$remove(ServerPlayer player, CallbackInfo ci)
    {
        PlayerEvents.LOGGED_OUT.post().handle(player);
    }
}
