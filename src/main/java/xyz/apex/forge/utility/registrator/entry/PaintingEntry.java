package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.fmllegacy.RegistryObject;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;

@SuppressWarnings("unused")
public final class PaintingEntry extends RegistryEntry<Motive> implements NonnullSupplier<Motive>
{
	public PaintingEntry(AbstractRegistrator<?> owner, RegistryObject<Motive> delegate)
	{
		super(owner, delegate);
	}

	public Motive asPaintingType()
	{
		return get();
	}

	public int getWidth()
	{
		return asPaintingType().getWidth();
	}

	public int getHeight()
	{
		return asPaintingType().getHeight();
	}

	public static PaintingEntry cast(RegistryEntry<Motive> registryEntry)
	{
		return cast(PaintingEntry.class, registryEntry);
	}

	public static PaintingEntry cast(com.tterrag.registrate.util.entry.RegistryEntry<Motive> registryEntry)
	{
		return cast(PaintingEntry.class, registryEntry);
	}
}
