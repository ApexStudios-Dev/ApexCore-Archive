package xyz.apex.minecraft.apexcore.common.multiblock;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import xyz.apex.minecraft.apexcore.common.component.ComponentBlock;
import xyz.apex.minecraft.apexcore.common.util.function.QuadFunction;
import xyz.apex.minecraft.apexcore.common.util.function.QuadPredicate;
import xyz.apex.minecraft.apexcore.common.util.function.TriFunction;

import java.util.List;

public final class MultiBlockPattern
{
    public static final int ORIGIN_INDEX = 0;
    private static final Int2ObjectMap<IntegerProperty> BLOCK_STATE_PROPERTIES = new Int2ObjectOpenHashMap<>();

    private final int width;
    private final int height;
    private final int depth;
    private final List<BlockPos> localPositions;
    private final IntegerProperty blockProperty;
    final TriFunction<MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace;
    final QuadFunction<MultiBlockType, BlockPos, BlockState, Integer, BlockState> placementStateModifier;
    final QuadPredicate<MultiBlockType, LevelReader, BlockPos, BlockState> placementPredicate;
    private final boolean placeSoundPerBlock;

    private MultiBlockPattern(Builder builder)
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
        blockProperty = getBlockStateProperty(localPositions.size());
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

    public List<BlockPos> getLocalPositions()
    {
        return localPositions;
    }

    public IntegerProperty getBlockProperty()
    {
        return blockProperty;
    }

    public boolean shouldPlaceSoundPerBlock()
    {
        return placeSoundPerBlock;
    }

    public MultiBlockType finalizeFor(ComponentBlock owner)
    {
        return new MultiBlockType(owner, this);
    }

    public Builder copy()
    {
        return builder(width, height, depth).copy(this);
    }

    public static Builder builder(int width, int height, int depth)
    {
        return new Builder(width, height, depth);
    }

    public static MultiBlockPattern simple(int width, int height, int depth)
    {
        return builder(width, height, depth).build();
    }

    public static IntegerProperty getBlockStateProperty(int maxIndices)
    {
        return BLOCK_STATE_PROPERTIES.computeIfAbsent(maxIndices, $ -> IntegerProperty.create("multiblock_index", 0, maxIndices));
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
        private TriFunction<MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace = (multiBlockType, blockState, pos) -> pos;
        private QuadFunction<MultiBlockType, BlockPos, BlockState, Integer, BlockState> placementStateModifier = (multiBlockType, pos, blockState, integer) -> blockState;
        private QuadPredicate<MultiBlockType, LevelReader, BlockPos, BlockState> placementPredicate = (multiBlockType, levelReader, pos, blockState) -> true;
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

        public Builder copy(MultiBlockPattern multiBlockPattern)
        {
            placeSoundPerBlock = multiBlockPattern.placeSoundPerBlock;

            return rotateLocalSpace(multiBlockPattern.rotateLocalSpace)
                    .placementStateModifier(multiBlockPattern.placementStateModifier)
                    .placementPredicate(multiBlockPattern.placementPredicate);
        }

        public Builder rotateLocalSpace(TriFunction<MultiBlockType, BlockState, BlockPos, BlockPos> rotateLocalSpace)
        {
            this.rotateLocalSpace = rotateLocalSpace;
            return this;
        }

        public Builder rotateLocalSpaceForFacing(DirectionProperty facingProperty)
        {
            return rotateLocalSpace((multiBlockType, blockState, pos) -> rotateForFacing(blockState, pos, facingProperty));
        }

        public Builder placementStateModifier(QuadFunction<MultiBlockType, BlockPos, BlockState, Integer, BlockState> placementStateModifier)
        {
            this.placementStateModifier = placementStateModifier;
            return this;
        }

        public Builder placementPredicate(QuadPredicate<MultiBlockType, LevelReader, BlockPos, BlockState> placementPredicate)
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
