package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.function.ToIntFunction;

public final class BlockHelper
{
	public static ToIntFunction<BlockState> litBlockEmission(int lightEmission)
	{
		return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightEmission : 0;
	}

	public static Boolean never(BlockState blockState, IBlockReader level, BlockPos pos, EntityType<?> entityType)
	{
		return false;
	}

	public static Boolean always(BlockState blockState, IBlockReader level, BlockPos pos, EntityType<?> entityType)
	{
		return true;
	}

	public static Boolean ocelotOrParrot(BlockState blockState, IBlockReader level, BlockPos pos, EntityType<?> entityType)
	{
		return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
	}

	public static boolean always(BlockState blockState, IBlockReader level, BlockPos pos)
	{
		return true;
	}

	public static boolean never(BlockState blockState, IBlockReader level, BlockPos pos)
	{
		return false;
	}
}
