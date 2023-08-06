package xyz.apex.minecraft.apexcore.common.core.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.client.renderer.ItemStackRenderer;

import java.util.Map;
import java.util.function.Supplier;

public final class ItemStackRenderHandler
{
    public static final ItemStackRenderHandler INSTANCE = new ItemStackRenderHandler();

    private final Map<Item, ItemStackRenderer> rendererMap = Maps.newHashMap();

    private ItemStackRenderHandler()
    {
    }

    public void register(ItemLike itemLike, Supplier<ItemStackRenderer> factory)
    {
        var item = itemLike.asItem();

        if(rendererMap.put(item, factory.get()) != null)
            throw new IllegalStateException("Attempt to register duplicate ItemStackRenderer! [%s]".formatted(item));
    }

    @Nullable
    public ItemStackRenderer getRenderer(ItemStack stack)
    {
        return getRenderer(stack.getItem());
    }

    @Nullable
    public ItemStackRenderer getRenderer(ItemLike item)
    {
        return rendererMap.get(item.asItem());
    }

    public boolean hasRenderer(ItemStack stack)
    {
        return hasRenderer(stack.getItem());
    }

    public boolean hasRenderer(ItemLike item)
    {
        return rendererMap.containsKey(item.asItem());
    }

    public boolean renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        var renderer = getRenderer(stack);

        if(renderer == null)
            return false;

        renderer.render(stack, displayContext, pose, buffer, packedLight, packedOverlay);
        return true;
    }
}
