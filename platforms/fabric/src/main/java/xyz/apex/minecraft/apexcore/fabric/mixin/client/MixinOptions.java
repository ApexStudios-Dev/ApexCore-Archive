package xyz.apex.minecraft.apexcore.fabric.mixin.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Options;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.InputEvents;

@Mixin(Options.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinOptions
{
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Options;load()V"
            )
    )
    private void ApexCore$aiStep(CallbackInfo ci)
    {
        InputEvents.REGISTER_KEY_MAPPING.post().handle(KeyBindingHelper::registerKeyBinding);
    }
}
