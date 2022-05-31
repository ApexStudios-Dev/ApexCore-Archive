package xyz.apex.forge.utility.registrator.entry;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.RegistryObject;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class PointOfInterestEntry extends RegistryEntry<PoiType> implements NonnullSupplier<PoiType>
{
	public PointOfInterestEntry(AbstractRegistrator<?> owner, RegistryObject<PoiType> delegate)
	{
		super(owner, delegate);
	}

	public PoiType asPointOfInterestType()
	{
		return get();
	}

	public int getMaxTickets()
	{
		return asPointOfInterestType().getMaxTickets();
	}

	public Predicate<PoiType> getPredicate()
	{
		return asPointOfInterestType().getPredicate();
	}

	public int getValidRange()
	{
		return asPointOfInterestType().getValidRange();
	}

	public ImmutableSet<BlockState> getBlockStates()
	{
		return asPointOfInterestType().getBlockStates();
	}

	public static PointOfInterestEntry cast(RegistryEntry<PoiType> registryEntry)
	{
		return cast(PointOfInterestEntry.class, registryEntry);
	}

	public static PointOfInterestEntry cast(com.tterrag.registrate.util.entry.RegistryEntry<PoiType> registryEntry)
	{
		return cast(PointOfInterestEntry.class, registryEntry);
	}
}
