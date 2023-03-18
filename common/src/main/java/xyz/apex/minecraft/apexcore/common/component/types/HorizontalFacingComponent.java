package xyz.apex.minecraft.apexcore.common.component.types;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.ComponentBlock;
import xyz.apex.minecraft.apexcore.common.component.ComponentType;
import xyz.apex.minecraft.apexcore.common.component.SimpleComponent;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class HorizontalFacingComponent extends SimpleComponent
{
    public static final ComponentType<HorizontalFacingComponent> COMPONENT_TYPE = ComponentType.register(
            new ResourceLocation(ApexCore.ID, "horizontal_facing"),
            HorizontalFacingComponent.class
    );

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private UnaryOperator<Direction> getFacingDirection = Direction::getOpposite;

    @ApiStatus.Internal // public cause reflection
    public HorizontalFacingComponent(ComponentBlock block)
    {
        super(block);
    }

    public HorizontalFacingComponent setGetFacingDirectionFunc(UnaryOperator<Direction> getFacingDirection)
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
