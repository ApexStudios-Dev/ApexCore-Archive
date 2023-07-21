package xyz.apex.minecraft.apexcore.fabric.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ScreenEvents;

@Mixin(Minecraft.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinMinecraft
{
    @Shadow @Nullable public Screen screen;

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
