package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

final class ForgePlatformModEvents extends ForgePlatformHolder
{
    private final Map<String, ModEvents> modBuses = Maps.newHashMap();

    ForgePlatformModEvents(ForgePlatform platform)
    {
        super(platform);
    }

    @OnlyIn(Dist.CLIENT)
    void registerRenderType(String modId, Block block, Supplier<Supplier<RenderType>> renderTypeSupplier)
    {
        getModEvents(modId).renderTypeRegistrations.add(Pair.of(block, renderTypeSupplier));
    }

    @OnlyIn(Dist.CLIENT)
    <T extends Entity> void registerEntityRenderer(String modId, Supplier<EntityType<T>> entityType, Supplier<Function<EntityRendererProvider.Context, EntityRenderer<T>>> entityRenderer)
    {
        getModEvents(modId).entityRendererRegistrations.add(new EntityRendererRegistration<>(entityType, entityRenderer));
    }

    public <T extends Entity> void registerEntityAttributes(String modId, Supplier<EntityType<T>> entityType, Supplier<AttributeSupplier.Builder> attributes)
    {
        getModEvents(modId).entityAttributeRegistrations.add(new EntityAttributeRegistration<>(entityType, attributes));
    }

    @OnlyIn(Dist.CLIENT)
    <T extends BlockEntity> void registerBlockEntityRenderer(String modId, Supplier<BlockEntityType<T>> blockEntityType, Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>>> blockEntityRenderer)
    {
        getModEvents(modId).blockEntityRendererRegistrations.add(new BlockEntityRendererRegistration<>(blockEntityType, blockEntityRenderer));
    }

    private ModEvents getModEvents(String modId)
    {
        if(modBuses.containsKey(modId)) return modBuses.get(modId);
        else
        {
            // this *MUST* match inorder to obtain the correct IEventBus
            Validate.isTrue(ModLoadingContext.get().getActiveNamespace().equals(modId));

            var modEvents = new ModEvents(platform, modId, FMLJavaModLoadingContext.get().getModEventBus());
            modBuses.put(modId, modEvents);
            return modEvents;
        }
    }

    IEventBus getModBus(String modId)
    {
        return getModEvents(modId).modBus;
    }

    private static final class ModEvents extends ForgePlatformHolder
    {
        private final List<Pair<Block, Supplier<Supplier<RenderType>>>> renderTypeRegistrations = Lists.newArrayList();
        private final List<EntityRendererRegistration<? extends Entity>> entityRendererRegistrations = Lists.newArrayList();
        private final List<EntityAttributeRegistration<? extends Entity>> entityAttributeRegistrations = Lists.newArrayList();
        private final List<BlockEntityRendererRegistration<? extends BlockEntity>> blockEntityRendererRegistrations = Lists.newArrayList();

        private final String modId;
        private final IEventBus modBus;

        private ModEvents(ForgePlatform platform, String modId, IEventBus modBus)
        {
            super(platform);

            this.modId = modId;
            this.modBus = modBus;

            platform.getLogger().debug("Registering IModBus events for mod: {}", modId);

            modBus.addListener(this::onRegisterCreativeModeTab);
            modBus.addListener(this::onBuildCreativeModeTabContents);
            modBus.addListener(this::onRegisterEntityAttributes);

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                modBus.addListener(this::onClientSetup);
                modBus.addListener(this::onRegisterEntityRenderers);
            });
        }

        private void onRegisterCreativeModeTab(CreativeModeTabEvent.Register event)
        {
            platform.getLogger().debug("Registering CreativeModeTabs for mod: {}", modId);

            xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.REGISTER.post(new xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.Register(
                    (registryName, beforeEntries, afterEntries, configurator) -> event.registerCreativeModeTab(new ResourceLocation(modId, registryName), beforeEntries, afterEntries, configurator)
            ));
        }

        private void onBuildCreativeModeTabContents(CreativeModeTabEvent.BuildContents event)
        {
            platform.getLogger().debug("Building CreativeModeTab Contents for mod: {}", modId);

            xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.BUILD_CONTENTS.post(new xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.BuildContents(
                    event.getTab(),
                    event.getFlags(),
                    event.hasPermissions(),
                    event.getEntries()::put
            ));
        }

        @OnlyIn(Dist.CLIENT)
        private void onClientSetup(FMLClientSetupEvent event)
        {
            if(!renderTypeRegistrations.isEmpty())
            {
                renderTypeRegistrations.forEach(entry -> {
                    var renderType = entry.getRight().get().get();
                    if(renderType != null) ItemBlockRenderTypes.setRenderLayer(entry.getLeft(), renderType);
                });

                renderTypeRegistrations.clear();
            }
        }

        @OnlyIn(Dist.CLIENT)
        private void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            if(!entityRendererRegistrations.isEmpty())
            {
                entityRendererRegistrations.forEach(entry -> entry.register(event));
                entityRendererRegistrations.clear();
            }

            if(!blockEntityRendererRegistrations.isEmpty())
            {
                blockEntityRendererRegistrations.forEach(entry -> entry.register(event));
                blockEntityRendererRegistrations.clear();
            }
        }

        private void onRegisterEntityAttributes(EntityAttributeCreationEvent event)
        {
            if(!entityAttributeRegistrations.isEmpty())
            {
                entityAttributeRegistrations.forEach(entry -> entry.register(event));
                entityAttributeRegistrations.clear();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private record EntityRendererRegistration<T extends Entity>(Supplier<EntityType<T>> entityType, Supplier<Function<EntityRendererProvider.Context, EntityRenderer<T>>> entityRenderer)
    {
        private void register(EntityRenderersEvent.RegisterRenderers event)
        {
            event.registerEntityRenderer(entityType.get(), entityRenderer.get()::apply);
        }
    }

    private record EntityAttributeRegistration<T extends Entity>(Supplier<EntityType<T>> entityType, Supplier<AttributeSupplier.Builder> attributes)
    {
        @SuppressWarnings("unchecked")
        private void register(EntityAttributeCreationEvent event)
        {
            event.put((EntityType<? extends LivingEntity>) entityType.get(), attributes.get().build());
        }
    }

    @OnlyIn(Dist.CLIENT)
    private record BlockEntityRendererRegistration<T extends BlockEntity>(Supplier<BlockEntityType<T>> blockEntityType, Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>>> blockEntityRenderer)
    {
        private void register(EntityRenderersEvent.RegisterRenderers event)
        {
            event.registerBlockEntityRenderer(blockEntityType.get(), blockEntityRenderer.get()::apply);
        }
    }
}
