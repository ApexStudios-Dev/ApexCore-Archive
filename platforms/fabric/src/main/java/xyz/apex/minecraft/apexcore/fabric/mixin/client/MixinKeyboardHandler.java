package xyz.apex.minecraft.apexcore.fabric.mixin.client;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.InputEvents;

@Mixin(KeyboardHandler.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinKeyboardHandler
{
    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "keyPress",
            at = @At("TAIL")
    )
    private void ApexCore$keyPress(long windowPointer, int keyCode, int scanCode, int action, int modifiers, CallbackInfo ci)
    {
        if(windowPointer == minecraft.getWindow().getWindow())
            InputEvents.KEY.post().handle(keyCode, scanCode, action, modifiers);
    }
}
