package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;
import xyz.apex.minecraft.apexcore.common.platform.Side;
import xyz.apex.minecraft.apexcore.common.platform.SideOnly;

import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public interface RendererHooks extends PlatformHolder
{
    void registerRenderType(Block block, Supplier<Supplier<RenderType>> renderType);

    <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, Supplier<EntityRendererProvider<T>> entityRendererProvider);

    <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, Supplier<BlockEntityRendererProvider<T>> blockEntityRendererProvider);

    void registerItemColor(Supplier<ItemColor> itemColor, Supplier<? extends ItemLike>... items);

    void registerBlockColor(Supplier<BlockColor> blockColor, Supplier<? extends Block>... blocks);

    static RendererHooks getInstance()
    {
        return Hooks.getInstance().renderer();
    }
}
