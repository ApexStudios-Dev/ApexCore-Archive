package xyz.apex.forge.utility.registrator.builder;

import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.PointOfInterestEntry;
import xyz.apex.java.utility.nullness.NonnullBiPredicate;
import xyz.apex.java.utility.nullness.NonnullPredicate;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unused")
public final class PointOfInterestBuilder<OWNER extends AbstractRegistrator<OWNER>, PARENT> extends RegistratorBuilder<OWNER, PoiType, PoiType, PARENT, PointOfInterestBuilder<OWNER, PARENT>, PointOfInterestEntry>
{
	private NonnullSupplier<? extends Block> block = () -> Blocks.AIR;
	private int maxTickets = 1;
	private Predicate predicate = (testType, thisType) -> testType == thisType;
	private int validRange = 1;

	public PointOfInterestBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback)
	{
		super(owner, parent, registryName, callback, PoiType.class, PointOfInterestEntry::new, PointOfInterestEntry::cast);
	}

	public PointOfInterestBuilder<OWNER, PARENT> matchingBlock(NonnullSupplier<? extends Block> block)
	{
		this.block = block;
		return this;
	}

	public PointOfInterestBuilder<OWNER, PARENT> maxTickets(int maxTickets)
	{
		this.maxTickets = maxTickets;
		return this;
	}

	public PointOfInterestBuilder<OWNER, PARENT> predicate(Predicate predicate)
	{
		this.predicate = predicate;
		return this;
	}

	public PointOfInterestBuilder<OWNER, PARENT> validRange(int validRange)
	{
		this.validRange = validRange;
		return this;
	}

	@Override
	protected @NonnullType PoiType createEntry()
	{
		var result = new AtomicReference<PoiType>();
		NonnullPredicate<PoiType> predicate = poiType -> this.predicate.apply(poiType, result.get());
		var matchingBlockStates = PoiType.getBlockStates(block.get());
		var registryName = getRegistryNameFull();
		var poiType = new PoiType(registryName, matchingBlockStates, maxTickets, predicate, validRange);
		result.set(poiType);
		return poiType;
	}

	@FunctionalInterface
	public interface Predicate extends NonnullBiPredicate<PoiType, PoiType>
	{
		@Override
		boolean test(PoiType testType, PoiType yourType);
	}
}
