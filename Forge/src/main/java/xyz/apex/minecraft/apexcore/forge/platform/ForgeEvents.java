package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import org.apache.commons.compress.utils.Lists;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformEvents;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class ForgeEvents extends ForgePlatformHolder implements PlatformEvents
{
    private final Set<String> dataMods = Sets.newHashSet();
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
        register();
        if(platform.isDedicatedServer()) return;
        ItemBlockRenderTypes.setRenderLayer(block.get(), ChunkRenderTypeSet.of(renderTypes.get().get()));
    }

    @Override
    public void registerItemColor(Supplier<? extends Item> itemSupplier, Supplier<Supplier<ItemColor>> colorHandler)
    {
        register();
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
        register();
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

    public void register()
    {
        var modId = ModLoadingContext.get().getActiveNamespace();
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        if(registeredMods.contains(modId)) return;

        if(platform.isClient())
        {
            modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, event -> {
                clientRunnables.forEach(Runnable::run);
                clientRunnables.clear();
                clientRan = true;
            });

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
            modBus.addListener(EventPriority.NORMAL, false, FMLDedicatedServerSetupEvent.class, event -> {
                serverRunnables.forEach(Runnable::run);
                serverRunnables.clear();
                serverRan = true;
            });
        }

        registeredMods.add(modId);
    }

    @Override
    public void registerDataGenerators(String modId)
    {
        register();
        if(dataMods.contains(modId)) return;
        platform.getLogger().debug("Captured mod '{}' data generation registration!", modId);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGatherData);
        dataMods.add(modId);
    }

    private void onGatherData(GatherDataEvent event)
    {
        var modId = event.getModContainer().getModId();
        if(!dataMods.contains(modId)) return;
        platform.getLogger().info("Registering mod '{}' DataGenerators...", modId);

        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();

        var client = event.includeClient();
        var server = event.includeServer();

        Generators.registerDataGenerators(
                modId,
                generator::getPackOutput,
                lookupProvider,
                provider -> generator.addProvider(client, provider),
                provider -> generator.addProvider(server, provider)
        );

        AtomicReference<TagsProvider<Block>> blockTagsProvider = new AtomicReference<>();

        if(Generators.shouldRegister(modId, ProviderTypes.BLOCK_TAGS))
        {
            blockTagsProvider.set(generator.<BlockTagsProvider>addProvider(server, output -> new BlockTagsProvider(output, lookupProvider, modId, existingFileHelper) {
                @Override
                protected void addTags(HolderLookup.Provider provider)
                {
                    Generators.processDataGenerator(modId, ProviderTypes.BLOCK_TAGS, this);
                }
            }));
        }

        if(Generators.shouldRegister(modId, ProviderTypes.ITEM_TAGS))
        {
            blockTagsProvider.compareAndSet(null, new BlockTagsProvider(generator.getPackOutput(), lookupProvider, modId, existingFileHelper) {
                @Override
                protected void addTags(HolderLookup.Provider provider)
                {
                }
            });

            generator.<ItemTagsProvider>addProvider(server, output -> new ItemTagsProvider(output, lookupProvider, blockTagsProvider.get(), modId, existingFileHelper) {
                @Override
                protected void addTags(HolderLookup.Provider provider)
                {
                    Generators.processDataGenerator(modId, ProviderTypes.ITEM_TAGS, this);
                }
            });
        }
    }

    @SuppressWarnings({ "unchecked", "DataFlowIssue" })
    <T> DeferredRegister<T> getModRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        register();
        var modId = ModLoadingContext.get().getActiveNamespace();

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
