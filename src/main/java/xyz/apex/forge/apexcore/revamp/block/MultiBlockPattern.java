package xyz.apex.forge.apexcore.revamp.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import xyz.apex.java.utility.nullness.NonnullConsumer;
import xyz.apex.java.utility.nullness.NonnullQuadFunction;
import xyz.apex.java.utility.nullness.NonnullQuadPredicate;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public final class MultiBlockPattern
{
	public static final int INDEX_ORIGIN = 0;

	private final List<BlockPos> localPositions;
	private final IntegerProperty blockProperty;
	private final NonnullQuadFunction<IMultiBlock, MultiBlockPattern, BlockState, BlockPos, BlockPos> worldSpaceFromLocalSpace;
	private final NonnullQuadFunction<IMultiBlock, MultiBlockPattern, BlockState, BlockPos, BlockPos> originFromWorldSpace;
	private final NonnullQuadFunction<MultiBlockPattern, BlockPos, BlockState, Integer, BlockState> placementStateModifier;
	private final NonnullQuadPredicate<IMultiBlock, LevelReader, BlockPos, BlockState> placementPredicate;
	private final boolean placeSoundPerBlock;

	private MultiBlockPattern(MultiBlockPattern.Builder builder)
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
	BlockState getStateForPlacement(IMultiBlock multiBlock, BlockState placementBlockState, BlockPlaceContext ctx)
	{
		if(placementBlockState.hasProperty(blockProperty))
		{
			var level = ctx.getLevel();
			var origin = ctx.getClickedPos();

			for(var localSpace : localPositions)
			{
				var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, placementBlockState, origin, localSpace);
				var blockState = level.getBlockState(worldSpace);

				if(passesPlacementTests(multiBlock, level, worldSpace, blockState))
					return placementBlockState;
			}

			return null;
		}

		return placementBlockState;
	}

	boolean canSurvive(IMultiBlock multiBlock, LevelReader level, BlockPos pos, BlockState blockState)
	{
		if(blockState.hasProperty(blockProperty))
		{
			var index = getIndex(blockState);
			var origin = getOriginFromWorldSpace(multiBlock, blockState, pos, localPositions.get(index));

			for(var localSpace : localPositions)
			{
				var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, blockState, origin, localSpace);
				var testBlockState = level.getBlockState(worldSpace);

				if(!passesPlacementTests(multiBlock, level, worldSpace, testBlockState))
					return false;
			}
		}

		return true;
	}

	void onPlace(IMultiBlock multiBlock, BlockState blockState, Level level, BlockPos origin, BlockState oldBlockState)
	{
		if(blockState.hasProperty(blockProperty))
		{
			if(!oldBlockState.is(multiBlock.asBlock()) && blockState.getValue(blockProperty) == INDEX_ORIGIN)
			{
				var soundType = blockState.getSoundType(level, origin, null);
				var placeSound = soundType.getPlaceSound();

				for(var i = 1; i < localPositions.size(); i++)
				{
					var localSpace = localPositions.get(i);
					var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, blockState, origin, localSpace);
					var placementBlockState = blockState.setValue(blockProperty, i);
					placementBlockState = placementStateModifier.apply(this, worldSpace, placementBlockState, i);

					level.setBlock(worldSpace, placementBlockState, 11);

					if(placeSoundPerBlock)
						level.playSound(null, worldSpace, placeSound, SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
				}
			}
		}
	}

	void onRemove(IMultiBlock multiBlock, BlockState blockState, Level level, BlockPos pos, BlockState newBlockState)
	{
		if(blockState.hasProperty(blockProperty))
		{
			var block = multiBlock.asBlock();

			// current or new block is now required multi block type
			if(!blockState.is(block) || !newBlockState.is(block))
			{
				var index = blockState.getValue(blockProperty);
				var origin = getOriginFromWorldSpace(multiBlock, blockState, pos, localPositions.get(index));

				for(var localSpace : localPositions)
				{
					var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, blockState, origin, localSpace);

					if(!worldSpace.equals(pos) && level.getBlockState(worldSpace).is(block))
						level.destroyBlock(worldSpace, false);
				}
			}
		}
	}

	void registerProperties(NonnullConsumer<Property<?>> consumer)
	{
		consumer.accept(blockProperty);
	}

	BlockState registerDefaultState(BlockState state)
	{
		return state.hasProperty(blockProperty) ? state.setValue(blockProperty, INDEX_ORIGIN) : state;
	}

	private boolean passesPlacementTests(IMultiBlock multiBlock, LevelReader level, BlockPos pos, BlockState blockState)
	{
		if(blockState.hasProperty(blockProperty))
		{
			if(!blockState.is(multiBlock.asBlock()))
			{
				if(!blockState.getMaterial().isReplaceable())
					return false;
			}

			return placementPredicate.test(multiBlock, level, pos, blockState);
		}

		return true;
	}
	// endregion

	// region: Helpers
	public int getIndex(BlockState blockState)
	{
		return blockState.getOptionalValue(blockProperty).orElse(INDEX_ORIGIN);
	}

	public boolean isOrigin(BlockState blockState)
	{
		return getIndex(blockState) == INDEX_ORIGIN;
	}

	public BlockPos getWorldSpaceFromLocalSpace(IMultiBlock multiBlock, BlockState blockState, BlockPos origin, BlockPos localSpace)
	{
		if(blockState.hasProperty(blockProperty))
		{
			var newLocalSpace = worldSpaceFromLocalSpace.apply(multiBlock, this, blockState, localSpace);
			return origin.offset(newLocalSpace);
		}

		return origin;
	}

	public BlockPos getOriginFromWorldSpace(IMultiBlock multiBlock, BlockState blockState, BlockPos worldSpace, BlockPos localSpace)
	{
		if(blockState.hasProperty(blockProperty))
		{
			var newLocalSpace = originFromWorldSpace.apply(multiBlock, this, blockState, localSpace);
			return worldSpace.subtract(newLocalSpace);
		}

		return worldSpace;
	}

	public List<BlockPos> getLocalPositions()
	{
		return localPositions;
	}
	// endregion

	public static MultiBlockPattern.Builder builder()
	{
		return new MultiBlockPattern.Builder();
	}

	public static BlockPos getMultiBlockWorldSpaceFromLocalSpace(IMultiBlock multiBlock, MultiBlockPattern pattern, BlockState blockState, BlockPos pos)
	{
		if(blockState.hasProperty(pattern.blockProperty))
		{
			if(BaseBlock.supportsFacing(blockState))
			{
				return switch(BaseBlock.getFacing(blockState))
				{
					case NORTH -> pos.rotate(Rotation.CLOCKWISE_90);
					case SOUTH -> pos.rotate(Rotation.COUNTERCLOCKWISE_90);
					case EAST -> pos.rotate(Rotation.CLOCKWISE_180);
					default -> pos;
				};
			}
		}

		return pos;
	}

	public static BlockPos getMultiBlockOriginFromWorldSpace(IMultiBlock multiBlock, MultiBlockPattern pattern, BlockState blockState, BlockPos pos)
	{
		if(blockState.hasProperty(pattern.blockProperty))
		{
			if(BaseBlock.supportsFacing(blockState))
			{
				return switch(BaseBlock.getFacing(blockState))
				{
					case NORTH -> pos.rotate(Rotation.CLOCKWISE_90);
					case SOUTH -> pos.rotate(Rotation.COUNTERCLOCKWISE_90);
					case EAST -> pos.rotate(Rotation.CLOCKWISE_180);
					default -> pos;
				};
			}
		}

		return pos;
	}

	public static final class Builder
	{
		private final List<String[]> layers = Lists.newArrayList();
		private NonnullQuadFunction<IMultiBlock, MultiBlockPattern, BlockState, BlockPos, BlockPos> worldSpaceFromLocalSpace = MultiBlockPattern::getMultiBlockWorldSpaceFromLocalSpace;
		private NonnullQuadFunction<IMultiBlock, MultiBlockPattern, BlockState, BlockPos, BlockPos> originFromWorldSpace = MultiBlockPattern::getMultiBlockOriginFromWorldSpace;
		private NonnullQuadFunction<MultiBlockPattern, BlockPos, BlockState, Integer, BlockState> placementStateModifier = (pattern, pos, blockState, integer) -> blockState;
		private NonnullQuadPredicate<IMultiBlock, LevelReader, BlockPos, BlockState> placementPredicate = (multiBlock, level, pos, blockState) -> true;
		private boolean placeSoundPerBlock = false;

		private Builder()
		{
		}

		public MultiBlockPattern.Builder layer(String... layers)
		{
			Collections.addAll(this.layers, layers);
			return this;
		}

		public MultiBlockPattern.Builder worldSpaceFromLocalSpace(NonnullQuadFunction<IMultiBlock, MultiBlockPattern, BlockState, BlockPos, BlockPos> worldSpaceFromLocalSpace)
		{
			this.worldSpaceFromLocalSpace = worldSpaceFromLocalSpace;
			return this;
		}

		public MultiBlockPattern.Builder originFromWorldSpace(NonnullQuadFunction<IMultiBlock, MultiBlockPattern, BlockState, BlockPos, BlockPos> originFromWorldSpace)
		{
			this.originFromWorldSpace = originFromWorldSpace;
			return this;
		}

		public MultiBlockPattern.Builder placementStateModifier(NonnullQuadFunction<MultiBlockPattern, BlockPos, BlockState, Integer, BlockState> placementStateModifier)
		{
			this.placementStateModifier = placementStateModifier;
			return this;
		}

		public MultiBlockPattern.Builder placementPredicate(NonnullQuadPredicate<IMultiBlock, LevelReader, BlockPos, BlockState> placementPredicate)
		{
			this.placementPredicate = placementPredicate;
			return this;
		}

		public MultiBlockPattern.Builder placeSoundPerBlock()
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
