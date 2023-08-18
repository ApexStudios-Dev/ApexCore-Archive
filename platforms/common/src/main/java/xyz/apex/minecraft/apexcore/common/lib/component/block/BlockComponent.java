package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.ForOverride;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public sealed interface BlockComponent permits BaseBlockComponent
{
    // region: Components
    @Nullable
    <C extends BlockComponent> C getComponent(BlockComponentType<C> componentType);

    <C extends BlockComponent> Optional<C> findComponent(BlockComponentType<C> componentType);

    <C extends BlockComponent> C getRequiredComponent(BlockComponentType<C> componentType);

    boolean hasComponent(BlockComponentType<?> componentType);

    Set<BlockComponentType<?>> getComponentTypes();

    Collection<BlockComponent> getComponents();

    Block getGameObject();

    @Nullable
    BlockEntity getBlockEntity(BlockGetter level, BlockPos pos, BlockState blockState);

    @Nullable
    <T extends BlockEntity> T getBlockEntity(BlockEntityType<T> blockEntityType, BlockGetter level, BlockPos pos, BlockState blockState);

    BlockComponentHolder getComponentHolder();
    // endregion

    // region: Events
    @DoNotCall
    @ForOverride
    BlockState registerDefaultBlockState(BlockState defaultBlockState);

    @DoNotCall
    @ForOverride
    @Nullable
    BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext context);

    @DoNotCall
    @ForOverride
    void playerDestroy(Level level, Player player, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack tool);

    @DoNotCall
    @ForOverride
    void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack);

    @DoNotCall
    @ForOverride
    void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player);

    @DoNotCall
    @ForOverride
    void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);

    @DoNotCall
    @ForOverride
    BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborBlockState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos);

    @DoNotCall
    @ForOverride
    void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving);

    @DoNotCall
    @ForOverride
    void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving);

    @DoNotCall
    @ForOverride
    void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving);

    @DoNotCall
    @ForOverride
    InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit);

    @Nullable
    @DoNotCall
    @ForOverride
    RenderShape getRenderShape(BlockState blockState);

    @Nullable
    @DoNotCall
    @ForOverride
    FluidState getFluidState(BlockState blockState);

    @DoNotCall
    @ForOverride
    BlockState rotate(BlockState blockState, Rotation rotation);

    @DoNotCall
    @ForOverride
    BlockState mirror(BlockState blockState, Mirror mirror);

    @DoNotCall
    @ForOverride
    boolean canBeReplaced(BlockState blockState, BlockPlaceContext useContext);

    @DoNotCall
    @ForOverride
    boolean canBeReplaced(BlockState blockState, Fluid fluid);

    @DoNotCall
    @ForOverride
    boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos);

    @DoNotCall
    @ForOverride
    boolean isSignalSource(BlockState blockState);

    @DoNotCall
    @ForOverride
    boolean hasAnalogOutputSignal(BlockState blockState);

    @DoNotCall
    @ForOverride
    int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos);

    @DoNotCall
    @ForOverride
    int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction direction);

    @DoNotCall
    @ForOverride
    @Nullable
    MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos);
    // endregion
}
