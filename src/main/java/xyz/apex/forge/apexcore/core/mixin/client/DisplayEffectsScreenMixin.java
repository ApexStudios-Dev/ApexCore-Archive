package xyz.apex.forge.apexcore.core.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;

import xyz.apex.forge.apexcore.core.client.CreativeScreenHandler;

@Mixin(DisplayEffectsScreen.class)
public abstract class DisplayEffectsScreenMixin
{
	@Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
	private void renderEffects(MatrixStack pose, CallbackInfo ci)
	{
		DisplayEffectsScreen self = (DisplayEffectsScreen) (Object) this;

		if(self instanceof CreativeScreen)
		{
			CreativeScreen screen = (CreativeScreen) self;
			int leftPos = self.getGuiLeft(); // FieldHelper.getPrivateValue(ContainerScreen.class, screen, "leftPos");

			if(CreativeScreenHandler.checkEffectRendering(screen, pose, leftPos))
				ci.cancel();
		}
	}
}
