package xyz.apex.forge.apexcore.lib.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class MultiBlockFourWay extends MultiBlock
{
	public static final DirectionProperty FACING = HorizontalBlock.FACING;

	public MultiBlockFourWay(Properties properties, MultiBlockPattern pattern)
	{
		super(properties, pattern);

		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockItemUseContext ctx, BlockState defaultBlockState)
	{
		return defaultBlockState.setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public final BlockState rotate(BlockState blockState, IWorld level, BlockPos pos, Rotation rotation)
	{
		BlockState rotatedBlockState = super.rotate(blockState, level, pos, rotation);
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
		super.createBlockStateDefinition(builder);
	}
}
