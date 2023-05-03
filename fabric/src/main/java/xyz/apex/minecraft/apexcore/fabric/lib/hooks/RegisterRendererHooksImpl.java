package xyz.apex.minecraft.apexcore.fabric.lib.hooks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterRendererHooks;

import java.util.function.Supplier;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
final class RegisterRendererHooksImpl implements RegisterRendererHooks
{
    @Override
    public void setBlockRenderType(Supplier<? extends Block> block, Supplier<Supplier<RenderType>> renderType)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType.get().get());
    }

    @Override
    public void setFluidRenderType(Supplier<? extends Fluid> fluid, Supplier<Supplier<RenderType>> renderType)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        BlockRenderLayerMap.INSTANCE.putFluid(fluid.get(), renderType.get().get());
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<T>> blockEntityType, Supplier<BlockEntityRendererProvider<T>> blockEntityRendererProvider)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        BlockEntityRenderers.register(blockEntityType.get(), blockEntityRendererProvider.get());
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<T>> entityType, Supplier<EntityRendererProvider<T>> entityRendererProvider)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        EntityRendererRegistry.register(entityType.get(), entityRendererProvider.get());
    }
}
