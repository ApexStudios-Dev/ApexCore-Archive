package xyz.apex.minecraft.apexcore.forge.hooks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class ForgeRendererHooks extends ForgePlatformHolder implements RendererHooks
{
    private final Map<String, Mod> mods = Maps.newHashMap();

    ForgeRendererHooks(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public void registerRenderType(Block block, Supplier<Supplier<RenderType>> renderType)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        compute(mod -> mod.clientSetupListeners.add(() -> ItemBlockRenderTypes.setRenderLayer(block, renderType.get().get())));
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, Supplier<EntityRendererProvider<T>> entityRendererProvider)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        compute(mod -> mod.registerRendererListeners.add(event -> event.registerEntityRenderer(entityType, entityRendererProvider.get())));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, Supplier<BlockEntityRendererProvider<T>> blockEntityRendererProvider)
    {
        if(!platform.getPhysicalSide().isClient()) return;
        compute(mod -> mod.registerRendererListeners.add(event -> event.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider.get())));
    }

    @SafeVarargs
    @Override
    public final void registerItemColor(Supplier<ItemColor> itemColor, Supplier<? extends ItemLike>... items)
    {
        if(!platform.getPhysicalSide().isClient()) return;

        compute(mod -> mod.itemColorListeners.add(event -> {
            var resolved = Stream.of(items).map(Supplier::get).toArray(ItemLike[]::new);
            event.register(itemColor.get(), resolved);
        }));
    }

    @SafeVarargs
    @Override
    public final void registerBlockColor(Supplier<BlockColor> blockColor, Supplier<? extends Block>... blocks)
    {
        if(!platform.getPhysicalSide().isClient()) return;

        compute(mod -> mod.blockColorListeners.add(event -> {
            var resolved = Stream.of(blocks).map(Supplier::get).toArray(Block[]::new);
            event.register(blockColor.get(), resolved);
        }));
    }

    private void compute(Consumer<Mod> consumer)
    {
        var modId = ModLoadingContext.get().getActiveNamespace();
        var mod = mods.computeIfAbsent(modId, $ -> new Mod());
        consumer.accept(mod);
    }

    private static final class Mod
    {
        private final List<Runnable> clientSetupListeners = Lists.newLinkedList();
        private final List<Consumer<EntityRenderersEvent.RegisterRenderers>> registerRendererListeners = Lists.newLinkedList();
        private final List<Consumer<RegisterColorHandlersEvent.Item>> itemColorListeners = Lists.newLinkedList();
        private final List<Consumer<RegisterColorHandlersEvent.Block>> blockColorListeners = Lists.newLinkedList();

        private Mod()
        {
            var modBus = FMLJavaModLoadingContext.get().getModEventBus();
            modBus.addListener(EventPriority.HIGHEST, this::onClientSetup);
            modBus.addListener(EventPriority.HIGHEST, this::onRegisterItemColor);
            modBus.addListener(EventPriority.HIGHEST, this::onRegisterBlockColor);
            modBus.addListener(EventPriority.HIGHEST, this::onRegisterRenderers);
        }

        private void onClientSetup(FMLClientSetupEvent event)
        {
            clientSetupListeners.forEach(Runnable::run);
            clientSetupListeners.clear();
        }

        private void onRegisterItemColor(RegisterColorHandlersEvent.Item event)
        {
            itemColorListeners.forEach(listener -> listener.accept(event));
            itemColorListeners.clear();
        }

        private void onRegisterBlockColor(RegisterColorHandlersEvent.Block event)
        {
            blockColorListeners.forEach(listener -> listener.accept(event));
            blockColorListeners.clear();
        }

        private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            registerRendererListeners.forEach(listener -> listener.accept(event));
            registerRendererListeners.clear();
        }
    }
}
