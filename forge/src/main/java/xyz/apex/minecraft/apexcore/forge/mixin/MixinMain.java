package xyz.apex.minecraft.apexcore.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.data.Main;

import xyz.apex.minecraft.apexcore.common.platform.GamePlatform;

@Mixin(Main.class)
public abstract class MixinMain
{

    @Inject(
            method = "main",
            at = @At("TAIL"),
            remap = false
    )
    private static void main(String[] args, CallbackInfo ci)
    {
        if(!GamePlatform.INSTANCE.isDevelopmentEnvironment()) return;
        if(!GamePlatform.INSTANCE.isRunningDataGeneration()) return;
        System.exit(0);
    }
}
