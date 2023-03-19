package xyz.apex.minecraft.apexcore.fabric.hooks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

import java.util.function.Supplier;

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
}
