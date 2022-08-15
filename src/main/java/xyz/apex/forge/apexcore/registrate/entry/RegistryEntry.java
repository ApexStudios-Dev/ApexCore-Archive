package xyz.apex.forge.apexcore.registrate.entry;

import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

public class RegistryEntry<T extends IForgeRegistryEntry<? super T>> extends com.tterrag.registrate.util.entry.RegistryEntry<T>
{
	protected final CoreRegistrate<?> owner;

	protected RegistryEntry(CoreRegistrate<?> owner, RegistryObject<T> delegate)
	{
		super(owner.backend, delegate);

		this.owner = owner;
	}

	public final CoreRegistrate<?> getOwner()
	{
		return owner;
	}

	// exists to make protected version public
	public static <E extends com.tterrag.registrate.util.entry.RegistryEntry<?>> E cast0(Class<? super E> clazz, com.tterrag.registrate.util.entry.RegistryEntry<?> entry)
	{
		return com.tterrag.registrate.util.entry.RegistryEntry.cast(clazz, entry);
	}

	public static <E extends RegistryEntry<?>> E cast(Class<? super E> clazz, RegistryEntry<?> entry)
	{
		return cast0(clazz, entry);
	}
}