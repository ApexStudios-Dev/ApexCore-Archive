package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class ModdedRegistries
{
    @ApiStatus.Internal // internal usages only, modders should never use this
    public static final ModdedRegistries INSTANCE = new ModdedRegistries();

    private final Table<ResourceKey<? extends Registry<?>>, String, ModdedRegistry<?>> registryTable = HashBasedTable.create();
    private final Map<ResourceKey<?>, List<ModdedRegistry.RegisterCallback<?>>> registerCallbackMap = Maps.newHashMap();

    @SuppressWarnings({ "unchecked", "ConstantConditions" })
    public <T, R extends ModdedRegistry<T>> R getOrCreate(ResourceKey<Registry<T>> type, String modId, Constructor<T, R> constructor)
    {
        if(INSTANCE.registryTable.contains(type, modId)) return (R) INSTANCE.registryTable.get(type, modId);

        var modded = constructor.create(type, modId);
        INSTANCE.registryTable.put(type, modId, modded);
        return modded;
    }

    @SuppressWarnings("unchecked")
    <T, R extends T> void onRegister(ResourceKey<T> key, RegistryEntry<R> entry, R value)
    {
        if(!registerCallbackMap.containsKey(key)) return;
        registerCallbackMap.get(key).forEach(callback -> ((ModdedRegistry.RegisterCallback<R>) callback).onRegister(key, entry, value));
        registerCallbackMap.clear();
    }

    public static <T> ModdedRegistry<T> get(ResourceKey<Registry<T>> type, String modId)
    {
        return INSTANCE.<T, ModdedRegistry<T>>getOrCreate(type, modId, BasicRegistry::new);
    }

    public static <T, R extends T> void addOnRegisterCallback(ResourceKey<T> key, ModdedRegistry.RegisterCallback<R> callback)
    {
        INSTANCE.registerCallbackMap.computeIfAbsent(key, $ -> Lists.newArrayList()).add(callback);
    }

    @FunctionalInterface
    public interface Constructor<T, R extends ModdedRegistry<T>>
    {
        R create(ResourceKey<Registry<T>> type, String modId);

        static <T, R extends ModdedRegistry<T>> Constructor<T, R> of(Function<String, R> constructor)
        {
            return (type, modId) -> constructor.apply(modId);
        }
    }
}
