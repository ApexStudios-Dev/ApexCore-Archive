package xyz.apex.minecraft.apexcore.fabric.hooks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

import java.util.function.Supplier;
import java.util.stream.Stream;

public final class FabricRendererHooks extends FabricPlatformHolder implements RendererHooks
{
    FabricRendererHooks(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public void registerRenderType(Block block, Supplier<Supplier<RenderType>> renderType)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        BlockRenderLayerMap.INSTANCE.putBlocks(renderType.get().get(), block);
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, Supplier<EntityRendererProvider<T>> entityRendererProvider)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        EntityRendererRegistry.register(entityType, entityRendererProvider.get());
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, Supplier<BlockEntityRendererProvider<T>> blockEntityRendererProvider)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        BlockEntityRenderers.register(blockEntityType, blockEntityRendererProvider.get());
    }

    @Override
    public void registerItemColor(Supplier<ItemColor> itemColor, Supplier<? extends ItemLike>... items)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        var resolved = Stream.of(items).map(Supplier::get).toArray(ItemLike[]::new);
        ColorProviderRegistry.ITEM.register(itemColor.get(), resolved);
    }

    @Override
    public void registerBlockColor(Supplier<BlockColor> blockColor, Supplier<? extends Block>... blocks)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        var resolved = Stream.of(blocks).map(Supplier::get).toArray(Block[]::new);
        ColorProviderRegistry.BLOCK.register(blockColor.get(), resolved);
    }
}
