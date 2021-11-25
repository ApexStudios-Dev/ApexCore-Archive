package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.Item;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IItemProvider;

import xyz.apex.java.utility.Lazy;

import javax.annotation.Nullable;

/**
 * Fixes the following issues with the base {@link CropsBlock} class
 * <ul>
 * <li>Using incorrect {@link Property} during {@link #createBlockStateDefinition(StateContainer.Builder)}</li>
 * <li>Obtaining correct max age during {@link #getMaxAge()}</li>
 * <li>Allowing base seed item to be overridden</li>
 * </ul>
 */
public class BetterCropsBlock extends CropsBlock
{
	@Nullable protected final IItemProvider baseSeedItem;
	private final Lazy<Item> lazySeedItem; // we use a lazy value so that #getBaseSeedIdRaw is only ever called once

	/**
	 * @param baseSeedItem Null to use block item as seed item
	 */
	public BetterCropsBlock(AbstractBlock.Properties properties, @Nullable IItemProvider baseSeedItem)
	{
		super(properties);

		this.baseSeedItem = baseSeedItem;
		lazySeedItem = Lazy.of(() -> getBaseSeedIdRaw().asItem());
	}

	public BetterCropsBlock(AbstractBlock.Properties properties)
	{
		this(properties, null);
	}

	protected Item getBaseSeedIdRaw()
	{
		// no overridden type specified
		// use block item
		if(baseSeedItem == null)
			return asItem();

		Item result = baseSeedItem.asItem();

		// overridden type gave us null
		// use Items#SEEDS (from CropsBlock)
		if(result == null)
			return super.getBaseSeedId().asItem();

		// use custom seed type
		return result;
	}

	@Override
	protected IItemProvider getBaseSeedId()
	{
		// allow the base seed item to be overridden
		// CropsBlock just uses Items#SEEDS
		// this uses the BlockItem if not specified in the constructor
		return lazySeedItem::get;
	}

	@Override
	public int getMaxAge()
	{
		// CropsBlock just returns 7 (since it uses AGE_7 property)
		// This allows any age property to be used
		// Returns the highest possible value as max age
		return getAgeProperty().getPossibleValues().stream().max(Integer::compare).orElseGet(super::getMaxAge);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(getAgeProperty()); // CropsBlock uses AGE_7 this fixes that to use the correct age property
	}
}
