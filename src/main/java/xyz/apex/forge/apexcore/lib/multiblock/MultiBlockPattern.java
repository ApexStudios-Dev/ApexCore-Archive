package xyz.apex.forge.apexcore.lib.multiblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import xyz.apex.java.utility.nullness.NonnullBiFunction;
import xyz.apex.java.utility.nullness.NonnullQuadFunction;
import xyz.apex.java.utility.nullness.NonnullTriPredicate;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public final class MultiBlockPattern
{
	public static final int INDEX_ORIGIN = 0;

	private final List<BlockPos> localPositions;
	private final IntegerProperty blockProperty;
	@Nullable private final NonnullBiFunction<BlockState, BlockPos, BlockPos> worldSpaceFromLocalSpace;
	@Nullable private final NonnullBiFunction<BlockState, BlockPos, BlockPos> originFromWorldSpace;
	@Nullable private final NonnullQuadFunction<MultiBlockPattern, BlockPos, BlockState, Integer, BlockState> placementStateModifier;
	@Nullable private final NonnullTriPredicate<LevelReader, BlockPos, BlockState> placementPredicate;
	private final boolean placeSoundPerBlock;

	private MultiBlockPattern(Builder builder)
	{
		worldSpaceFromLocalSpace = builder.worldSpaceFromLocalSpace;
		originFromWorldSpace = builder.originFromWorldSpace;
		placementPredicate = builder.placementPredicate;
		placeSoundPerBlock = builder.placeSoundPerBlock;
		placementStateModifier = builder.placementStateModifier;

		var list = ImmutableList.<BlockPos>builder();
		var pos = new BlockPos.MutableBlockPos();

		for(var y = 0; y < builder.layers.size(); y++)
		{
			var patterns = builder.layers.get(y);
			pos.setY(y);

			for(var x = 0; x < patterns.length; x++)
			{
				var pattern = patterns[x];
				pos.setX(x);

				for(var z = 0; z < pattern.length(); z++)
				{
					var token = pattern.charAt(z);
					pos.setZ(z);

					if(!Character.isWhitespace(token))
						list.add(pos.immutable());
				}
			}
		}

		localPositions = list.build();
		blockProperty = IntegerProperty.create("multi_block_index", 0, localPositions.size());
	}

	// region: Block Wrappers
	@Nullable
	public BlockState getStateForPlacement(MultiBlock multiBlock, BlockPlaceContext ctx, BlockState defaultBlockState)
	{
		var level = ctx.getLevel();
		var origin = ctx.getClickedPos();

		for(var localSpace : localPositions)
		{
			var worldSpace = getWorldSpaceFromLocalSpace(defaultBlockState, origin, localSpace);
			var blockState = level.getBlockState(worldSpace);

			if(passesPlacementTests(multiBlock, level, worldSpace, blockState))
				return defaultBlockState;
		}

		return null;
	}

	public boolean canSurvive(MultiBlock multiBlock, LevelReader level, BlockPos pos, BlockState blockState)
	{
		var index = getIndex(blockState);
		var origin = getOriginFromWorldSpace(blockState, pos, localPositions.get(index));

		for(var localSpace : localPositions)
		{
			var worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);
			var testBlockState = level.getBlockState(worldSpace);

			if(!passesPlacementTests(multiBlock, level, worldSpace, testBlockState))
				return false;
		}

		return true;
	}

	public void onPlace(BlockState blockState, Level level, BlockPos origin, BlockState oldBlockState, boolean isMoving)
	{
		if(!oldBlockState.is(blockState.getBlock()) && blockState.getValue(blockProperty) == INDEX_ORIGIN)
		{
			var soundType = blockState.getSoundType(level, origin, null);
			var placeSound = soundType.getPlaceSound();

			for(var i = 1; i < localPositions.size(); i++)
			{
				var localSpace = localPositions.get(i);
				var worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);
				var placementBlockState = blockState.setValue(blockProperty, i);
				placementBlockState = getPlacementState(worldSpace, placementBlockState, i);

				level.setBlock(worldSpace, placementBlockState, 11);

				if(placeSoundPerBlock)
					level.playSound(null, worldSpace, placeSound, SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
			}
		}
	}

	public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
	{
		var block = blockState.getBlock();
		var index = blockState.getValue(blockProperty);
		var origin = getOriginFromWorldSpace(blockState, pos, localPositions.get(index));

		for(var localSpace : localPositions)
		{
			var worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);

			if(!worldSpace.equals(pos) && level.getBlockState(worldSpace).is(block))
				level.destroyBlock(worldSpace, false);
		}
	}

	public void rotate(BlockState blockState, LevelAccessor level, BlockPos origin, Rotation rotation, BlockState rotatedBlockState)
	{
		var index = blockState.getValue(blockProperty);

		if(index != INDEX_ORIGIN)
		{
			for(var i = 1; i < localPositions.size(); i++)
			{
				var localSpace = localPositions.get(i);
				var worldSpace = getWorldSpaceFromLocalSpace(rotatedBlockState, origin, localSpace);

				level.setBlock(worldSpace, rotatedBlockState.setValue(blockProperty, i), 3);
			}
		}
	}

	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(blockProperty);
	}

	private boolean passesPlacementTests(MultiBlock multiBlock, LevelReader level, BlockPos pos, BlockState blockState)
	{
		if(!blockState.is(multiBlock))
		{
			if(!blockState.getMaterial().isReplaceable())
				return false;
		}

		if(placementPredicate != null)
			return placementPredicate.test(level, pos, blockState);
		return true;
	}

	BlockState registerDefaultState(BlockState state)
	{
		return state.setValue(blockProperty, 0);
	}
	// endregion

	// region: Helpers
	public int getIndex(BlockState blockState)
	{
		return blockState.getValue(blockProperty);
	}

	public boolean isOrigin(BlockState blockState)
	{
		return blockState.getOptionalValue(blockProperty).map(index -> index == INDEX_ORIGIN).orElse(false);
	}

	public BlockPos getWorldSpaceFromLocalSpace(BlockState blockState, BlockPos origin, BlockPos localSpace)
	{
		if(worldSpaceFromLocalSpace != null)
		{
			var newLocalSpace = worldSpaceFromLocalSpace.apply(blockState, localSpace);
			return origin.offset(newLocalSpace);
		}

		return origin.offset(localSpace);
	}

	public BlockPos getOriginFromWorldSpace(BlockState blockState, BlockPos worldSpace, BlockPos localSpace)
	{
		if(originFromWorldSpace != null)
		{
			var newLocalSpace = originFromWorldSpace.apply(blockState, localSpace);
			return worldSpace.subtract(newLocalSpace);
		}

		return worldSpace.subtract(localSpace);
	}

	public List<BlockPos> getLocalPositions()
	{
		return localPositions;
	}
	// endregion

	private BlockState getPlacementState(BlockPos pos, BlockState blockState, int index)
	{
		if(placementStateModifier != null)
			return placementStateModifier.apply(this, pos, blockState, index);

		return blockState;
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static final class Builder
	{
		private final List<String[]> layers = Lists.newArrayList();
		@Nullable private NonnullBiFunction<BlockState, BlockPos, BlockPos> worldSpaceFromLocalSpace;
		@Nullable private NonnullBiFunction<BlockState, BlockPos, BlockPos> originFromWorldSpace;
		@Nullable private NonnullQuadFunction<MultiBlockPattern, BlockPos, BlockState, Integer, BlockState> placementStateModifier;
		@Nullable private NonnullTriPredicate<LevelReader, BlockPos, BlockState> placementPredicate;
		private boolean placeSoundPerBlock = false;

		private Builder()
		{
		}

		public Builder layer(String... layers)
		{
			Collections.addAll(this.layers, layers);
			return this;
		}

		public Builder worldSpaceFromLocalSpace(NonnullBiFunction<BlockState, BlockPos, BlockPos> worldSpaceFromLocalSpace)
		{
			this.worldSpaceFromLocalSpace = worldSpaceFromLocalSpace;
			return this;
		}

		public Builder originFromWorldSpace(NonnullBiFunction<BlockState, BlockPos, BlockPos> originFromWorldSpace)
		{
			this.originFromWorldSpace = originFromWorldSpace;
			return this;
		}

		public Builder setSpacesFor4Way()
		{
			return worldSpaceFromLocalSpace((blockState, pos) -> {
				var facing = blockState.getValue(MultiBlockFourWay.FACING);

				if(facing == Direction.NORTH)
					return pos.rotate(Rotation.CLOCKWISE_90);
				else if(facing == Direction.SOUTH)
					return pos.rotate(Rotation.COUNTERCLOCKWISE_90);
				else if(facing == Direction.EAST)
					return pos.rotate(Rotation.CLOCKWISE_180);

				return pos;
			}).originFromWorldSpace((blockState, pos) -> {
				var facing = blockState.getValue(MultiBlockFourWay.FACING);

				if(facing == Direction.NORTH)
					return pos.rotate(Rotation.CLOCKWISE_90);
				else if(facing == Direction.SOUTH)
					return pos.rotate(Rotation.COUNTERCLOCKWISE_90);
				else if(facing == Direction.EAST)
					return pos.rotate(Rotation.CLOCKWISE_180);

				return pos;
			});
		}

		public Builder placementStateModifier(NonnullQuadFunction<MultiBlockPattern, BlockPos, BlockState, Integer, BlockState> placementStateModifier)
		{
			this.placementStateModifier = placementStateModifier;
			return this;
		}

		public Builder placementPredicate(NonnullTriPredicate<LevelReader, BlockPos, BlockState> placementPredicate)
		{
			this.placementPredicate = placementPredicate;
			return this;
		}

		public Builder placeSoundPerBlock()
		{
			placeSoundPerBlock = true;
			return this;
		}

		public MultiBlockPattern build()
		{
			return new MultiBlockPattern(this);
		}
	}
}
