package xyz.apex.forge.apexcore.core.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import xyz.apex.forge.apexcore.core.client.CreativeScreenHandler;

@Mixin(CreativeScreen.class)
@OnlyIn(Dist.CLIENT)
public abstract class CreativeScreenMixin
{
	@Shadow private float scrollOffs;
	@Shadow(remap = false) private static int tabPage;

	private final CreativeScreen self = (CreativeScreen) (Object) this;

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DisplayEffectsScreen;init()V", shift = At.Shift.AFTER))
	private void init(CallbackInfo ci)
	{
		CreativeScreenHandler.init(self);
	}

	@Inject(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;fillItemList(Lnet/minecraft/util/NonNullList;)V", shift = At.Shift.AFTER))
	private void selectTab_FilterItems(ItemGroup itemGroup, CallbackInfo ci)
	{
		CreativeScreenHandler.selectTab_FilterItems(self, itemGroup);
	}

	@Inject(method = "selectTab", at = @At("HEAD"))
	private void selectTab_Head(ItemGroup itemGroup, CallbackInfo ci)
	{
		CreativeScreenHandler.selectTab_Head(self, itemGroup);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V", shift = At.Shift.BEFORE))
	private void render(MatrixStack pose, int mouseX, int mouseY, float partialTicks, CallbackInfo ci)
	{
		CreativeScreenHandler.render(self, pose);
	}

	@Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/inventory/CreativeScreen;renderTabButton(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/item/ItemGroup;)V", shift = At.Shift.AFTER, ordinal = 3))
	private void renderBg(MatrixStack pose, float partialTick, int mouseX, int mouseY, CallbackInfo ci)
	{
		CreativeScreenHandler.renderBg(self, pose);
	}

	@Inject(method = "mouseClicked", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/inventory/CreativeScreen;topPos:I", shift = At.Shift.AFTER), cancellable = true)
	private void mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
	{
		CreativeScreenHandler.mouseClicked(self, mouseX, mouseY, mouseButton, cir);
	}

	@Inject(method = "mouseReleased", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/inventory/CreativeScreen;scrolling:Z", shift = At.Shift.AFTER), cancellable = true)
	private void mouseReleased(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
	{
		CreativeScreenHandler.mouseReleased(self, mouseX, mouseY, mouseButton, newScrollOffs -> scrollOffs = newScrollOffs, cir);
	}

	@Inject(method = "checkTabHovering", at = @At(value = "HEAD"), cancellable = true)
	private void checkTabHovering(MatrixStack pose, ItemGroup itemGroup, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir)
	{
		CreativeScreenHandler.checkTabHovering(self, pose, itemGroup, mouseX, mouseY, cir);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo ci)
	{
		CreativeScreenHandler.tick(self, tabPage);
	}
}
