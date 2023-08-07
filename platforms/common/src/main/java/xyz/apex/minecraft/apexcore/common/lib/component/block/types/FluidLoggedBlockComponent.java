package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

import java.util.Optional;
import java.util.function.Supplier;

public final class FluidLoggedBlockComponent extends BaseBlockComponent implements SimpleWaterloggedBlock
{
    public static final BlockComponentType<FluidLoggedBlockComponent> WATER = forFluid(ApexCore.ID, "water_logged", () -> Fluids.WATER, () -> Fluids.FLOWING_WATER, BlockStateProperties.WATERLOGGED);
    public static final BlockComponentType<FluidLoggedBlockComponent> LAVA = forFluid(ApexCore.ID, "lava_logged", () -> Fluids.LAVA, () -> Fluids.LAVA, BooleanProperty.create("lavalogged"));

    private final Supplier<Fluid> fluidType;
    private final Supplier<FlowingFluid> flowingFluidType;
    private final BooleanProperty property;

    private FluidLoggedBlockComponent(BlockComponentHolder componentHolder, Supplier<Fluid> fluidType, Supplier<FlowingFluid> flowingFluidType, BooleanProperty property)
    {
        super(componentHolder);

        this.fluidType = fluidType;
        this.flowingFluidType = flowingFluidType;
        this.property = property;
    }

    public boolean isFor(Fluid fluid)
    {
        return fluid.isSame(fluidType.get()) || fluid.isSame(flowingFluidType.get());
    }

    public boolean isFor(FluidState fluidState)
    {
        return fluidState.is(fluidType.get()) || fluidState.is(flowingFluidType.get());
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return defaultBlockState.setValue(property, false);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(property);
    }

    @Override
    public BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext context)
    {
        var fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return placementBlockState.setValue(property, isFor(fluidState));
    }

    @Nullable
    @Override
    public FluidState getFluidState(BlockState blockState)
    {
        return blockState.getValue(property) ? flowingFluidType.get().getSource(false) : null;
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState blockState, Fluid fluid)
    {
        if(!isFor(fluid))
            return false;
        if(blockState.getValue(property))
            return false;
        return blockState.getFluidState().isEmpty();
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState blockState, FluidState fluidState)
    {
        if(!isFor(fluidState))
            return false;
        if(blockState.getValue(property))
            return false;
        if(!blockState.getFluidState().isEmpty())
            return false;

        if(!level.isClientSide())
        {
            level.setBlock(pos, blockState.setValue(property, true), Block.UPDATE_ALL);
            var fluid = fluidState.getType();
            level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
        }

        return true;
    }

    @Override
    public ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState blockState)
    {
        if(!blockState.getValue(property))
            return ItemStack.EMPTY;

        var newBlockState = blockState.setValue(property, false);
        level.setBlock(pos, newBlockState, Block.UPDATE_ALL);

        if(!newBlockState.canSurvive(level, pos))
            level.destroyBlock(pos, true);

        return fluidType.get().getBucket().getDefaultInstance();
    }

    @Override
    public Optional<SoundEvent> getPickupSound()
    {
        return fluidType.get().getPickupSound().or(() -> flowingFluidType.get().getPickupSound());
    }

    public static BlockComponentType<FluidLoggedBlockComponent> forFluid(String ownerId, String componentName, Supplier<Fluid> fluidType, Supplier<FlowingFluid> flowingFluidType, BooleanProperty fluidLoggedProperty)
    {
        return BlockComponentType.register(ownerId, componentName, componentHolder -> new FluidLoggedBlockComponent(componentHolder, fluidType, flowingFluidType, fluidLoggedProperty));
    }
}
