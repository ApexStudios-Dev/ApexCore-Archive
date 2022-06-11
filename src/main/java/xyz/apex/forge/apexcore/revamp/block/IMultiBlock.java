package xyz.apex.forge.apexcore.revamp.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IMultiBlock
{
	MultiBlockPattern getMultiBlockPattern();

	default Block asBlock()
	{
		return (Block) this;
	}

	// region: Wrappers
	default int getMultiBlockIndex(BlockState blockState)
	{
		return getMultiBlockPattern().getIndex(blockState);
	}

	default boolean isMultiBlockOrigin(BlockState blockState)
	{
		return getMultiBlockPattern().isOrigin(blockState);
	}

	default BlockPos getMultiBlockWorldSpaceFromLocalSpace(BlockState blockState, BlockPos origin, BlockPos localSpace)
	{
		return getMultiBlockPattern().getWorldSpaceFromLocalSpace(this, blockState, origin, localSpace);
	}

	default BlockPos getMultiBlockOriginFromWorldSpace(BlockState blockState, BlockPos worldSpace, BlockPos localSpace)
	{
		return getMultiBlockPattern().getWorldSpaceFromLocalSpace(this, blockState, worldSpace, localSpace);
	}

	default List<BlockPos> getMultiBlockLocalPositions()
	{
		return getMultiBlockPattern().getLocalPositions();
	}
	// endregion
}
