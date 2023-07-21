package xyz.apex.minecraft.apexcore.fabric.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.TickEvents;

@Mixin(LivingEntity.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinLivingEntity
{
    @Inject(
            method = "die",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$die(DamageSource damageSource, CallbackInfo ci)
    {
        var self = (LivingEntity) (Object) this;

        if(EntityEvents.LIVING_DEATH.post().handle(self, damageSource))
            ci.cancel();
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void ApexCore$tick$start(CallbackInfo ci)
    {
        var self = (LivingEntity) (Object) this;
        TickEvents.START_LIVING_ENTITY.post().handle(self);
    }
}
