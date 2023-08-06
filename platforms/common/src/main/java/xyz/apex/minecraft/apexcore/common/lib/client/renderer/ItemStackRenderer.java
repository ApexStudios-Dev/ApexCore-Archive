package xyz.apex.minecraft.apexcore.common.lib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import xyz.apex.minecraft.apexcore.common.core.client.ItemStackRenderHandler;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

import java.util.function.Supplier;

@SideOnly(PhysicalSide.CLIENT)
public interface ItemStackRenderer
{
    void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay);

    static void register(ItemLike item, Supplier<ItemStackRenderer> factory)
    {
        ItemStackRenderHandler.INSTANCE.register(item, factory);
    }
}
