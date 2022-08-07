package xyz.apex.forge.apexcore.registrate.entry;

import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

import java.util.function.Supplier;

public final class ForgeRegistryEntry<TYPE> implements NonNullSupplier<IForgeRegistry<TYPE>>
{
	private final CoreRegistrate<?> owner;
	private final ResourceKey<Registry<TYPE>> registryType;
	private final Lazy<IForgeRegistry<TYPE>> forgeRegistry;

	public ForgeRegistryEntry(CoreRegistrate<?> owner, ResourceKey<Registry<TYPE>> registryType, Supplier<IForgeRegistry<TYPE>> forgeRegistry)
	{
		this.owner = owner;
		this.registryType = registryType;
		this.forgeRegistry = Lazy.of(forgeRegistry);
	}

	public CoreRegistrate<?> getOwner()
	{
		return owner;
	}

	public ResourceKey<Registry<TYPE>> getRegistryType()
	{
		return registryType;
	}

	public IForgeRegistry<TYPE> getForgeRegistry()
	{
		return forgeRegistry.get();
	}

	@Override
	public IForgeRegistry<TYPE> get()
	{
		return forgeRegistry.get();
	}
}