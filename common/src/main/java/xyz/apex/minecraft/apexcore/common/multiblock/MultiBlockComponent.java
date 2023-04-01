package xyz.apex.minecraft.apexcore.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentType;

import java.util.Objects;
import java.util.function.Consumer;

public final class MultiBlockComponent extends BaseBlockComponent
{
    public static final BlockComponentType<MultiBlockComponent> COMPONENT_TYPE = BlockComponentType.register(
            new ResourceLocation(ApexCore.ID, "multiblock"),
            MultiBlockComponent::new
    );

    @Nullable private MultiBlockType multiBlockType;

    private MultiBlockComponent(BlockComponentHolder holder)
    {
        super(holder);
    }

    public MultiBlockType getMultiBlockType()
    {
        return Objects.requireNonNull(multiBlockType);
    }

    public void setMultiBlockType(MultiBlockType multiBlockType)
    {
        this.multiBlockType = multiBlockType;
    }

    public void setMultiBlockType(MultiBlockPattern multiBlockPattern)
    {
        setMultiBlockType(multiBlockPattern.finalizeFor(holder));
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState blockState)
    {
        if(multiBlockType == null) return super.registerDefaultBlockState(blockState);
        return multiBlockType.registerDefaultBlockState(blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx, BlockState blockState)
    {
        if(multiBlockType == null) return super.getStateForPlacement(ctx, blockState);
        return multiBlockType.getStateForPlacement(blockState, ctx);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        if(multiBlockType == null) return super.canSurvive(blockState, level, pos);
        return multiBlockType.canSurvive(level, pos, blockState);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
        if(multiBlockType != null) multiBlockType.onPlace(blockState, level, pos, oldBlockState);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        if(multiBlockType != null) multiBlockType.onRemove(blockState, level, pos, newBlockState);
    }

    @Override
    public void createBlockStateDefinition(Consumer<Property<?>> consumer)
    {
        if(multiBlockType != null) multiBlockType.registerBlockProperty(consumer);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        if(multiBlockType == null) return super.getRenderShape(blockState);
        return multiBlockType.isOrigin(blockState) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Override
    public void onRegistered(BlockComponentHolder.Registrar registrar)
    {
        Validate.notNull(multiBlockType, "Block: '%s' did not set a MultiBlockType!!".formatted(toBlock().getClass().getName()));
    }

    public static <P extends Comparable<P>> boolean setBlockStateForAll(Level level, BlockPos pos, BlockState blockState, Property<P> property, P value)
    {
        // only for actual multi blocks
        if(!(blockState.getBlock() instanceof BlockComponentHolder holder) || !holder.hasComponent(COMPONENT_TYPE)) return false;

        var component = holder.getRequiredComponent(COMPONENT_TYPE);
        Objects.requireNonNull(component.multiBlockType); // if someone fails to set the multiblock type, crash the game, this should *ALWAYS* be set
        // this should be true, but better to double check
        if(!component.multiBlockType.isValidBlock(blockState)) return false;

        // find origin point of multi block
        var originPos = component.multiBlockType.getOriginPos(blockState, pos);
        var flag = false;

        // set block state for all blocks within the multi block
        for(var localPos : component.multiBlockType.getLocalPositions())
        {
            var worldPos = component.multiBlockType.getWorldSpaceFromLocalSpace(blockState, originPos, localPos);
            var worldBlockState = level.getBlockState(worldPos);

            // if block in world is not valid skip it
            if(!component.multiBlockType.isValidBlock(worldBlockState)) continue;
            // if block does not have property skip it
            if(!worldBlockState.hasProperty(property)) continue;
            // if block already has this value skip it
            if(worldBlockState.getValue(property) == value) continue;

            flag = level.setBlock(worldPos, worldBlockState.setValue(property, value), Block.UPDATE_ALL) || flag;
        }

        return flag;
    }
}
