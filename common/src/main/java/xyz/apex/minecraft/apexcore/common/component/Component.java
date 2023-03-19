package xyz.apex.minecraft.apexcore.common.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface Component
{
    // region: Component
    @Nullable
    default <T extends Component> T getComponent(ComponentType<T> componentType)
    {
        return getBlock().getComponent(componentType);
    }

    default <T extends Component> Optional<T> getOptionalComponent(ComponentType<T> componentType)
    {
        return getBlock().getOptionalComponent(componentType);
    }

    default <T extends Component> T getRequiredComponent(ComponentType<T> componentType)
    {
        return getBlock().getRequiredComponent(componentType);
    }

    default Set<ResourceLocation> getComponentTypes()
    {
        return getBlock().getComponentTypes();
    }

    default Collection<Component> getComponents()
    {
        return getBlock().getComponents();
    }

    default <T extends Component> boolean hasComponent(ComponentType<T> componentType)
    {
        return getBlock().hasComponent(componentType);
    }

    ComponentBlock getBlock();

    default void validate() {}
    // endregion

    // region: Block
    default InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return InteractionResult.PASS;
    }

    default BlockState registerDefaultBlockState(BlockState blockState)
    {
        return blockState;
    }

    @Nullable
    default BlockState getStateForPlacement(BlockPlaceContext ctx, BlockState blockState)
    {
        return blockState;
    }

    default boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        return true;
    }

    default void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving) {}

    default void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving) {}

    default void createBlockStateDefinition(Consumer<Property<?>> consumer) { }

    @Nullable
    default RenderShape getRenderShape(BlockState blockState)
    {
        return null;
    }

    default void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack) { }

    default boolean hasAnalogOutputSignal(BlockState blockState)
    {
        return false;
    }

    default int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        return 0;
    }

    default BlockState rotate(BlockState blockState, Rotation rotation)
    {
        return blockState;
    }


    default BlockState mirror(BlockState blockState, Mirror mirror)
    {
        return blockState;
    }

    default BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        return blockState;
    }

    default void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) { }

    default void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) { }

    default boolean isPathFindable(BlockState blockState, BlockGetter level, BlockPos pos, PathComputationType pathComputationType)
    {
        return true;
    }

    @Nullable
    default PushReaction getPistonPushReaction(BlockState blockState)
    {
        return null;
    }

    default long getSeed(BlockState blockState, BlockPos pos)
    {
        return -1L;
    }
    // endregion
}
