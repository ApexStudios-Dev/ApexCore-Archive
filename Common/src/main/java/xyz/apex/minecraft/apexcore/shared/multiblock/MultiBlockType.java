package xyz.apex.minecraft.apexcore.shared.multiblock;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import xyz.apex.minecraft.apexcore.shared.util.function.QuadFunction;
import xyz.apex.minecraft.apexcore.shared.util.function.QuadPredicate;

import java.util.List;
import java.util.function.Consumer;

public final class MultiBlockType
{
    public static final int ORIGIN_INDEX = 0;

    private final int width;
    private final int height;
    private final int depth;
    private final List<BlockPos> localPositions;
    private final IntegerProperty blockProperty;
    private final QuadFunction<MultiBlock, MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace;
    private final QuadFunction<MultiBlockType, BlockPos, BlockState, Integer, BlockState> placementStateModifier;
    private final QuadPredicate<MultiBlock, LevelReader, BlockPos, BlockState> placementPredicate;
    private final boolean placeSoundPerBlock;

    private MultiBlockType(Builder builder)
    {
        width = builder.width;
        height = builder.height;
        depth = builder.depth;
        rotateLocalSpace = builder.rotateLocalSpace;
        placementStateModifier = builder.placementStateModifier;
        placementPredicate = builder.placementPredicate;
        placeSoundPerBlock = builder.placeSoundPerBlock;

        var positions = ImmutableList.<BlockPos>builder();

        for(var x = 0; x < width; x++)
        {
            for(var z = 0; z < depth; z++)
            {
                for(var y = 0; y < height; y++)
                {
                    positions.add(new BlockPos(x, y, z));
                }
            }
        }

        localPositions = positions.build();
        blockProperty = IntegerProperty.create("multiblock_index", 0, localPositions.size());
    }

    private BlockState modifyBlockStateForPlacement(BlockState blockState, int index, BlockPos pos)
    {
        var result = setIndex(blockState, index);
        return placementStateModifier.apply(this, pos, result, index);
    }

    @Nullable
    public BlockState getStateForPlacement(MultiBlock multiBlock, BlockState placementBlockState, BlockPlaceContext ctx)
    {
        var level = ctx.getLevel();
        var origin = ctx.getClickedPos();
        var result = placementBlockState;

        for(var i = 0; i < localPositions.size(); i++)
        {
            var localSpace = localPositions.get(i);
            var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, placementBlockState, origin, localSpace);
            var testBlockstate = modifyBlockStateForPlacement(placementBlockState, i, worldSpace);
            var worldBlockState = level.getBlockState(worldSpace);

            if(!passesPlacementTests(multiBlock, level, worldSpace, testBlockstate, worldBlockState)) return null;
            if(origin.equals(worldSpace)) result = testBlockstate;
        }

