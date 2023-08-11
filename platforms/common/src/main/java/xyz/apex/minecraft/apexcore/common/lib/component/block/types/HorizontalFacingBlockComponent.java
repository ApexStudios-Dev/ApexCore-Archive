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
import org.apache.commons.lang3.Validate;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

import java.util.function.Function;

public final class HorizontalFacingBlockComponent extends BaseBlockComponent
{
    public static final BlockComponentType<HorizontalFacingBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "horizontal_facing", HorizontalFacingBlockComponent::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private Direction defaultFacing = Direction.NORTH;
    private Function<BlockPlaceContext, Direction> placementDirection = context -> context.getHorizontalDirection().getOpposite();

    private HorizontalFacingBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    public HorizontalFacingBlockComponent withDefaultFacing(Direction defaultFacing)
    {
        Validate.isTrue(!isRegistered(), "Can only change default facing during registration");
        this.defaultFacing = defaultFacing;
        return this;
    }

    public HorizontalFacingBlockComponent withPlacementDirection(Function<BlockPlaceContext, Direction> placementDirection)
    {
        Validate.isTrue(!isRegistered(), "Can only change default facing during registration");
        this.placementDirection = placementDirection;
        return this;
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return defaultBlockState.setValue(FACING, defaultFacing);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext context)
    {
        return placementBlockState.setValue(FACING, placementDirection.apply(context));
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
