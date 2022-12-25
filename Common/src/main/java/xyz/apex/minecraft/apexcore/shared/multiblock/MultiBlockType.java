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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
    private static final DirectionProperty FACING_PROPERTY = BlockStateProperties.FACING;

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

    @Nullable
    public BlockState getStateForPlacement(MultiBlock multiBlock, BlockState placementBlockState, BlockPlaceContext ctx)
    {
        var level = ctx.getLevel();
        var origin = ctx.getClickedPos();

        for(var i = 0; i < localPositions.size(); i++)
        {
            var localSpace = localPositions.get(i);
            var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, placementBlockState, origin, localSpace);
            var testBlockstate = setIndex(placementBlockState, i);
            var worldBlockState = level.getBlockState(worldSpace);

            if(!passesPlacementTests(multiBlock, level, worldSpace, testBlockstate, worldBlockState)) return null;
        }

        return placementBlockState;
    }

    public boolean canSurvive(MultiBlock multiBlock, LevelReader level, BlockPos pos, BlockState blockState)
    {
        var index = getIndex(blockState);
        var origin = getOriginFromWorldSpace(multiBlock, blockState, pos, localPositions.get(index));

        for(var i = 0; i < localPositions.size(); i++)
        {
            var localSpace = localPositions.get(i);
            var worldSpace = getWorldSpaceFromLocalSpace(multiBlock, blockState, origin, localSpace);
            var testBlockState = setIndex(blockState, i);
            var worldBlockState = level.getBlockState(worldSpace);

            if(!passesPlacementTests(multiBlock, level, worldSpace, testBlockState, worldBlockState)) return false;
        }

        return true;
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
                var placementBlockState = setIndex(blockState, i);
                placementBlockState = placementStateModifier.apply(this, worldSpace, placementBlockState, i);

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
        if(!worldBlockState.getMaterial().isReplaceable()) return false;
        if(placementPredicate.test(multiBlock, level, pos, multiBlockState)) return true;
        return false;
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

    public static Builder builder(int width, int height, int depth)
    {
        return new Builder(width, height, depth);
    }

    public static MultiBlockType simple(int width, int height, int depth)
    {
        return builder(width, height, depth).build();
    }

    private static BlockPos rotateLocalSpace(MultiBlock multiBlock, MultiBlockType multiBlockType, BlockState blockState, BlockPos pos)
    {
        if(blockState.hasProperty(FACING_PROPERTY))
        {
            return switch(blockState.getValue(FACING_PROPERTY)) {
                case NORTH -> pos.rotate(Rotation.CLOCKWISE_90);
                case SOUTH -> pos.rotate(Rotation.COUNTERCLOCKWISE_90);
                case EAST -> pos.rotate(Rotation.CLOCKWISE_180);
                default -> pos;
            };
        }

        return pos;
    }

    public static final class Builder
    {
        private final int width;
        private final int height;
        private final int depth;
        private QuadFunction<MultiBlock, MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace = MultiBlockType::rotateLocalSpace;
        private QuadFunction<MultiBlockType, BlockPos, BlockState, Integer, BlockState> placementStateModifier = (multiBlockType, pos, blockState, integer) -> blockState;
        private QuadPredicate<MultiBlock, LevelReader, BlockPos, BlockState> placementPredicate = (multiBlock, levelReader, pos, blockState) -> true;
        private boolean placeSoundPerBlock = false;

        private Builder(int width, int height, int depth)
        {
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        public Builder rotateLocalSpace(QuadFunction<MultiBlock, MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace)
        {
            this.rotateLocalSpace = rotateLocalSpace;
            return this;
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
