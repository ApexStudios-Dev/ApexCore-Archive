package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

public final class HorizontalFacingBlockComponent extends BaseBlockComponent
{
    public static final BlockComponentType<HorizontalFacingBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "horizontal_facing", HorizontalFacingBlockComponent::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private HorizontalFacingBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return defaultBlockState.setValue(FACING, Direction.NORTH);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext context)
    {
        return placementBlockState.setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation)
    {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror)
    {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }
}
