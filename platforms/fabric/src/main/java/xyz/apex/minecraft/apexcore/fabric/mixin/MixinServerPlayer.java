package xyz.apex.minecraft.apexcore.fabric.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;

@Mixin(Player.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinServerPlayer
{
    @Inject(
            method = "die",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$die(DamageSource damageSource, CallbackInfo ci)
    {
        var self = (ServerPlayer) (Object) this;

        if(PlayerEvents.DEATH.post().handle(self, damageSource))
            ci.cancel();
    }
}
