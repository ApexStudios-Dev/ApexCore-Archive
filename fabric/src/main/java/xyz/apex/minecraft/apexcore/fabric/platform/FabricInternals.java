package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import xyz.apex.minecraft.apexcore.common.platform.Internals;
import xyz.apex.minecraft.apexcore.common.platform.SideExecutor;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;

import java.util.List;
import java.util.Map;

final class FabricInternals implements Internals
{
    private final Map<String, FabricModInternals> modInternals = Maps.newHashMap();
    private boolean registered = false;

    FabricInternals()
    {
        SideExecutor.runForSide(
                () -> () -> ClientLifecycleEvents.CLIENT_STARTED.register(client -> client.executeIfPossible(this::register)),
                () -> () -> ServerLifecycleEvents.SERVER_STARTING.register(server -> server.executeIfPossible(this::register))
        );
    }

    @Override
    public <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return modInternals.computeIfAbsent(ownerId, FabricModInternals::new).deferredRegister(registryType);
    }

    private void register()
    {
        if(registered) return;

        // order copied from Forge GameData
        var registries = List.of(
                // Game objects
                Registries.BLOCK,
                Registries.FLUID,
                Registries.ITEM,
                Registries.MOB_EFFECT,
                Registries.SOUND_EVENT,
                Registries.POTION,
                Registries.ENCHANTMENT,
                Registries.ENTITY_TYPE,
                Registries.BLOCK_ENTITY_TYPE,
                Registries.PARTICLE_TYPE,
                Registries.MENU,
                Registries.PAINTING_VARIANT,
                Registries.RECIPE_TYPE,
                Registries.RECIPE_SERIALIZER,
                Registries.ATTRIBUTE,
                Registries.STAT_TYPE,
                Registries.COMMAND_ARGUMENT_TYPE,

                // Villagers
                Registries.VILLAGER_PROFESSION,
                Registries.POINT_OF_INTEREST_TYPE,
                Registries.MEMORY_MODULE_TYPE,
                Registries.SENSOR_TYPE,
                Registries.SCHEDULE,
                Registries.ACTIVITY,

                // Worldgen
                Registries.CARVER,
                Registries.FEATURE,
                Registries.CHUNK_STATUS,
                Registries.BLOCK_STATE_PROVIDER_TYPE,
                Registries.FOLIAGE_PLACER_TYPE,
                Registries.TREE_DECORATOR_TYPE,

                // Dynamic Worldgen
                Registries.BIOME
        );

        registries.forEach(this::register);
        BuiltInRegistries.REGISTRY.registryKeySet().stream().filter(registryType -> !registries.contains(registryType)).forEach(this::register);

        registered = true;
    }

    private void register(ResourceKey<? extends Registry<?>> registryType)
    {
        modInternals.values().forEach(internals -> internals.register(registryType));
    }

    private final class FabricModInternals
    {
        private final String ownerId;
        private final Map<ResourceKey<? extends Registry<?>>, FabricDeferredRegister<?>> registries = Maps.newHashMap();

        private FabricModInternals(String ownerId)
        {
            this.ownerId = ownerId;
        }

        @SuppressWarnings("unchecked")
        private <T> FabricDeferredRegister<T> deferredRegister(ResourceKey<? extends Registry<T>> registryType)
        {
            return (FabricDeferredRegister<T>) registries.computeIfAbsent(registryType, $ -> {
                if(registered) throw new IllegalStateException("Attempt to create new DeferredRegister instance post registration");
                return new FabricDeferredRegister<>(ownerId, registryType);
            });
        }

        private void register(ResourceKey<? extends Registry<?>> registryType)
        {
            if(registered) return;
            var registry = registries.get(registryType);
            if(registry != null) registry.register();
        }
    }
}
