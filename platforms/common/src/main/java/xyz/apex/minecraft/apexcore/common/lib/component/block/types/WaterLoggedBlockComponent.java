package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

public final class WaterLoggedBlockComponent extends BaseBlockComponent implements SimpleWaterloggedBlock
{
    public static final BlockComponentType<WaterLoggedBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "water_logged", WaterLoggedBlockComponent::new);

    private WaterLoggedBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return defaultBlockState.setValue(BlockStateProperties.WATERLOGGED, false);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext context)
    {
        var fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return placementBlockState.setValue(BlockStateProperties.WATERLOGGED, fluidState.is(Fluids.WATER));
    }

    @Nullable
    @Override
    public FluidState getFluidState(BlockState blockState)
    {
        return blockState.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : null;
    }
}
