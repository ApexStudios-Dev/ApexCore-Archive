package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

import java.util.function.Supplier;

/**
 * Hooks for registering various renderer elements.
 */
@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface RegisterRendererHooks
{
    /**
     * Set a blocks render type.
     *
     * @param block      Block to set render type for.
     * @param renderType Render type to be set.
     */
    void setBlockRenderType(Supplier<? extends Block> block, Supplier<Supplier<RenderType>> renderType);

    /**
     * Set a fluids render type.
     *
     * @param fluid      Fluid to set render type for.
     * @param renderType Render type to be set.
     */
    void setFluidRenderType(Supplier<? extends Fluid> fluid, Supplier<Supplier<RenderType>> renderType);
}
