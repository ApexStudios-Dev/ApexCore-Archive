package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import com.google.common.base.Suppliers;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.Validate;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Block component which gives blocks 'FACING' properties.
 * <p>
 * Blocks using this component are given the 'FACING' {@link Property} marking them as facing a given {@link Direction}.
 * <p>
 * Defaults to using {@link BlockStateProperties#HORIZONTAL_FACING} property, but this can be overridden using the {@link #withFacingProperty(DirectionProperty, Supplier)} method.
 * <p>
 * Use {@link #withPlacementModifier(BiFunction)} to modify the {@link Direction} set when block is placed, defaults to {@link BlockPlaceContext#getHorizontalDirection()}.
 * <p>
 * Calling both {@link #withFacingProperty(DirectionProperty, Supplier)} and {@link #withPlacementModifier(BiFunction)} must be done during component registration only, calls outside
 * component registration will throw errors potentially crashing the game.
 */
public final class FacingBlockComponent extends BlockComponent
{
    public static final BlockComponentType<FacingBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "facing", FacingBlockComponent::new);

    private DirectionProperty facingProperty = BlockStateProperties.HORIZONTAL_FACING;
    private Supplier<Direction> defaultFacing = () -> Direction.NORTH;
    private BiFunction<FacingBlockComponent, BlockPlaceContext, Direction> placementModifier = (component, ctx) -> ctx.getHorizontalDirection();

    private FacingBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    /**
     * Sets custom direction property to be used by this component.
     *
     * @param facingProperty New direction property to be used.
     * @return This component instance.
     */
    public FacingBlockComponent withFacingProperty(DirectionProperty facingProperty, Supplier<Direction> defaultFacing)
    {
        Validate.isTrue(!registeredComponents());
        this.facingProperty = facingProperty;
        this.defaultFacing = Suppliers.memoize(defaultFacing::get);
        return this;
    }

    /**
     * Sets custom placement modifier to be used when block bound to this component is placed.
     *
     * @param placementModifier Custom modifier to be used.
     * @return This component instance.
     */
    public FacingBlockComponent withPlacementModifier(BiFunction<FacingBlockComponent, BlockPlaceContext, Direction> placementModifier)
    {
        Validate.isTrue(!registeredComponents());
        this.placementModifier = placementModifier;
        return this;
    }

    /**
     * Returns true if block state has facing property.
     *
     * @param blockState Block state to validate.
     * @return True if block state has facing property.
     */
    public boolean hasProperty(BlockState blockState)
    {
        return blockState.hasProperty(facingProperty);
    }

    /**
     * Returns direction block state is facing.
     *
     * @param blockState Block state to get facing for.
     * @return Direction block state is facing.
     */
    public Direction getFacing(BlockState blockState)
    {
        // returns currently set value if property exists for given block state
        // if its invalid uses our default facing value instead
        return blockState.getOptionalValue(facingProperty).orElseGet(defaultFacing);
    }

    /**
     * Set direction block state is facing.
     *
     * @param blockState Block state to set facing for.
     * @param facing     Facing direction to be set.
     * @return Block state after setting facing direction.
     */
    public BlockState setFacing(BlockState blockState, Direction facing)
    {
        // block state does not have property or property does not have given value
        // return block state with no changes applied
        if(!blockState.hasProperty(facingProperty) || !facingProperty.getPossibleValues().contains(facing))
            return blockState;
        // facing property & value must be valid if we get here
        return blockState.setValue(facingProperty, facing);
    }

    @Override
    protected BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext ctx)
    {
        return setFacing(placementBlockState, placementModifier.apply(this, ctx));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(facingProperty);
    }

    @Override
    protected BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return setFacing(defaultBlockState, defaultFacing.get());
    }

    @Override
    protected BlockState rotate(BlockState blockState, Rotation rotation)
    {
        if(!blockState.hasProperty(facingProperty)) return blockState;
        return setFacing(blockState, rotation.rotate(blockState.getValue(facingProperty)));
    }

    @Override
    protected BlockState mirror(BlockState blockState, Mirror mirror)
    {
        if(!hasProperty(blockState)) return blockState;
        return blockState.rotate(mirror.getRotation(blockState.getValue(facingProperty)));
    }
}
