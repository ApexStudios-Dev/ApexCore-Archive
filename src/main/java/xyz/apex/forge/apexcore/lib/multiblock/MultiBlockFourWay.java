package xyz.apex.forge.apexcore.lib.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;

public class MultiBlockFourWay extends MultiBlock
{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public MultiBlockFourWay(Properties properties, MultiBlockPattern pattern)
	{
		super(properties, pattern);

		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext ctx, BlockState defaultBlockState)
	{
		var placementState = super.getPlacementState(ctx, defaultBlockState);

		if(placementState != null)
			return placementState.setValue(FACING, ctx.getHorizontalDirection().getOpposite());

		return null;
	}

	@Override
	public final BlockState rotate(BlockState blockState, LevelAccessor level, BlockPos pos, Rotation rotation)
	{
		var rotatedBlockState = super.rotate(blockState, level, pos, rotation);
		pattern.rotate(blockState, level, pos, rotation, rotatedBlockState);
		return rotatedBlockState;
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation)
	{
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror)
	{
		return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
		super.createBlockStateDefinition(builder);
	}
}
