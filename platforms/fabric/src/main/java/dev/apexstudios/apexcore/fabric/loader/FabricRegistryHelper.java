package dev.apexstudios.apexcore.fabric.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.inventory.BlockEntityItemHandlerProvider;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.RegistrationHelper;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;

final class FabricRegistryHelper implements RegistryHelper, RegistrationHelper
{
    private static final Map<String, FabricRegistryHelper> INSTANCES = Maps.newConcurrentMap();

    private final String ownerId;

    private FabricRegistryHelper(String ownerId)
    {
        this.ownerId = ownerId;

        ItemStorage.SIDED.registerFallback((level, pos, blockState, blockEntity, side) ->
        {
            if(blockEntity instanceof BlockEntityItemHandlerProvider provider)
            {
                var itemHandler = provider.getItemHandler(side);

                if(itemHandler != null)
                    return new FabricItemHandler(itemHandler);
            }

            return null;
        });
    }

    @Override
    public String ownerId()
    {
        return ownerId;
    }

    @Override
    public void register(AbstractRegister<?> register)
    {
        BuiltInRegistries.REGISTRY.forEach(registry -> register.onRegister(registry.key(), this));
        BuiltInRegistries.REGISTRY.forEach(registry -> register.onRegisterLate(registry.key()));
    }

    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> valueKey, T value)
    {
        var registry = BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryType);
        Registry.registerForHolder(registry, valueKey, value);
    }

    @Override
    public void registerColorHandler(ItemLike item, OptionalLike<OptionalLike<ItemColor>> colorHandler)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> colorHandler.ifPresent(c -> c.ifPresent(handler -> ColorProviderRegistry.ITEM.register(handler, item))));
    }

    @Override
    public void registerColorHandler(Block block, OptionalLike<OptionalLike<BlockColor>> colorHandler)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> colorHandler.ifPresent(c -> c.ifPresent(handler -> ColorProviderRegistry.BLOCK.register(handler, block))));
    }

    @Override
    public void registerRenderType(Block block, OptionalLike<OptionalLike<RenderType>> renderType)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> renderType.ifPresent(r -> r.ifPresent(rt -> BlockRenderLayerMap.INSTANCE.putBlock(block, rt))));
    }

    @Override
    public void registerRenderType(Fluid fluid, OptionalLike<OptionalLike<RenderType>> renderType)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> renderType.ifPresent(r -> r.ifPresent(rt -> BlockRenderLayerMap.INSTANCE.putFluid(fluid, rt))));
    }

    @Override
    public <T extends Entity> void registerEntityAttributes(EntityType<T> entityType, OptionalLike<AttributeSupplier.Builder> attributes)
    {
        attributes.ifPresent(builder -> FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) entityType, builder));
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, OptionalLike<OptionalLike<EntityRendererProvider<T>>> rendererProvider)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> rendererProvider.ifPresent(r -> r.ifPresent(provider -> EntityRendererRegistry.register(entityType, provider))));
    }

    @Override
    public void registerItemDispenseBehavior(ItemLike item, OptionalLike<DispenseItemBehavior> dispenseBehavior)
    {
        dispenseBehavior.ifPresent(behavior -> DispenserBlock.registerBehavior(item, behavior));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, OptionalLike<OptionalLike<BlockEntityRendererProvider<T>>> rendererProvider)
    {
        rendererProvider.ifPresent(r -> r.ifPresent(provider -> BlockEntityRenderers.register(blockEntityType, provider)));
    }

    @Override
    public void registerCreativeModeTabItemGenerator(ResourceKey<CreativeModeTab> creativeModeTab, CreativeModeTab.DisplayItemsGenerator generator)
    {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(entries -> generator.accept(entries.getContext(), entries));
    }

    static RegistryHelper get(String ownerId)
    {
        return INSTANCES.computeIfAbsent(ownerId, FabricRegistryHelper::new);
    }
}
