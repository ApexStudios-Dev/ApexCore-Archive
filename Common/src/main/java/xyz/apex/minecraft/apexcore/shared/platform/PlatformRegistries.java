package xyz.apex.minecraft.apexcore.shared.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterial;

import xyz.apex.minecraft.apexcore.shared.registry.ArmorMaterialBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.TierBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;

import java.util.Collection;
import java.util.function.Supplier;

public interface PlatformRegistries extends PlatformHolder
{
    <T> Collection<T> getAllKnown(ResourceKey<? extends Registry<T>> registryType, String modId);

    <T, R extends T> void register(ResourceKey<? extends Registry<T>> registryType, RegistryEntry<R> registryEntry, Supplier<R> factory);

    EnhancedTier registerTier(TierBuilder builder);

    ArmorMaterial registerArmorMaterial(ArmorMaterialBuilder builder);
}
