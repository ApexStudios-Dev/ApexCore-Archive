package xyz.apex.forge.apexcore.revamp.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import xyz.apex.java.utility.nullness.NonnullConsumer;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class BaseMultiBlock extends BaseBlock implements IMultiBlock
{
	public BaseMultiBlock(Properties properties)
	{
		super(properties);

		registerDefaultState(getMultiBlockPattern().registerDefaultState(defaultBlockState()));
	}

	// region: IMultiBlock
	@Override
	public final Block asBlock()
	{
		return super.asBlock();
	}

	@Nullable
	@Override
	protected BlockState modifyPlacementState(BlockState placementBlockState, BlockPlaceContext ctx)
	{
		var blockState = super.modifyPlacementState(placementBlockState, ctx);

		if(blockState == null)
			return null;

		return getMultiBlockPattern().getStateForPlacement(this, blockState, ctx);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
	{
		var pattern = getMultiBlockPattern();

		if(!pattern.canSurvive(this, level, pos, blockState))
			return false;

		return super.canSurvive(blockState, level, pos);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
	{
		getMultiBlockPattern().onPlace(this, blockState, level, pos, oldBlockState);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
	{
		getMultiBlockPattern().onRemove(this, blockState, level, pos, newBlockState);
		super.onRemove(blockState, level, pos, newBlockState, isMoving);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	protected void registerProperties(NonnullConsumer<Property<?>> consumer)
	{
		super.registerProperties(consumer);
		getMultiBlockPattern().registerProperties(consumer);
	}
	// endregion

	public static abstract class WithBlockEntity<BLOCK_ENTITY extends BlockEntity> extends BaseBlock.WithBlockEntity<BLOCK_ENTITY> implements IMultiBlock
	{
		public WithBlockEntity(Properties properties)
		{
			super(properties);
		}

		// region: IMultiBlock
		@Override
		public final Block asBlock()
		{
			return super.asBlock();
		}

		@Nullable
		@Override
		protected BlockState modifyPlacementState(BlockState placementBlockState, BlockPlaceContext ctx)
		{
			var blockState = super.modifyPlacementState(placementBlockState, ctx);

			if(blockState == null)
				return null;

			return getMultiBlockPattern().getStateForPlacement(this, blockState, ctx);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
		{
			var pattern = getMultiBlockPattern();

			if(!pattern.canSurvive(this, level, pos, blockState))
				return false;

			return super.canSurvive(blockState, level, pos);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
		{
			getMultiBlockPattern().onPlace(this, blockState, level, pos, oldBlockState);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
		{
			getMultiBlockPattern().onRemove(this, blockState, level, pos, newBlockState);
			super.onRemove(blockState, level, pos, newBlockState, isMoving);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		protected void registerProperties(NonnullConsumer<Property<?>> consumer)
		{
			super.registerProperties(consumer);
			getMultiBlockPattern().registerProperties(consumer);
		}
		// endregion
	}

	public static abstract class WithContainer<BLOCK_ENTITY extends BlockEntity, CONTAINER extends AbstractContainerMenu> extends BaseBlock.WithContainer<BLOCK_ENTITY, CONTAINER> implements IMultiBlock
	{
		public WithContainer(Properties properties)
		{
			super(properties);
		}

		// region: IMultiBlock
		@Override
		public final Block asBlock()
		{
			return super.asBlock();
		}

		@Nullable
		@Override
		protected BlockState modifyPlacementState(BlockState placementBlockState, BlockPlaceContext ctx)
		{
			var blockState = super.modifyPlacementState(placementBlockState, ctx);

			if(blockState == null)
				return null;

			return getMultiBlockPattern().getStateForPlacement(this, blockState, ctx);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
		{
			var pattern = getMultiBlockPattern();

			if(!pattern.canSurvive(this, level, pos, blockState))
				return false;

			return super.canSurvive(blockState, level, pos);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
		{
			getMultiBlockPattern().onPlace(this, blockState, level, pos, oldBlockState);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
		{
			getMultiBlockPattern().onRemove(this, blockState, level, pos, newBlockState);
			super.onRemove(blockState, level, pos, newBlockState, isMoving);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		protected void registerProperties(NonnullConsumer<Property<?>> consumer)
		{
			super.registerProperties(consumer);
			getMultiBlockPattern().registerProperties(consumer);
		}
		// endregion
	}
}
