package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.platform.Side;
import xyz.apex.minecraft.apexcore.common.platform.SideExecutor;

import java.util.function.Supplier;

public interface RenderTypeHooks
{
    static void registerRenderType(Block block, Supplier<Supplier<RenderType>> renderType)
    {
        SideExecutor.runWhenOn(Side.CLIENT, () -> () -> Platform.INSTANCE.internals().registerRenderType(block, renderType.get().get()));
    }
}
