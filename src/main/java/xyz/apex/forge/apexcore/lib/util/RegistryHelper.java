package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.Objects;
import java.util.function.Supplier;

public final class RegistryHelper
{
	public static <T> ITagManager<T> getTags(IForgeRegistry<T> registry)
	{
		return Objects.requireNonNull(registry.tags());
	}

	public static <T, R extends T> boolean hasTag(IForgeRegistry<T> registry, TagKey<T> tag, R value)
	{
		var tags = Objects.requireNonNull(registry.tags());
		return tags.getTag(tag).contains(value);
	}

	public static <T, R extends T> ResourceLocation getRegistryName(IForgeRegistry<T> registry, R value)
	{
		return Objects.requireNonNull(registry.getKey(value));
	}

	// The following methods are useful for registries registered via Registrate, RegistryEntry implements Supplier
	public static <T> ITagManager<T> getTags(Supplier<IForgeRegistry<T>> registrySupplier)
	{
		var registry = Objects.requireNonNull(registrySupplier.get());
		return getTags(registry);
	}

	public static <T, R extends T> boolean hasTag(Supplier<IForgeRegistry<T>> registrySupplier, TagKey<T> tag, R value)
	{
		var registry = Objects.requireNonNull(registrySupplier.get());
		return hasTag(registry, tag, value);
	}

	public static <T, R extends T> ResourceLocation getRegistryName(Supplier<IForgeRegistry<T>> registrySupplier, R value)
	{
		var registry = Objects.requireNonNull(registrySupplier.get());
		return getRegistryName(registry, value);
	}
}