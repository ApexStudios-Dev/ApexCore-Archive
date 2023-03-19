package xyz.apex.minecraft.apexcore.common.registry;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import xyz.apex.minecraft.apexcore.common.platform.Platform;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class RegistryManager
{
    private static final Map<String, RegistryManager> INSTANCES = Maps.newHashMap();

    private final String ownerId;
    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries = Maps.newHashMap();

    private RegistryManager(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    @SuppressWarnings("unchecked")
    public <T> DeferredRegister<T> getRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        return (DeferredRegister<T>) registries.computeIfAbsent(registryType, $ -> Platform.INSTANCE.internals().deferredRegister(ownerId, registryType));
    }

    public void register()
    {
        register(this);
    }

    public static RegistryManager get(String ownerId)
    {
        return INSTANCES.computeIfAbsent(ownerId, RegistryManager::new);
    }

    public static void register(String ownerId)
    {
        var registryManager = INSTANCES.get(ownerId);
        if(registryManager != null) register(registryManager);
    }

    private static void register(RegistryManager registryManager)
    {
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

        Consumer<ResourceKey<? extends Registry<?>>> register = registryType -> {
            var registry = registryManager.registries.get(registryType);
            if(registry != null) registry.register();
        };

        registries.forEach(register);
        BuiltInRegistries.REGISTRY.registryKeySet().stream().filter(registryType -> !registries.contains(registryType)).forEach(register);
    }
}
