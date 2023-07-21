package xyz.apex.minecraft.apexcore.fabric.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.TickEvents;

@Mixin(Player.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinPlayer
{
    @Inject(
            method = "die",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$die(DamageSource damageSource, CallbackInfo ci)
    {
        var self = (Player) (Object) this;

        if(PlayerEvents.DEATH.post().handle(self, damageSource))
            ci.cancel();
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void ApexCore$tick$start(CallbackInfo ci)
    {
        var self = (Player) (Object) this;
        TickEvents.START_PLAYER.post().handle(self);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void ApexCore$tick$end(CallbackInfo ci)
    {
        var self = (Player) (Object) this;
        TickEvents.END_PLAYER.post().handle(self);
    }
}
