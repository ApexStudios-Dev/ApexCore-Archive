package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public non-sealed class BaseBlockComponent implements BlockComponent
{
    protected final BlockComponentHolder componentHolder;
    private boolean registered = false;

    protected BaseBlockComponent(BlockComponentHolder componentHolder)
    {
        this.componentHolder = componentHolder;
    }

    // region: Components
    @DoNotCall
    @ApiStatus.Internal
    void postRegistration()
    {
        registered = true;
    }

    @Nullable
    @Override
    public final <C extends BlockComponent> C getComponent(BlockComponentType<C> componentType)
    {
        return componentHolder.getComponent(componentType);
    }

    @Override
    public final <C extends BlockComponent> Optional<C> findComponent(BlockComponentType<C> componentType)
    {
        return componentHolder.findComponent(componentType);
    }

    @Override
    public final <C extends BlockComponent> C getRequiredComponent(BlockComponentType<C> componentType)
    {
        return componentHolder.getRequiredComponent(componentType);
    }

    @Override
    public final boolean hasComponent(BlockComponentType<?> componentType)
    {
        return componentHolder.hasComponent(componentType);
    }

    @Override
    public final Set<BlockComponentType<?>> getComponentTypes()
    {
        return componentHolder.getComponentTypes();
    }

    @Override
    public final Collection<BlockComponent> getComponents()
    {
        return componentHolder.getComponents();
    }

    @Override
    public final Block getGameObject()
    {
        return componentHolder.getGameObject();
    }

    @Override
    public final BlockComponentHolder getComponentHolder()
    {
        return componentHolder;
    }

    protected final boolean isRegistered()
    {
        return registered;
    }
    // endregion

    // region: Events
    @Override
    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return defaultBlockState;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext context)
    {
        return placementBlockState;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack tool)
    {
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborBlockState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        return blockState;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return null;
    }

    @Nullable
    @Override
    public FluidState getFluidState(BlockState blockState)
    {
        return null;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation)
    {
        return blockState;
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror)
    {
        return blockState;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext useContext)
    {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState blockState)
    {
        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState)
    {
        return false;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        return 0;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction direction)
    {
        return 0;
    }
    // endregion
}
