package xyz.apex.forge.apexcore.lib.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public class MultiBlock extends Block
{
	protected final MultiBlockPattern pattern;

	public MultiBlock(Properties properties, MultiBlockPattern pattern)
	{
		super(properties);
		this.pattern = pattern;
		replaceStateContainer(this);
	}

	@Nullable
	@Override
	public final BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		var defaultBlockState = defaultBlockState();
		var placementState = pattern.getStateForPlacement(this, ctx, defaultBlockState);

		if(placementState != null)
			placementState = getPlacementState(ctx, placementState);

		return placementState;
	}

	@Override
	public final boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
	{
		return pattern.canSurvive(this, level, pos, blockState) && canSurviveAdditional(level, pos, blockState);
	}

	protected boolean canSurviveAdditional(LevelReader level, BlockPos pos, BlockState blockState)
	{
		return true;
	}

	@Nullable
	protected BlockState getPlacementState(BlockPlaceContext ctx, BlockState defaultBlockState)
	{
		return defaultBlockState;
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
	{
		pattern.onPlace(blockState, level, pos, oldBlockState, isMoving);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
	{
		if(!blockState.is(oldBlockState.getBlock()))
			pattern.onRemove(blockState, level, pos, oldBlockState, isMoving);

		super.onRemove(blockState, level, pos, oldBlockState, isMoving);
	}

	public final MultiBlockPattern getMultiBlockPattern()
	{
		return pattern;
	}

	// required as the default is built before our pattern has been set
	public static void replaceStateContainer(MultiBlock block)
	{
		var builder = new StateDefinition.Builder<Block, BlockState>(block);
		block.createBlockStateDefinition(builder);
		block.pattern.createBlockStateDefinition(builder);
		var stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
		block.registerDefaultState(block.pattern.registerDefaultState(stateDefinition.any()));

		ObfuscationReflectionHelper.setPrivateValue(Block.class, block, stateDefinition, "f_49792_");
	}
}
