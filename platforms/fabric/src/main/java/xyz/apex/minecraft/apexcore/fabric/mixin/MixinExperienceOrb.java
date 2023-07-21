package xyz.apex.minecraft.apexcore.fabric.mixin;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;

@Mixin(ExperienceOrb.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinExperienceOrb
{
    @Inject(
            method = "playerTouch",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$playerTouch(Player player, CallbackInfo ci)
    {
        var self = (ExperienceOrb) (Object) this;

        if(!self.level().isClientSide && player.takeXpDelay == 0)
        {
            if(PlayerEvents.PICKUP_EXPERIENCE.post().handle(player, self))
                ci.cancel();
        }
    }
}
