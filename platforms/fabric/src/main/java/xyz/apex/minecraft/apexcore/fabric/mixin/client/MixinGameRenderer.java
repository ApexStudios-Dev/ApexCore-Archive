package xyz.apex.minecraft.apexcore.fabric.mixin.client;

import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.TickEvents;

@Mixin(GameRenderer.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinGameRenderer
{
    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void ApexCore$render$start(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci)
    {
        TickEvents.START_RENDER.post().handle(partialTicks);
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void ApexCore$render$emd(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci)
    {
        TickEvents.END_RENDER.post().handle(partialTicks);
    }
}
