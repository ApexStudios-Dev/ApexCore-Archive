package xyz.apex.minecraft.apexcore.common.component.block.types;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentType;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class HorizontalFacingBlockComponent extends BaseBlockComponent
{
    public static final BlockComponentType<HorizontalFacingBlockComponent> COMPONENT_TYPE = BlockComponentType.register(
            new ResourceLocation(ApexCore.ID, "horizontal_facing"),
            HorizontalFacingBlockComponent::new
    );

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private UnaryOperator<Direction> getFacingDirection = Direction::getOpposite;

    private HorizontalFacingBlockComponent(BlockComponentHolder holder)
    {
        super(holder);
    }

    public HorizontalFacingBlockComponent setGetFacingDirectionFunc(UnaryOperator<Direction> getFacingDirection)
    {
        this.getFacingDirection = getFacingDirection;
        return this;
    }

    public Direction getFacingDirection(BlockPlaceContext ctx)
    {
        return getFacingDirection.apply(ctx.getHorizontalDirection());
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState blockState)
    {
        return blockState.setValue(FACING, Direction.NORTH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx, BlockState blockState)
    {
        return blockState.setValue(FACING, getFacingDirection(ctx));
    }

    @Override
    public void createBlockStateDefinition(Consumer<Property<?>> consumer)
    {
        consumer.accept(FACING);
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
