package xyz.apex.forge.apexcore.revamp.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ISeatMultiBlock extends ISeatBlock, IMultiBlock
{
	default boolean sitAtOriginOnly()
	{
		return false;
	}

	@Override
	default BlockPos getSeatSitPos(BlockState blockState, BlockPos pos)
	{
		if(sitAtOriginOnly())
			return getMultiBlockOriginPos(blockState, pos);
		return ISeatBlock.super.getSeatSitPos(blockState, pos);
	}

	@Override
	default void setSeatOccupied(Level level, BlockPos pos, BlockState blockState, boolean occupied)
	{
		if(sitAtOriginOnly())
		{
			var origin = getMultiBlockOriginPos(blockState, pos);

			for(var localSpace : getMultiBlockLocalPositions())
			{
				var worldSpace = getMultiBlockWorldSpaceFromLocalSpace(blockState, origin, localSpace);
				var seatBlockState = level.getBlockState(worldSpace);

				ISeatBlock.super.setSeatOccupied(level, worldSpace, seatBlockState, occupied);
			}
		}
		else
			ISeatBlock.super.setSeatOccupied(level, pos, blockState, occupied);
	}
}
