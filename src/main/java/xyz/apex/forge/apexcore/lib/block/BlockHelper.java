package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.ToIntFunction;

public final class BlockHelper
{
	public static ToIntFunction<BlockState> litBlockEmission(int lightEmission)
	{
		return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightEmission : 0;
	}

	public static Boolean never(BlockState blockState, LevelReader level, BlockPos pos, EntityType<?> entityType)
	{
		return false;
	}

	public static Boolean always(BlockState blockState, LevelReader level, BlockPos pos, EntityType<?> entityType)
	{
		return true;
	}

	public static Boolean ocelotOrParrot(BlockState blockState, LevelReader level, BlockPos pos, EntityType<?> entityType)
	{
		return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
	}

	public static boolean always(BlockState blockState, LevelReader level, BlockPos pos)
	{
		return true;
	}

	public static boolean never(BlockState blockState, LevelReader level, BlockPos pos)
	{
		return false;
	}
}
