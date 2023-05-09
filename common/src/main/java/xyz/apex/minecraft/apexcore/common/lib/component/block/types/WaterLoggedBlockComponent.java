package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

import java.util.Optional;

/**
 * Component used to give blocks water logging capabilities.
 */
public final class WaterLoggedBlockComponent extends BlockComponent implements SimpleWaterloggedBlock
{
    public static final BlockComponentType<WaterLoggedBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "water_logged", WaterLoggedBlockComponent::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private WaterLoggedBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    /**
     * Returns true if given block state supports water logging and is water logged.
     *
     * @param blockState Block state to validate.
     * @return True if given block state supports water logging and is water logged.
     */
    public boolean isWaterLogged(BlockState blockState)
    {
        return blockState.getOptionalValue(WATERLOGGED).orElse(false);
    }

    /**
     * Returns true if block state supports water logging.
     *
     * @param blockState Block state to validate.
     * @return True if block state supports water logging.
     */
    public boolean hasProperty(BlockState blockState)
    {
        return blockState.hasProperty(WATERLOGGED);
    }

    /**
     * Marks block state as being water logged or not, if it supports water logging.
     *
     * @param blockState  Block state to validate.
     * @param waterLogged True to mark block state as being water logged.
     * @return Block state after being marked as being water logged or not.
     */
    public BlockState setWaterLogged(BlockState blockState, boolean waterLogged)
    {
        return hasProperty(blockState) ? blockState.setValue(WATERLOGGED, waterLogged) : blockState;
    }

    @Override
    protected Optional<FluidState> getFluidState(BlockState blockState)
    {
        return isWaterLogged(blockState) ? Optional.of(Fluids.WATER.getSource(false)) : super.getFluidState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(WATERLOGGED);
    }

    @Override
    protected BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return setWaterLogged(defaultBlockState, false);
    }

    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborBlockState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        if(isWaterLogged(blockState)) level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(blockState, direction, neighborBlockState, level, currentPos, neighborPos);
    }

    @Override
    protected BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext ctx)
    {
        var isWaterLogged = ctx.getLevel().isWaterAt(ctx.getClickedPos());
        return setWaterLogged(placementBlockState, isWaterLogged);
    }
}
