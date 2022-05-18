package xyz.apex.forge.apexcore.lib.multiblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

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
	private final NonnullTriPredicate<IWorldReader, BlockPos, BlockState> placementPredicate;
	private final boolean placeSoundPerBlock;

	private MultiBlockPattern(Builder builder)
	{
		worldSpaceFromLocalSpace = builder.worldSpaceFromLocalSpace;
		originFromWorldSpace = builder.originFromWorldSpace;
		placementPredicate = builder.placementPredicate;
		placeSoundPerBlock = builder.placeSoundPerBlock;
		placementStateModifier = builder.placementStateModifier;

		ImmutableList.Builder<BlockPos> list = ImmutableList.builder();
		BlockPos.Mutable pos = new BlockPos.Mutable();

		for(int y = 0; y < builder.layers.size(); y++)
		{
			String[] patterns = builder.layers.get(y);
			pos.setY(y);

			for(int x = 0; x < patterns.length; x++)
			{
				String pattern = patterns[x];
				pos.setX(x);

				for(int z = 0; z < pattern.length(); z++)
				{
					char token = pattern.charAt(z);
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
	public BlockState getStateForPlacement(BlockItemUseContext ctx, BlockState defaultBlockState)
	{
		World level = ctx.getLevel();
		BlockPos origin = ctx.getClickedPos();

		for(BlockPos localSpace : localPositions)
		{
			BlockPos worldSpace = getWorldSpaceFromLocalSpace(defaultBlockState, origin, localSpace);
			BlockState blockState = level.getBlockState(worldSpace);

			if(placementPredicate.test(level, worldSpace, blockState))
				return defaultBlockState;
		}

		return null;
	}

	public boolean canSurvive(IWorldReader level, BlockPos pos, BlockState blockState)
	{
		int index = getIndex(blockState);
		BlockPos origin = getOriginFromWorldSpace(blockState, pos, localPositions.get(index));

		for(BlockPos localSpace : localPositions)
		{
			BlockPos worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);
			BlockState testBlockState = level.getBlockState(worldSpace);

			if(!placementPredicate.test(level, worldSpace, testBlockState))
				return false;
		}

		return true;
	}

	public void onPlace(BlockState blockState, World level, BlockPos origin, BlockState oldBlockState, boolean isMoving)
	{
		if(blockState.getValue(blockProperty) == INDEX_ORIGIN)
		{
			SoundType soundType = blockState.getSoundType(level, origin, null);
			SoundEvent placeSound = soundType.getPlaceSound();

			for(int i = 1; i < localPositions.size(); i++)
			{
				BlockPos localSpace = localPositions.get(i);
				BlockPos worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);
				BlockState placementBlockState = blockState.setValue(blockProperty, i);
				placementBlockState = getPlacementState(worldSpace, placementBlockState, i);

				level.setBlock(worldSpace, placementBlockState, 11);

				if(placeSoundPerBlock)
					level.playSound(null, worldSpace, placeSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
			}
		}
	}

	public void onRemove(BlockState blockState, World level, BlockPos pos, BlockState newBlockState, boolean isMoving)
	{
		Block block = blockState.getBlock();
		int index = blockState.getValue(blockProperty);
		BlockPos origin = getOriginFromWorldSpace(blockState, pos, localPositions.get(index));

		for(BlockPos localSpace : localPositions)
		{
			BlockPos worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);

			if(!worldSpace.equals(pos) && level.getBlockState(worldSpace).is(block))
				level.destroyBlock(worldSpace, false);
		}
	}

	public void rotate(BlockState blockState, IWorld level, BlockPos origin, Rotation rotation, BlockState rotatedBlockState)
	{
		int index = blockState.getValue(blockProperty);

		if(index != 0)
		{
			for(int i = 1; i < localPositions.size(); i++)
			{
				BlockPos localSpace = localPositions.get(i);
				BlockPos worldSpace = getWorldSpaceFromLocalSpace(rotatedBlockState, origin, localSpace);

				level.setBlock(worldSpace, rotatedBlockState.setValue(blockProperty, i), 3);
			}
		}
	}

	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(blockProperty);
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
			BlockPos newLocalSpace = worldSpaceFromLocalSpace.apply(blockState, localSpace);
			return origin.offset(newLocalSpace);
		}

		return origin.offset(localSpace);
	}

	public BlockPos getOriginFromWorldSpace(BlockState blockState, BlockPos worldSpace, BlockPos localSpace)
	{
		if(originFromWorldSpace != null)
		{
			BlockPos newLocalSpace = originFromWorldSpace.apply(blockState, localSpace);
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
		private NonnullTriPredicate<IWorldReader, BlockPos, BlockState> placementPredicate = (level, pos, blockState) -> blockState.getMaterial().isReplaceable();
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
				Direction facing = blockState.getValue(MultiBlockFourWay.FACING);

				if(facing == Direction.NORTH)
					return pos.rotate(Rotation.CLOCKWISE_90);
				else if(facing == Direction.SOUTH)
					return pos.rotate(Rotation.COUNTERCLOCKWISE_90);
				else if(facing == Direction.EAST)
					return pos.rotate(Rotation.CLOCKWISE_180);

				return pos;
			}).originFromWorldSpace((blockState, pos) -> {
				Direction facing = blockState.getValue(MultiBlockFourWay.FACING);

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

		public Builder placementPredicate(NonnullTriPredicate<IWorldReader, BlockPos, BlockState> placementPredicate)
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
