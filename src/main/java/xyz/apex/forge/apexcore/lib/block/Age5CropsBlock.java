package xyz.apex.forge.apexcore.lib.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class Age5CropsBlock extends BetterCropsBlock
{
	public static final IntegerProperty AGE = BlockStateProperties.AGE_5;

	public Age5CropsBlock(Properties properties, @Nullable ItemLike baseSeedItem)
	{
		super(properties, baseSeedItem);
	}

	public Age5CropsBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public IntegerProperty getAgeProperty()
	{
		return AGE;
	}

	@Override
	public int getMaxAge()
	{
		return 5;
	}
}