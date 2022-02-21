package xyz.apex.forge.apexcore.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import xyz.apex.forge.apexcore.core.client.CreativeModeInventoryScreenHandler;

@Mixin(CreativeModeInventoryScreen.class)
@OnlyIn(Dist.CLIENT)
public abstract class CreativeModeInventoryScreenMixin
{
	@Shadow private float scrollOffs;
	@Shadow(remap = false) private static int tabPage;

	private final CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) (Object) this;

	@Inject(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;fillItemList(Lnet/minecraft/core/NonNullList;)V", shift = At.Shift.AFTER))
	private void selectTab_FilterItems(CreativeModeTab itemGroup, CallbackInfo ci)
	{
		CreativeModeInventoryScreenHandler.selectTab_FilterItems(self, itemGroup);
	}

	@Inject(method = "selectTab", at = @At("HEAD"))
	private void selectTab_Head(CreativeModeTab itemGroup, CallbackInfo ci)
	{
		CreativeModeInventoryScreenHandler.selectTab_Head(self, itemGroup);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", shift = At.Shift.BEFORE))
	private void render(PoseStack pose, int mouseX, int mouseY, float partialTicks, CallbackInfo ci)
	{
		CreativeModeInventoryScreenHandler.render(self, pose);
	}

	@Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;renderTabButton(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/CreativeModeTab;)V", shift = At.Shift.AFTER, ordinal = 3))
	private void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY, CallbackInfo ci)
	{
		CreativeModeInventoryScreenHandler.renderBg(self, pose);
	}

	@Inject(method = "mouseClicked", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;topPos:I", shift = At.Shift.AFTER), cancellable = true)
	private void mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
	{
		CreativeModeInventoryScreenHandler.mouseClicked(self, mouseX, mouseY, mouseButton, cir);
	}

	@Inject(method = "mouseReleased", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;scrolling:Z", shift = At.Shift.AFTER), cancellable = true)
	private void mouseReleased(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
	{
		CreativeModeInventoryScreenHandler.mouseReleased(self, mouseX, mouseY, mouseButton, newScrollOffs -> scrollOffs = newScrollOffs, cir);
	}

	@Inject(method = "checkTabHovering", at = @At(value = "HEAD"), cancellable = true)
	private void checkTabHovering(PoseStack pose, CreativeModeTab itemGroup, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir)
	{
		CreativeModeInventoryScreenHandler.checkTabHovering(self, pose, itemGroup, mouseX, mouseY, cir);
	}

	@Inject(method = "containerTick", at = @At("TAIL"))
	private void containerTick(CallbackInfo ci)
	{
		CreativeModeInventoryScreenHandler.tick(self, tabPage);
	}
}
