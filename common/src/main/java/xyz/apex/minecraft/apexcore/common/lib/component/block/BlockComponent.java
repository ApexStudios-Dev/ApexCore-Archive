package xyz.apex.minecraft.apexcore.common.lib.component.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.BaseComponent;

import java.util.Optional;

/**
 * Base implementation for block components.
 */
public class BlockComponent extends BaseComponent<Block, BlockComponentHolder>
{
    protected BlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    @Nullable
    protected BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext ctx)
    {
        return placementBlockState;
    }

    protected void playerDestroy(Level level, Player player, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack tool)
    {
    }

    protected void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
    }

    protected void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
    }

    protected BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return defaultBlockState;
    }

    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborBlockState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        return blockState;
    }

    protected boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        return true;
    }

    protected void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
    }

    protected void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
    }

    protected void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
    }

    protected InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        return InteractionResult.PASS;
    }

    protected Optional<FluidState> getFluidState(BlockState blockState)
    {
        return Optional.empty();
    }

    protected BlockState rotate(BlockState blockState, Rotation rotation)
    {
        return blockState;
    }

    protected BlockState mirror(BlockState blockState, Mirror mirror)
    {
        return blockState;
    }
}
