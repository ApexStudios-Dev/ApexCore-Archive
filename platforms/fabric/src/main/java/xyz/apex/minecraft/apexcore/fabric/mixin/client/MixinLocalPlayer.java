package xyz.apex.minecraft.apexcore.fabric.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ClientEvents;

@Mixin(LocalPlayer.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinLocalPlayer
{
    @Inject(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getTutorial()Lnet/minecraft/client/tutorial/Tutorial;"
            )
    )
    private void ApexCore$aiStep(CallbackInfo ci)
    {
        var self = (LocalPlayer) (Object) this;
        ClientEvents.PLAYER_INPUT.post().handle(self, self.input);
    }
}
