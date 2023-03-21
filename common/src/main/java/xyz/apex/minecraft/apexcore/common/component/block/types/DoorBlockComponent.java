package xyz.apex.minecraft.apexcore.common.component.block.types;

import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentType;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentTypes;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class DoorBlockComponent extends BaseBlockComponent
{
    public static final BlockComponentType<DoorBlockComponent> COMPONENT_TYPE = BlockComponentType.register(new ResourceLocation(ApexCore.ID, "door"), DoorBlockComponent::new, BlockComponentTypes.HORIZONTAL_FACING);

    public static final DirectionProperty FACING = HorizontalFacingBlockComponent.FACING;
    public static final BooleanProperty OPEN = DoorBlock.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = DoorBlock.HINGE;
    public static final BooleanProperty POWERED = DoorBlock.POWERED;
    public static final EnumProperty<DoubleBlockHalf> HALF = DoorBlock.HALF;

    private Supplier<SoundEvent> openSound = Suppliers.memoize(() -> SoundEvents.WOODEN_DOOR_OPEN);
    private Supplier<SoundEvent> closeSound = Suppliers.memoize(() -> SoundEvents.WOODEN_DOOR_CLOSE);

    private DoorBlockComponent(BlockComponentHolder holder)
    {
        super(holder);
    }

    public DoorBlockComponent setSounds(Supplier<SoundEvent> openSound, Supplier<SoundEvent> closeSound)
    {
        return setOpenSound(openSound).setCloseSound(closeSound);
    }

    public DoorBlockComponent setOpenSound(Supplier<SoundEvent> openSound)
    {
        this.openSound = Suppliers.memoize(openSound::get);
        return this;
    }

    public DoorBlockComponent setCloseSound(Supplier<SoundEvent> closeSound)
    {
        this.closeSound = Suppliers.memoize(closeSound::get);
        return this;
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState blockState)
    {
        return blockState.setValue(OPEN, false).setValue(HINGE, DoorHingeSide.LEFT).setValue(POWERED, false).setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void createBlockStateDefinition(Consumer<Property<?>> consumer)
    {
        consumer.accept(OPEN);
        consumer.accept(HINGE);
        consumer.accept(POWERED);
        consumer.accept(HALF);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx, BlockState blockState)
    {
        var pos = ctx.getClickedPos();
        var level = ctx.getLevel();

        if(pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(ctx)) return getPlacementState(holder, ctx, blockState);
        return null;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(blockState.getMaterial() != Material.METAL)
        {
            var isOpen = blockState.getValue(OPEN);
            setOpen(player, level, blockState, pos, !isOpen);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        var doubleBlockHalf = blockState.getValue(HALF);

        if(direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP))
        {
            if(neighborState.is(toBlock()) && neighborState.getValue(HALF) != doubleBlockHalf) return blockState.setValue(OPEN, neighborState.getValue(OPEN)).setValue(HINGE, neighborState.getValue(HINGE)).setValue(POWERED, neighborState.getValue(POWERED));
            return Blocks.AIR.defaultBlockState();
        }

        if(doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(level, currentPos)) return Blocks.AIR.defaultBlockState();
        return blockState;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        if(hasComponent(BlockComponentTypes.MULTI_BLOCK)) return;
        if(!level.isClientSide && player.isCreative()) DoublePlantBlock.preventCreativeDropFromBottomPart(level, pos, blockState, player);
    }

    @Override
    public boolean isPathFindable(BlockState blockState, BlockGetter level, BlockPos pos, PathComputationType pathComputationType)
    {
        return switch(pathComputationType) {
            case LAND, AIR -> blockState.getValue(OPEN);
            case WATER -> false;
        };
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(hasComponent(BlockComponentTypes.MULTI_BLOCK)) return;
        level.setBlock(pos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        var open = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.relative(blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));

        if(!blockState.is(block) && open != blockState.getValue(POWERED))
        {
            if(open != blockState.getValue(OPEN))
            {
                playSound(null, level, pos, open);
                level.gameEvent(null, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }

            level.setBlock(pos, blockState.setValue(POWERED, open).setValue(OPEN, open), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        var belowPos = pos.below();
        var bewlowBlockState = level.getBlockState(belowPos);
        return blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? bewlowBlockState.isFaceSturdy(level, belowPos, Direction.UP) : bewlowBlockState.is(toBlock());
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState)
    {
        return PushReaction.DESTROY;
    }

    @Override
    public long getSeed(BlockState blockState, BlockPos pos)
    {
        return Mth.getSeed(pos.getX(), pos.below(blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    private void setOpen(@Nullable Entity entity, Level level, BlockState blockState, BlockPos pos, boolean open)
    {
        if(blockState.is(toBlock()) && blockState.getValue(OPEN) != open)
        {
            level.setBlock(pos, blockState.setValue(OPEN, open), 10);
            playSound(entity, level, pos, open);
            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
    }

    private void playSound(@Nullable Entity source, Level level, BlockPos pos, boolean isOpening)
    {
        level.playSound(source, pos, isOpening ? openSound.get() : closeSound.get(), SoundSource.BLOCKS, 1F, level.random.nextFloat() * .1F + .9F);
    }

    public static BlockState getPlacementState(BlockComponentHolder door, BlockPlaceContext ctx, BlockState blockState)
    {
        var level = ctx.getLevel();
        var pos = ctx.getClickedPos();
        var open = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        return blockState.setValue(HINGE, getHinge(door, ctx)).setValue(POWERED, open).setValue(OPEN, open).setValue(HALF, DoubleBlockHalf.LOWER);
    }

    public static DoorHingeSide getHinge(BlockComponentHolder door, BlockPlaceContext ctx)
    {
        var level = ctx.getLevel();
        var pos = ctx.getClickedPos();
        var facing = ctx.getHorizontalDirection();
        var abovePos = pos.above();
        var ccwFacing = facing.getCounterClockWise();
        var ccwPos = pos.relative(ccwFacing);
        var ccwBlockState = level.getBlockState(ccwPos);
        var ccwAbovePos = abovePos.relative(ccwFacing);
        var ccwAboveBlockState = level.getBlockState(ccwAbovePos);
        var cwFacing = facing.getClockWise();
        var cwPos = pos.relative(cwFacing);
        var cwBlockState = level.getBlockState(cwPos);
        var cwAbovePos = abovePos.relative(cwFacing);
        var cwAboveBlockState = level.getBlockState(cwAbovePos);

        var i = (ccwBlockState.isCollisionShapeFullBlock(level, ccwPos) ? -1 : 0) +
                (ccwAboveBlockState.isCollisionShapeFullBlock(level, ccwAbovePos) ? -1 : 0) +
                (cwBlockState.isCollisionShapeFullBlock(level, cwPos) ? 1 : 0) +
                (cwAboveBlockState.isCollisionShapeFullBlock(level, cwAbovePos) ? 1 : 0)
        ;

        var block = door.toBlock();
        var ccwIsLowerDoor = ccwBlockState.is(block) && ccwBlockState.getValue(HALF) == DoubleBlockHalf.LOWER;
        var cwIsLowerDoor = cwBlockState.is(block) && cwBlockState.getValue(HALF) == DoubleBlockHalf.LOWER;

        if((!ccwIsLowerDoor || cwIsLowerDoor) && i <= 0)
        {
            if((!cwIsLowerDoor || ccwIsLowerDoor) && i >= 0)
            {
                var xOff = facing.getStepX();
                var zOff = facing.getStepZ();
                var clickPos = ctx.getClickLocation();
                var clickX = clickPos.x - pos.getX();
                var clickZ = clickPos.z - pos.getZ();
                return (xOff >= 0D || !(clickZ < .5D)) && (xOff <= 0D || !(clickZ > .5D)) && (zOff >= 0D || !(clickX > .5D)) && (zOff <= 0D || !(clickX < .5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            }

            return DoorHingeSide.LEFT;
        }

        return DoorHingeSide.RIGHT;
    }

}
