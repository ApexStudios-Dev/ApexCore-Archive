package xyz.apex.forge.apexcore.lib.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
	public final BlockState getStateForPlacement(BlockItemUseContext ctx)
	{
		BlockState defaultBlockState = defaultBlockState();
		BlockState placementState = pattern.getStateForPlacement(ctx, defaultBlockState);

		if(placementState != null)
			placementState = getPlacementState(ctx, placementState);

		return placementState;
	}

	@Nullable
	protected BlockState getPlacementState(BlockItemUseContext ctx, BlockState defaultBlockState)
	{
		return defaultBlockState;
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void onPlace(BlockState blockState, World level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
	{
		pattern.onPlace(blockState, level, pos, oldBlockState, isMoving);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void onRemove(BlockState blockState, World level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
	{
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
		StateContainer.Builder<Block, BlockState> builder = new StateContainer.Builder<>(block);
		block.createBlockStateDefinition(builder);
		block.pattern.createBlockStateDefinition(builder);
		StateContainer<Block, BlockState> stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
		block.registerDefaultState(block.pattern.registerDefaultState(stateDefinition.any()));

		ObfuscationReflectionHelper.setPrivateValue(Block.class, block, stateDefinition, "field_176227_L");
	}
}
