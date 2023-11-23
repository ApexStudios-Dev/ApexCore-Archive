package dev.apexstudios.apexcore.neoforge.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.RegistrationHelper;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DispenseItemBehavior;
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
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;

final class NeoForgeRegistryHelper implements RegistryHelper
{
    private static final Map<String, NeoForgeRegistryHelper> INSTANCES = Maps.newConcurrentMap();

    private final String ownerId;

    private NeoForgeRegistryHelper(String ownerId)
    {
        this.ownerId = ownerId;
    }

    @Override
    public String ownerId()
    {
        return ownerId;
    }

    @Override
    public void register(AbstractRegister<?> register)
    {
        ModEvents.addListener(ownerId, RegisterEvent.class, event -> register.onRegister(event.getRegistryKey(), new RegistrationHelper()
        {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> valueKey, T value)
            {
                event.register(registryType, forgeHelper -> forgeHelper.register(valueKey, value));
            }
        }));

        ModEvents.addListener(ownerId, EventPriority.LOW, RegisterEvent.class, event -> register.onRegisterLate(event.getRegistryKey()));
    }

    @Override
    public void registerColorHandler(ItemLike item, OptionalLike<OptionalLike<ItemColor>> colorHandler)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> colorHandler.ifPresent(c -> c.ifPresent(handler -> ModEvents.addListener(ownerId, RegisterColorHandlersEvent.Item.class, event -> event.register(handler, item)))));
    }

    @Override
    public void registerColorHandler(Block block, OptionalLike<OptionalLike<BlockColor>> colorHandler)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> colorHandler.ifPresent(c -> c.ifPresent(handler -> ModEvents.addListener(ownerId, RegisterColorHandlersEvent.Block.class, event -> event.register(handler, block)))));
    }

    @Override
    public void registerRenderType(Block block, OptionalLike<OptionalLike<RenderType>> renderType)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> renderType.ifPresent(r -> r.ifPresent(rt -> ModEvents.addListener(ownerId, FMLClientSetupEvent.class, event -> ItemBlockRenderTypes.setRenderLayer(block, rt)))));
    }

    @Override
    public void registerRenderType(Fluid fluid, OptionalLike<OptionalLike<RenderType>> renderType)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> renderType.ifPresent(r -> r.ifPresent(rt -> ModEvents.addListener(ownerId, FMLClientSetupEvent.class, event -> ItemBlockRenderTypes.setRenderLayer(fluid, rt)))));
    }

    @Override
    public <T extends Entity> void registerEntityAttributes(EntityType<T> entityType, OptionalLike<AttributeSupplier.Builder> attributes)
    {
        attributes.ifPresent(builder -> ModEvents.addListener(ownerId, EntityAttributeCreationEvent.class, event -> event.put((EntityType<? extends LivingEntity>) entityType, builder.build())));
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, OptionalLike<OptionalLike<EntityRendererProvider<T>>> rendererProvider)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> rendererProvider.ifPresent(r -> r.ifPresent(provider -> ModEvents.addListener(ownerId, EntityRenderersEvent.RegisterRenderers.class, event -> event.registerEntityRenderer(entityType, provider)))));
    }

    @Override
    public void registerItemDispenseBehavior(ItemLike item, OptionalLike<DispenseItemBehavior> dispenseBehavior)
    {
        dispenseBehavior.ifPresent(behavior -> ModEvents.addListener(ownerId, FMLCommonSetupEvent.class, event -> event.enqueueWork(() -> DispenserBlock.registerBehavior(item, behavior))));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, OptionalLike<OptionalLike<BlockEntityRendererProvider<T>>> rendererProvider)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> rendererProvider.ifPresent(r -> r.ifPresent(provider -> ModEvents.addListener(ownerId, EntityRenderersEvent.RegisterRenderers.class, event -> event.registerBlockEntityRenderer(blockEntityType, provider)))));
    }

    @Override
    public void registerCreativeModeTabItemGenerator(ResourceKey<CreativeModeTab> creativeModeTab, CreativeModeTab.DisplayItemsGenerator generator)
    {
        ModEvents.addListener(ownerId, BuildCreativeModeTabContentsEvent.class, event -> {
            if(event.getTabKey() == creativeModeTab)
                generator.accept(event.getParameters(), event);
        });
    }

    static RegistryHelper get(String ownerId)
    {
        return INSTANCES.computeIfAbsent(ownerId, NeoForgeRegistryHelper::new);
    }
}
