package dev.apexstudios.apexcore.fabric.platform;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.platform.ModRegistries;
import dev.apexstudios.apexcore.common.platform.PlatformRegistries;
import net.covers1624.quack.util.LazyValue;
import net.covers1624.quack.util.SneakyUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

class PlatformRegistriesImpl implements PlatformRegistries
{
    private final Map<String, ModRegistries> modRegistries = Maps.newConcurrentMap();
    private boolean registered = false;

    @Override
    public ModRegistries forMod(String id)
    {
        return modRegistries.computeIfAbsent(id, $ -> {
            var registry = new ModRegistriesImpl(id);

            if(registered)
                registry.register();

            return registry;
        });
    }

    @Override
    public <T> Registry<T> registry(@Nullable RegistryAccess registryAccess, ResourceKey<? extends Registry<T>> registryType)
    {
        if(registryAccess == null)
            return registry(registryType);

        return registryAccess.registryOrThrow(registryType);
    }

    @Override
    public <T> Registry<T> registry(ResourceKey<? extends Registry<T>> registryType)
    {
        return BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryType);
    }

    @Override
    public <T, R extends T> Supplier<R> register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> registryEntryFactory)
    {
        var registryEntry = Registry.register(registry(registryType), registryName, registryEntryFactory.get());
        return new LazyValue<>(() -> SneakyUtils.unsafeCast(registryEntry));
    }

    @Override
    public <T, R extends T> Holder.Reference<R> registerForHolder(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> registryEntryFactory)
    {
        return SneakyUtils.unsafeCast(Registry.registerForHolder(registry(registryType), registryName, registryEntryFactory.get()));
    }

    public void register()
    {
        if(registered)
            return;

        registered = true;
        modRegistries.values().forEach(registry -> ((ModRegistriesImpl) registry).register());
    }
}
