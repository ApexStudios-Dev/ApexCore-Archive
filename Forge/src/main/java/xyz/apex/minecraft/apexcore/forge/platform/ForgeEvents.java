package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.Validate;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import xyz.apex.minecraft.apexcore.forge.platform.data.DataGenerators;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformEvents;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public final class ForgeEvents extends ForgePlatformHolder implements PlatformEvents
{
    private final Set<String> registeredMods = Sets.newHashSet();
    private final Table<String, ResourceKey<? extends Registry<?>>, DeferredRegister<?>> modRegistries = HashBasedTable.create();
    private final Table<String, Item, List<Supplier<Supplier<ItemColor>>>> itemColorHandlers = HashBasedTable.create();
    private final Table<String, Block, List<Supplier<Supplier<BlockColor>>>> blockColorHandlers = HashBasedTable.create();
    private final List<Runnable> clientRunnables = Lists.newArrayList();
    private final List<Runnable> serverRunnables = Lists.newArrayList();
    private boolean clientRan = false;
    private boolean serverRan = false;

    ForgeEvents(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public void onClientSetup(Runnable runnable)
    {
        if(clientRan) runnable.run();
        else clientRunnables.add(runnable);
    }

    @Override
    public void onDedicatedServerSetup(Runnable runnable)
    {
        if(serverRan) runnable.run();
        else serverRunnables.add(runnable);
    }

    @SuppressWarnings("removal")
    @Override
    public void registerRenderTypes(Supplier<? extends Block> block, Supplier<Supplier<RenderType[]>> renderTypes)
    {
        register(ModLoadingContext.get().getActiveNamespace());
        if(platform.isDedicatedServer()) return;
        ItemBlockRenderTypes.setRenderLayer(block.get(), ChunkRenderTypeSet.of(renderTypes.get().get()));
    }

    @Override
    public void registerItemColor(Supplier<? extends Item> itemSupplier, Supplier<Supplier<ItemColor>> colorHandler)
    {
        register(ModLoadingContext.get().getActiveNamespace());
        if(platform.isDedicatedServer()) return;

        var modId = ModLoadingContext.get().getActiveNamespace();
        var item = itemSupplier.get();

        var list = itemColorHandlers.get(modId, item);

        if(list == null)
        {
            list = Lists.newArrayList();
            itemColorHandlers.put(modId, item, list);
        }

        list.add(colorHandler);
    }

    @Override
    public void registerBlockColor(Supplier<? extends Block> blockSupplier, Supplier<Supplier<BlockColor>> colorHandler)
    {
        register(ModLoadingContext.get().getActiveNamespace());
        if(platform.isDedicatedServer()) return;

        var modId = ModLoadingContext.get().getActiveNamespace();
        var block = blockSupplier.get();

        var list = blockColorHandlers.get(modId, block);

        if(list == null)
        {
            list = Lists.newArrayList();
            blockColorHandlers.put(modId, block, list);
        }

        list.add(colorHandler);
    }

    @Override
    public void register(String modId)
    {
        Validate.isTrue(ModLoadingContext.get().getActiveNamespace().equals(modId));
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        if(registeredMods.contains(modId)) return;

        if(platform.isClient())
        {
            modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, event -> event.enqueueWork(() -> {
                clientRunnables.forEach(Runnable::run);
                clientRunnables.clear();
                clientRan = true;
            }));

            modBus.addListener(EventPriority.NORMAL, false, RegisterColorHandlersEvent.Item.class, event -> {
                if(itemColorHandlers.containsRow(modId))
                {
                    var row = itemColorHandlers.row(modId);

                    for(var entry : row.entrySet())
                    {
                        var item = entry.getKey();

                        for(var colorHandler : entry.getValue())
                        {
                            event.register(colorHandler.get().get(), item);
                        }
                    }

                    row.clear();
                }
            });

            modBus.addListener(EventPriority.NORMAL, false, RegisterColorHandlersEvent.Block.class, event -> {
                if(blockColorHandlers.containsRow(modId))
                {
                    var row = blockColorHandlers.row(modId);

                    for(var entry : row.entrySet())
                    {
                        var block = entry.getKey();

                        for(var colorHandler : entry.getValue())
                        {
                            event.register(colorHandler.get().get(), block);
                        }
                    }

                    row.clear();
                }
            });
        }

        if(platform.isDedicatedServer())
        {
            modBus.addListener(EventPriority.NORMAL, false, FMLDedicatedServerSetupEvent.class, event -> event.enqueueWork(() -> {
                serverRunnables.forEach(Runnable::run);
                serverRunnables.clear();
                serverRan = true;
            }));
        }

        registeredMods.add(modId);
    }

    @Override
    public void registerDataGenerators(String modId)
    {
        register(modId);
        platform.getLogger().debug("Captured mod '{}' data generation registration!", modId);
        DataGenerators.register(modId);
    }

    @SuppressWarnings({ "unchecked", "DataFlowIssue" })
    <T> DeferredRegister<T> getModRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        var modId = ModLoadingContext.get().getActiveNamespace();
        register(modId);

        if(modRegistries.contains(modId, registryType)) return (DeferredRegister<T>) modRegistries.get(modId, registryType);
        else
        {
            var modRegistry = DeferredRegister.create(registryType, modId);
            modRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
            modRegistries.put(modId, registryType, modRegistry);
            return modRegistry;
        }
    }
}
