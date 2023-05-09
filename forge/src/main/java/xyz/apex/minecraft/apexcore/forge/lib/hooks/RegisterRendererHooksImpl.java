package xyz.apex.minecraft.apexcore.forge.lib.hooks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        ModEvents.active().addListener(FMLClientSetupEvent.class, event -> ItemBlockRenderTypes.setRenderLayer(block.get(), renderType.get().get()));
    }

    @Override
    public void setFluidRenderType(Supplier<? extends Fluid> fluid, Supplier<Supplier<RenderType>> renderType)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ModEvents.active().addListener(FMLClientSetupEvent.class, event -> ItemBlockRenderTypes.setRenderLayer(fluid.get(), renderType.get().get()));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<? extends BlockEntityType<T>> blockEntityType, Supplier<Supplier<BlockEntityRendererProvider<T>>> blockEntityRendererProvider)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ModEvents.active().addListener(EntityRenderersEvent.RegisterRenderers.class, event -> event.registerBlockEntityRenderer(blockEntityType.get(), blockEntityRendererProvider.get().get()));
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<T>> entityType, Supplier<Supplier<EntityRendererProvider<T>>> entityRendererProvider)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ModEvents.active().addListener(EntityRenderersEvent.RegisterRenderers.class, event -> event.registerEntityRenderer(entityType.get(), entityRendererProvider.get().get()));
    }
}
