package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;

public class Age5CropsBlock extends BetterCropsBlock
{
	public static final IntegerProperty AGE = BlockStateProperties.AGE_5;

	public Age5CropsBlock(Properties properties, @Nullable IItemProvider baseSeedItem)
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
