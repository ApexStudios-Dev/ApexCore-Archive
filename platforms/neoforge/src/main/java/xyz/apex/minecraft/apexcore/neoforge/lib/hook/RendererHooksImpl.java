package xyz.apex.minecraft.apexcore.neoforge.lib.hook;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
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
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;
import xyz.apex.minecraft.apexcore.neoforge.core.ModEvents;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
public final class RendererHooksImpl implements RendererHooks
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

    @Override
    public void registerModelLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> layerDefinition)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ModEvents.active().addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, event -> event.registerLayerDefinition(layerLocation, layerDefinition));
    }
}
