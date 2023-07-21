package xyz.apex.minecraft.apexcore.fabric.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.apex.minecraft.apexcore.common.lib.event.types.InputEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ScreenEvents;

@Mixin(Minecraft.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinMinecraft
{
    @Shadow @Nullable public Screen screen;

    @Inject(
            method = "continueAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/BlockHitResult;getDirection()Lnet/minecraft/core/Direction;"
            ),
            allow = 1,
            cancellable = true
    )
    private void ApexCore$continueAttack(boolean leftClick, CallbackInfo ci)
    {
        if(InputEvents.CLICK.post().handle(true, false, false, InteractionHand.MAIN_HAND))
            ci.cancel();
    }

    @Inject(
            method = "startAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"
            ),
            allow = 1,
            cancellable = true
    )
    private void ApexCore$startAttack(CallbackInfoReturnable<Boolean> cir)
    {
        if(InputEvents.CLICK.post().handle(true, false, false, InteractionHand.MAIN_HAND))
            cir.setReturnValue(false);
    }

    @Inject(
            method = "startUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
            ),
            allow = 1,
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void ApexCore$startUseItem(CallbackInfo ci, InteractionHand[] hands, int i, int j, InteractionHand hand)
    {
        if(InputEvents.CLICK.post().handle(false, true, false, hand))
            ci.cancel();
    }

    @Inject(
            method = "pickBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getAbilities()Lnet/minecraft/world/entity/player/Abilities;"
            ),
            allow = 1,
            cancellable = true
    )
    private void ApexCore$pickBlock(CallbackInfo ci)
    {
        if(InputEvents.CLICK.post().handle(false, false, true, InteractionHand.MAIN_HAND))
            ci.cancel();
    }

    @Inject(
            method = "setScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;added()V"
            )
    )
    private void ApexCore$setScreen$opened(@Nullable Screen screen, CallbackInfo ci)
    {
        assert screen != null; // should be non-null due to mixin injection point
        ScreenEvents.OPENED.post().handle(screen);
    }

    @Inject(
            method = "setScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;removed()V"
            )
    )
    private void ApexCore$setScreen$closed(@Nullable Screen screen, CallbackInfo ci)
    {
        assert this.screen != null; // should be non-null due to mixin injection point
        ScreenEvents.CLOSED.post().handle(this.screen);
    }
}
