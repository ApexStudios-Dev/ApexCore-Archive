package xyz.apex.forge.apexcore.lib.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.util.Lazy;

/**
 * Fixes the following issues with the base {@link CropBlock} class
 * <ul>
 * <li>Using incorrect {@link Property} during {@link #createBlockStateDefinition(StateDefinition.Builder)}</li>
 * <li>Obtaining correct max age during {@link #getMaxAge()}</li>
 * <li>Allowing base seed item to be overridden</li>
 * </ul>
 */
@Deprecated(forRemoval = true)
public class BetterCropsBlock extends CropBlock
{
	@Nullable protected final ItemLike baseSeedItem;
	private final Lazy<Item> lazySeedItem; // we use a lazy value so that #getBaseSeedIdRaw is only ever called once

	/**
	 * @param baseSeedItem Null to use block item as seed item
	 */
	public BetterCropsBlock(Properties properties, @Nullable ItemLike baseSeedItem)
	{
		super(properties);

		this.baseSeedItem = baseSeedItem;
		lazySeedItem = Lazy.of(() -> getBaseSeedIdRaw().asItem());
	}

	public BetterCropsBlock(Properties properties)
	{
		this(properties, null);
	}

	protected Item getBaseSeedIdRaw()
	{
		// no overridden type specified
		// use block item
		if(baseSeedItem == null)
			return asItem();

		var result = baseSeedItem.asItem();

		// overridden type gave us null
		// use Items#SEEDS (from CropsBlock)
		if(result == null)
			return super.getBaseSeedId().asItem();

		// use custom seed type
		return result;
	}

	@Override
	protected ItemLike getBaseSeedId()
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(getAgeProperty()); // CropsBlock uses AGE_7 this fixes that to use the correct age property
	}
}