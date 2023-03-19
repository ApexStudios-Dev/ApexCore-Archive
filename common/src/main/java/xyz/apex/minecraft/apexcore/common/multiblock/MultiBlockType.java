package xyz.apex.minecraft.apexcore.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.component.ComponentBlock;

import java.util.List;
import java.util.function.Consumer;

public class MultiBlockType
{
    private final ComponentBlock owner;
    private final MultiBlockPattern pattern;

    MultiBlockType(ComponentBlock owner, MultiBlockPattern pattern)
    {
        this.owner = owner;
        this.pattern = pattern;
    }

    private BlockState modifyBlockStateForPlacement(BlockState blockState, int index, BlockPos pos)
    {
        var result = setIndex(blockState, index);
        return pattern.placementStateModifier.apply(this, pos, result, index);
    }

    public boolean isValidBlock(BlockState blockState)
    {
        return blockState.is(owner.toBlock());
    }

    @Nullable
    public BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext ctx)
    {
        var level = ctx.getLevel();
        var origin = ctx.getClickedPos();
        var result = placementBlockState;
        var localPositions = pattern.getLocalPositions();

        for(var i = 0; i < localPositions.size(); i++)
        {
            var localSpace = localPositions.get(i);
            var worldSpace = getWorldSpaceFromLocalSpace(placementBlockState, origin, localSpace);
            var testBlockstate = modifyBlockStateForPlacement(placementBlockState, i, worldSpace);
            var worldBlockState = level.getBlockState(worldSpace);

            if(!passesPlacementTests(level, worldSpace, testBlockstate, worldBlockState)) return null;
            if(origin.equals(worldSpace)) result = testBlockstate;
        }

        return result;
    }

    public boolean canSurvive(LevelReader level, BlockPos pos, BlockState blockState)
    {
        if(!isValidBlock(blockState)) return false;

        if(isOrigin(blockState))
        {
            var worldBlockState = level.getBlockState(pos);
            return passesPlacementTests(level, pos, blockState, worldBlockState);
        }

        // delegate all other locations back to origin point
        // if origin point can exist, so can all the others
        // override canSurvive in your block type
        // if specific positions need specific survival checks
        var origin = getOriginPos(blockState, pos);
        return level.getBlockState(origin).canSurvive(level, origin);
    }

    public void onPlace(BlockState blockState, Level level, BlockPos origin, BlockState oldBlockState)
    {
        if(!isValidBlock(oldBlockState) && isOrigin(blockState))
        {
            var soundType = blockState.getSoundType();
            var placeSound = soundType.getPlaceSound();
            var localPositions = pattern.getLocalPositions();

            for(var i = 1; i < localPositions.size(); i++)
            {
                var localSpace = localPositions.get(i);
                var worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);
                var placementBlockState = modifyBlockStateForPlacement(blockState, i, worldSpace);
                level.setBlock(worldSpace, placementBlockState, Block.UPDATE_ALL_IMMEDIATE);
                if(pattern.shouldPlaceSoundPerBlock()) level.playSound(null, worldSpace, placeSound, SoundSource.BLOCKS, (soundType.volume + 1F) / 2F, soundType.pitch * .8F);
            }
        }
    }

    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState)
    {
        if(!isValidBlock(blockState) || !isValidBlock(newBlockState))
        {
            var index = getIndex(blockState);
            var localPositions = pattern.getLocalPositions();
            var origin = getOriginFromWorldSpace(blockState, pos, localPositions.get(index));

            for(var localSpace : localPositions)
            {
                var worldSpace = getWorldSpaceFromLocalSpace(blockState, origin, localSpace);
                if(worldSpace.equals(pos)) continue;
                if(!isValidBlock(level.getBlockState(worldSpace))) continue;
                level.destroyBlock(worldSpace, false);
            }
        }
    }

    public void registerBlockProperty(Consumer<Property<?>> consumer)
    {
        consumer.accept(pattern.getBlockProperty());
    }

    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return setIndex(defaultBlockState, MultiBlockPattern.ORIGIN_INDEX);
    }

    public boolean passesPlacementTests(LevelReader level, BlockPos pos, BlockState multiBlockState, BlockState worldBlockState)
    {
        if(!worldBlockState.getMaterial().isReplaceable()) return false;
        if(!pattern.placementPredicate.test(this, level, pos, multiBlockState)) return false;
        return true;
    }

    public int getWidth()
    {
        return pattern.getWidth();
    }

    public int getHeight()
    {
        return pattern.getHeight();
    }

    public int getDepth()
    {
        return pattern.getDepth();
    }

    public IntegerProperty getBlockProperty()
    {
        return pattern.getBlockProperty();
    }

    public List<BlockPos> getLocalPositions()
    {
        return pattern.getLocalPositions();
    }

    public int getIndex(BlockState blockState)
    {
        return blockState.getValue(pattern.getBlockProperty());
    }

    public BlockState setIndex(BlockState blockState, int index)
    {
        return blockState.setValue(pattern.getBlockProperty(), index);
    }

    public boolean isOrigin(BlockState blockState)
    {
        return getIndex(blockState) == MultiBlockPattern.ORIGIN_INDEX;
    }

    public BlockPos getWorldSpaceFromLocalSpace(BlockState blockState, BlockPos origin, BlockPos localSpace)
    {
        var rotated = pattern.rotateLocalSpace.apply(this, blockState, localSpace);
        return origin.offset(rotated);
    }

    public BlockPos getOriginFromWorldSpace(BlockState blockState, BlockPos origin, BlockPos localSpace)
    {
        var rotated = pattern.rotateLocalSpace.apply(this, blockState, localSpace);
        return origin.subtract(rotated);
    }

    public BlockPos getOriginPos(BlockState blockState, BlockPos worldSpace)
    {
        var index = getIndex(blockState);
        var localSpace = pattern.getLocalPositions().get(index);
        return getOriginFromWorldSpace(blockState, worldSpace, localSpace);
    }
}
