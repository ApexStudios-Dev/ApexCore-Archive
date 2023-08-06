package xyz.apex.minecraft.apexcore.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.core.client.ItemStackRenderHandler;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public abstract class MixinBlockEntityWithoutLevelRenderer
{
    @Inject(
            method = "renderByItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci)
    {
        if(ItemStackRenderHandler.INSTANCE.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay))
            ci.cancel();
    }
}