        return result;
    }

    public boolean canSurvive(MultiBlock multiBlock, LevelReader level, BlockPos pos, BlockState blockState)
    {
        if(!multiBlock.isSameBlockTypeForMultiBlock(blockState)) return false;

        if(isOrigin(blockState))
        {
            var worldBlockState = level.getBlockState(pos);
            return passesPlacementTests(multiBlock, level, pos, blockState, worldBlockState);
        }

        // delegate all other locations back to origin point
        // if origin point can exist, so can all the others
        // override canSurvive in your block type
        // if specific positions need specific survival checks
        var origin = getOriginPos(multiBlock, blockState, pos);
        return level.getBlockState(origin).canSurvive(level, origin);
    }

    public void onPlace(MultiBlock multiBlock, BlockState blockState, Level level, BlockPos origin, BlockState oldBlockState)
    {
        if(!multiBlock.isSameBlockTypeForMultiBlock(oldBlockState) && isOrigin(blockState))
        {
            var soundType = blockState.getSoundType();
            var placeSound = soundType.getPlaceSound();

            for(var i = 1; i < localPositions.size(); i++)
            {
                var localSpace = localPositions.get(i);
                var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, blockState, origin, localSpace);
                var placementBlockState = modifyBlockStateForPlacement(blockState, i, worldSpace);
                level.setBlock(worldSpace, placementBlockState, Block.UPDATE_ALL_IMMEDIATE);
                if(placeSoundPerBlock) level.playSound(null, worldSpace, placeSound, SoundSource.BLOCKS, (soundType.volume + 1F) / 2F, soundType.pitch * .8F);
            }
        }
    }

    public void onRemove(MultiBlock multiBlock, BlockState blockState, Level level, BlockPos pos, BlockState newBlockState)
    {
        if(!multiBlock.isSameBlockTypeForMultiBlock(blockState) || !multiBlock.isSameBlockTypeForMultiBlock(newBlockState))
        {
            var index = getIndex(blockState);
            var origin = getOriginFromWorldSpace(multiBlock, blockState, pos, localPositions.get(index));

            for(var localSpace : localPositions)
            {
                var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, blockState, origin, localSpace);
                if(worldSpace.equals(pos)) continue;
                if(!multiBlock.isSameBlockTypeForMultiBlock(level.getBlockState(worldSpace))) continue;
                level.destroyBlock(worldSpace, false);
            }
        }
    }

    public void registerBlockProperty(Consumer<Property<?>> consumer)
    {
        consumer.accept(blockProperty);
    }

    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return setIndex(defaultBlockState, ORIGIN_INDEX);
    }

    public boolean passesPlacementTests(MultiBlock multiBlock, LevelReader level, BlockPos pos, BlockState multiBlockState, BlockState worldBlockState)
    {
        // if(multiBlock.isSameBlockTypeForMultiBlock(worldBlockState)) return true;

        if(!worldBlockState.getMaterial().isReplaceable()) return false;
        if(!placementPredicate.test(multiBlock, level, pos, multiBlockState)) return false;
        return true;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getDepth()
    {
        return depth;
    }

    public IntegerProperty getBlockProperty()
    {
        return blockProperty;
    }

    public List<BlockPos> getLocalPositions()
    {
        return localPositions;
    }

    public int getIndex(BlockState blockState)
    {
        return blockState.getValue(blockProperty);
    }

    public BlockState setIndex(BlockState blockState, int index)
    {
        return blockState.setValue(blockProperty, index);
    }

    public boolean isOrigin(BlockState blockState)
    {
        return getIndex(blockState) == ORIGIN_INDEX;
    }

    public BlockPos getWorldSpaceFromLocalSpace(MultiBlock multiBlock, BlockState blockState, BlockPos origin, BlockPos localSpace)
    {
        var rotated = rotateLocalSpace.apply(multiBlock, this, blockState, localSpace);
        return origin.offset(rotated);
    }

    public BlockPos getOriginFromWorldSpace(MultiBlock multiBlock, BlockState blockState, BlockPos origin, BlockPos localSpace)
    {
        var rotated = rotateLocalSpace.apply(multiBlock, this, blockState, localSpace);
        return origin.subtract(rotated);
    }

    public BlockPos getOriginPos(MultiBlock multiBlock, BlockState blockState, BlockPos worldSpace)
    {
        var index = getIndex(blockState);
        var localSpace = localPositions.get(index);
        return getOriginFromWorldSpace(multiBlock, blockState, worldSpace, localSpace);
    }

    public Builder copy()
    {
        return builder(width, height, depth).copy(this);
    }

    public static Builder builder(int width, int height, int depth)
    {
        return new Builder(width, height, depth);
    }

    public static MultiBlockType simple(int width, int height, int depth)
    {
        return builder(width, height, depth).build();
    }

    private static BlockPos rotateForFacing(BlockState blockState, BlockPos pos, DirectionProperty facingProperty)
    {
        return switch(blockState.getValue(facingProperty)) {
            case NORTH -> pos.rotate(Rotation.CLOCKWISE_90);
            case SOUTH -> pos.rotate(Rotation.COUNTERCLOCKWISE_90);
            case EAST -> pos.rotate(Rotation.CLOCKWISE_180);
            default -> pos;
        };
    }

    public static final class Builder
    {
        private final int width;
        private final int height;
        private final int depth;
        private QuadFunction<MultiBlock, MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace = (multiBlock, multiBlockType, blockState, pos) -> pos;
        private QuadFunction<MultiBlockType, BlockPos, BlockState, Integer, BlockState> placementStateModifier = (multiBlockType, pos, blockState, integer) -> blockState;
        private QuadPredicate<MultiBlock, LevelReader, BlockPos, BlockState> placementPredicate = (multiBlock, levelReader, pos, blockState) -> true;
        private boolean placeSoundPerBlock = false;

        private Builder(int width, int height, int depth)
        {
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        public Builder copy(Builder other)
        {
            if(other.placeSoundPerBlock) placeSoundPerBlock();
            return rotateLocalSpace(other.rotateLocalSpace)
                    .placementStateModifier(other.placementStateModifier)
                    .placementPredicate(other.placementPredicate);
        }

        public Builder copy(MultiBlockType multiBlockType)
        {
            if(multiBlockType.placeSoundPerBlock) placeSoundPerBlock();
            return rotateLocalSpace(multiBlockType.rotateLocalSpace)
                    .placementStateModifier(multiBlockType.placementStateModifier)
                    .placementPredicate(multiBlockType.placementPredicate);
        }

        public Builder rotateLocalSpace(QuadFunction<MultiBlock, MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace)
        {
            this.rotateLocalSpace = rotateLocalSpace;
            return this;
        }

        public Builder rotateLocalSpaceForFacing(DirectionProperty facingProperty)
        {
            return rotateLocalSpace((multiBlock, multiBlockType, blockState, pos) -> MultiBlockType.rotateForFacing(blockState, pos, facingProperty));
        }

        public Builder placementStateModifier(QuadFunction<MultiBlockType, BlockPos, BlockState, Integer, BlockState> placementStateModifier)
        {
            this.placementStateModifier = placementStateModifier;
            return this;
        }

        public Builder placementPredicate(QuadPredicate<MultiBlock, LevelReader, BlockPos, BlockState> placementPredicate)
        {
            this.placementPredicate = placementPredicate;
            return this;
        }

        public Builder placeSoundPerBlock()
        {
            placeSoundPerBlock = true;
            return this;
        }

        public MultiBlockType build()
        {
            return new MultiBlockType(this);
        }
    }
}
