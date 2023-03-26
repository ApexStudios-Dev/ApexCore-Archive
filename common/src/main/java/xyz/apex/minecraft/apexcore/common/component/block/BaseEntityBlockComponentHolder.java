package xyz.apex.minecraft.apexcore.common.component.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.component.entity.types.MenuProviderBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.multiblock.MultiBlockComponent;
import xyz.apex.minecraft.apexcore.common.util.ExtendedBlockEntityTicker;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class BaseEntityBlockComponentHolder<T extends BlockEntity & BlockEntityComponentHolder> extends BaseBlockComponentHolder implements EntityBlock, ExtendedBlockEntityTicker
        // see BaseBlockEntityComponentHolder as to why these are implemented
        , WorldlyContainerHolder
{
    private final Function<BlockEntity, Listener> listener = Util.memoize(Listener::create);
    private final BiFunction<T, Boolean, BlockEntityTicker<T>> ticker = Util.memoize(Ticker::create);

    protected BaseEntityBlockComponentHolder(Properties properties)
    {
        super(properties);
    }

    protected abstract BlockEntityType<T> getBlockEntityType();

    public final Optional<T> getBlockEntity(BlockState blockState, BlockGetter level, BlockPos pos)
    {
        return getBlockEntity(getBlockEntityType(), blockState, level, pos);
    }

    public final Optional<T> getBlockEntity(BlockGetter level, BlockPos pos)
    {
        return getBlockEntity(getBlockEntityType(), level, pos);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        var blockEntity = getBlockEntity(blockState, level, pos).orElse(null);

        if(blockEntity != null)
        {
            var result = MenuProviderBlockEntityComponent.tryOpen(blockEntity, player, hand);
            if(result.consumesAction()) return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(blockState, level, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public T newBlockEntity(BlockPos pos, BlockState blockState)
    {
        var isOriginPoint = getOptionalComponent(BlockComponentTypes.MULTI_BLOCK).map(MultiBlockComponent::getMultiBlockType).map(multiBlockType -> multiBlockType.isOrigin(blockState)).orElse(true);
        if(!isOriginPoint) return null;
        return getBlockEntityType().create(pos, blockState);
    }

    @Override
    public final boolean triggerEvent(BlockState blockState, Level level, BlockPos pos, int eventId, int eventParam)
    {
        return getBlockEntity(blockState, level, pos).map(blockEntity -> blockEntity.triggerEvent(eventId, eventParam)).orElse(false);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        return getBlockEntity(blockState, level, pos).filter(MenuProvider.class::isInstance).map(MenuProvider.class::cast).orElse(null);
    }

    @Nullable
    @Override
    public final <O extends BlockEntity> BlockEntityTicker<O> getTicker(Level level, BlockState state, BlockEntityType<O> blockEntityType)
    {
        // NOOP - We use mixins to create ticker based on block entity state
        // createBlockEntityTicker is used instead of this method
        // but this method is still invoked from vanilla code
        // returning a value will mess with our logic
        // thus this method is finalized to ensure it never returns anything
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <O extends BlockEntity> BlockEntityTicker<O> createBlockEntityTicker(Level level, O blockEntity)
    {
        // T & O should equal each other, we check block entity type in the mixin
        return (BlockEntityTicker<O>) ticker.apply((T) blockEntity, level.isClientSide);
    }

    // returns only if block entity has component that is a listener
    @Nullable
    @Override
    public final <O extends BlockEntity> GameEventListener getListener(ServerLevel level, O blockEntity)
    {
        return listener.apply(blockEntity);
    }

    @Override
    public final WorldlyContainer getContainer(BlockState blockState, LevelAccessor level, BlockPos pos)
    {
        return getBlockEntity(blockState, level, pos).flatMap(blockEntity -> blockEntity.getOptionalComponent(BlockEntityComponentTypes.CONTAINER)).orElse(null);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, blockState, placer, stack);
        if(!stack.hasCustomHoverName()) return;
        getBlockEntity(blockState, level, pos).flatMap(blockEntity -> blockEntity.getOptionalComponent(BlockEntityComponentTypes.NAMEABLE)).ifPresent(nameable -> nameable.setCustomName(stack.getDisplayName()));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        if(!blockState.is(newBlockState.getBlock()))
        {
            getBlockEntity(blockState, level, pos).flatMap(blockEntity -> blockEntity.getOptionalComponent(BlockEntityComponentTypes.CONTAINER)).ifPresent(container -> {
                if(level instanceof ServerLevel serverLevel) Containers.dropContents(serverLevel, pos, container);
                level.updateNeighbourForOutputSignal(pos, blockState.getBlock());
            });
        }
        super.onRemove(blockState, level, pos, newBlockState, isMoving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState)
    {
        return true; // should be based on block entity components
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        return getBlockEntity(blockState, level, pos).flatMap(blockEntity -> blockEntity.getOptionalComponent(BlockEntityComponentTypes.CONTAINER)).map(AbstractContainerMenu::getRedstoneSignalFromContainer).orElse(0);
    }

    public static <T extends BlockEntity> Optional<T> getBlockEntity(BlockEntityType<T> blockEntityType, BlockState blockState, BlockGetter level, BlockPos pos)
    {
        if(!blockEntityType.isValid(blockState)) return Optional.empty();

        if(blockState.getBlock() instanceof BlockComponentHolder holder)
        {
            var multiBlockComponent = holder.getComponent(BlockComponentTypes.MULTI_BLOCK);

            if(multiBlockComponent != null)
            {
                var multiBlockType = multiBlockComponent.getMultiBlockType();

                if(!multiBlockType.isValidBlock(blockState)) return Optional.empty();

                if(!multiBlockType.isOrigin(blockState))
                {
                    var originPos = multiBlockType.getOriginPos(blockState, pos);
                    var originBlockState = level.getBlockState(originPos);
                    return getBlockEntity(blockEntityType, originBlockState, level, originPos);
                }
            }
        }

        return level.getBlockEntity(pos, blockEntityType);
    }

    public static <T extends BlockEntity> Optional<T> getBlockEntity(BlockEntityType<T> blockEntityType, BlockGetter level, BlockPos pos)
    {
        return getBlockEntity(blockEntityType, level.getBlockState(pos), level, pos);
    }

    @Nullable
    public static BlockEntity getBlockEntityUnsafe(BlockState blockState, BlockGetter level, BlockPos pos)
    {
        if(blockState.getBlock() instanceof BlockComponentHolder holder)
        {
            var multiBlockComponent = holder.getComponent(BlockComponentTypes.MULTI_BLOCK);

            if(multiBlockComponent != null)
            {
                var multiBlockType = multiBlockComponent.getMultiBlockType();

                if(!multiBlockType.isValidBlock(blockState)) return null;

                if(!multiBlockType.isOrigin(blockState))
                {
                    var originPos = multiBlockType.getOriginPos(blockState, pos);
                    var originBlockState = level.getBlockState(originPos);
                    return getBlockEntityUnsafe(originBlockState, level, originPos);
                }
            }
        }

        return level.getBlockEntity(pos);
    }

    public static BlockEntity getBlockEntityUnsafe(BlockGetter level, BlockPos pos)
    {
        return getBlockEntityUnsafe(level.getBlockState(pos), level, pos);
    }

    private static final class Listener implements GameEventListener
    {
        private final BlockEntityComponent.Listener[] listeners;
        private final BlockPositionSource position;

        private Listener(BlockPos pos, BlockEntityComponent.Listener[] listeners)
        {
            this.listeners = listeners;
            position = new BlockPositionSource(pos);
        }

        @Override
        public PositionSource getListenerSource()
        {
            return position;
        }

        @Override
        public int getListenerRadius()
        {
            return Stream.of(listeners).mapToInt(BlockEntityComponent.Listener::listenerRadius).max().orElse(0);
        }

        @Override
        public boolean handleGameEvent(ServerLevel level, GameEvent gameEvent, GameEvent.Context ctx, Vec3 pos)
        {
            return Stream.of(listeners).anyMatch(listener -> listener.handleGameEvent(level, gameEvent, ctx, pos));
        }

        @Nullable
        private static Listener create(BlockEntity blockEntity)
        {
            if(!(blockEntity instanceof BlockEntityComponentHolder holder)) return null;

            var listeners = holder.components().filter(BlockEntityComponent.Listener.class::isInstance).map(BlockEntityComponent.Listener.class::cast).toArray(BlockEntityComponent.Listener[]::new);
            if(listeners.length == 0) return null;

            return new Listener(blockEntity.getBlockPos(), listeners);
        }
    }

    private record Ticker<T extends BlockEntity>(Runnable[] tickers) implements BlockEntityTicker<T>
    {
        @Override
        public void tick(Level level, BlockPos pos, BlockState blockState, T blockEntity)
        {
            for(var ticker : tickers)
            {
                ticker.run();
            }
        }

        @Nullable
        private static <T extends BlockEntity> Ticker<T> create(BlockEntity blockEntity, boolean isClientSide)
        {
            if(!(blockEntity instanceof BlockEntityComponentHolder holder)) return null;

            var tickers = holder.components()
                    .filter(BlockEntityComponent.Ticker.class::isInstance)
                    .map(BlockEntityComponent.Ticker.class::cast)
                    .map(ticker -> ticker.createTicker(isClientSide))
                    .filter(Objects::nonNull)
                    .toArray(Runnable[]::new);

            if(tickers.length == 0) return null;
            return new Ticker(tickers);
        }
    }
}
