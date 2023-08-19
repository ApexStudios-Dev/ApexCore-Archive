package xyz.apex.minecraft.testmod.common.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.client.renderer.ItemStackRenderer;

@SideOnly(PhysicalSide.CLIENT)
public final class TestItemStackRenderer implements ItemStackRenderer
{
    @Override
    public void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        var renderer = Minecraft.getInstance().getItemRenderer();
        var model = renderer.getItemModelShaper().getItemModel(Items.BARRIER);
        model = model == null ? renderer.getItemModelShaper().getModelManager().getMissingModel() : model;
        renderer.render(stack, displayContext, false, pose, buffer, packedLight, packedOverlay, model);
    }
}
