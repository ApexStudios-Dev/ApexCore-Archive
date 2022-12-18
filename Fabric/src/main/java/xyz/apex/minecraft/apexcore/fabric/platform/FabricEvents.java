package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.commons.compress.utils.Lists;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformEvents;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class FabricEvents extends FabricPlatformHolder implements PlatformEvents
{
    private final Table<String, ResourceKey<? extends Registry<?>>, List<Runnable>> registrations = HashBasedTable.create();
    private final List<Runnable> clientRunnables = Lists.newArrayList();
    private final List<Runnable> serverRunnables = Lists.newArrayList();
    private boolean clientRan = false;
    private boolean serverRan = false;

    FabricEvents(FabricPlatform platform)
    {
        super(platform);

        if(platform.isClient())
        {
            ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
                clientRunnables.forEach(Runnable::run);
                clientRunnables.clear();
                clientRan = true;
            });
        }

        if(platform.isDedicatedServer())
        {
            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                serverRunnables.forEach(Runnable::run);
                serverRunnables.clear();
                serverRan = true;
            });
        }
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

    @Override
    public void registerRenderTypes(Supplier<? extends Block> block, Supplier<Supplier<RenderType[]>> renderTypes)
    {
        if(platform.isDedicatedServer()) return;
        var layers = renderTypes.get().get();
        if(layers.length == 0) return;
        BlockRenderLayerMap.INSTANCE.putBlock(block.get(), layers[0]);
    }

    @Override
    public void registerItemColor(Supplier<? extends Item> itemSupplier, Supplier<Supplier<ItemColor>> colorHandler)
    {
        if(platform.isDedicatedServer()) return;
        ColorProviderRegistry.ITEM.register(colorHandler.get().get(), itemSupplier.get());
    }

    @Override
    public void registerBlockColor(Supplier<? extends Block> blockSupplier, Supplier<Supplier<BlockColor>> colorHandler)
    {
        if(platform.isDedicatedServer()) return;
        ColorProviderRegistry.BLOCK.register(colorHandler.get().get(), blockSupplier.get());
    }

    @Override
    public void register(String modId)
    {
        if(registrations.containsRow(modId))
        {
            BuiltInRegistries.REGISTRY.forEach(registry -> {
                var registryType = registry.key();
                var registrations = this.registrations.get(modId, registryType);

                if(registrations != null)
                {
                    registrations.forEach(Runnable::run);
                    registrations.clear();
                    this.registrations.remove(modId, registryType);
                }
            });
        }
    }

    @Override
    public void registerDataGenerators(String modId)
    {
        // NOOP
    }

    <T, R extends T> Supplier<R> register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey, Supplier<R> factory)
    {
        var ref = new AtomicReference<R>();
        var registrations = getRegistrations(registryKey.location().getNamespace(), registryType);

        registrations.add(() -> {
            var registry = RegistryEntry.getRegistryOrThrow(registryType);
            ref.set(Registry.register(registry, registryKey, factory.get()));
        });

        return ref::get;
    }

    @SuppressWarnings("DataFlowIssue")
    private <T, R extends T> List<Runnable> getRegistrations(String modId, ResourceKey<? extends Registry<T>> registryType)
    {
        if(registrations.contains(modId, registryType)) return registrations.get(modId, registryType);
        var list = Lists.<Runnable>newArrayList();
        registrations.put(modId, registryType, list);
        return list;
    }
}
