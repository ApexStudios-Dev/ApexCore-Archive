package xyz.apex.minecraft.apexcore.forge.hooks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

import java.util.function.Supplier;

public final class ForgeRendererHooks extends ForgePlatformHolder implements RendererHooks
{
    ForgeRendererHooks(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public void registerRenderType(Block block, Supplier<Supplier<RenderType>> renderType)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        ItemBlockRenderTypes.setRenderLayer(block, renderType.get().get());
    }
}
