package xyz.apex.forge.apexcore.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;

import xyz.apex.forge.apexcore.core.client.CreativeScreenHandler;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class DisplayEffectsScreenMixin
{
	@Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
	private void renderEffects(PoseStack pose, CallbackInfo ci)
	{
		var self = (EffectRenderingInventoryScreen) (Object) this;

		if(self instanceof CreativeModeInventoryScreen screen)
		{
			int leftPos = self.getGuiLeft(); // FieldHelper.getPrivateValue(ContainerScreen.class, screen, "leftPos");

			if(CreativeScreenHandler.checkEffectRendering(screen, pose, leftPos))
				ci.cancel();
		}
	}
}
