package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

    /**
     * Registers renderer for given block entity type.
     *
     * @param blockEntityType             Block entity type to register renderer for.
     * @param blockEntityRendererProvider Block entity renderer to register.
     * @param <T>                         Block entity type.
     */
    <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<T>> blockEntityType, Supplier<BlockEntityRendererProvider<T>> blockEntityRendererProvider);

    /**
     * Registers renderer for given entity type.
     *
     * @param entityType             Entity type to register renderer for.
     * @param entityRendererProvider Entity renderer to register.
     * @param <T>                    Entity type.
     */
    <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<T>> entityType, Supplier<EntityRendererProvider<T>> entityRendererProvider);
}
