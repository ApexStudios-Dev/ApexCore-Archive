package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;
import xyz.apex.minecraft.apexcore.common.platform.Side;
import xyz.apex.minecraft.apexcore.common.platform.SideOnly;

import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public interface RendererHooks extends PlatformHolder
{
    void registerRenderType(Block block, Supplier<Supplier<RenderType>> renderType);

    static RendererHooks getInstance()
    {
        return Hooks.getInstance().renderer();
    }
}
