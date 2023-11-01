package dev.apexstudios.apexcore.common.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface PlatformRegistries
{
    ModRegistries forMod(String id);

    <T> Registry<T> registry(@Nullable RegistryAccess registryAccess, ResourceKey<? extends Registry<T>> registryType);

    <T> Registry<T> registry(ResourceKey<? extends Registry<T>> registryType);

    <T, R extends T> Supplier<R> register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> registryEntryFactory);

    <T, R extends T> Holder.Reference<R> registerForHolder(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> registryEntryFactory);

    static PlatformRegistries get()
    {
        return Platform.INSTANCE.registries();
    }
}
