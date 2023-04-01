package xyz.apex.minecraft.apexcore.common.component.block;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.component.block.types.BedBlockComponent;
import xyz.apex.minecraft.apexcore.common.hooks.BlockHooks;
import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.platform.PlatformOnly;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public non-sealed class BaseBlockComponentHolder extends Block implements BlockComponentHolder
{
    private Set<BlockComponentType<?>> componentTypes = Sets.newLinkedHashSet();
    private boolean registered = false;

    public BaseBlockComponentHolder(Consumer<Registrar> registrarConsumer, Properties properties)
    {
        super(properties);

        initializeComponents(registrarConsumer);
    }

    // region: Component
    private void initializeComponents(Consumer<Registrar> registrarConsumer)
    {
        // register components
        if(registered) return;
        registerComponents(this::unsafeRegisterComponent);

        // register components other components require
        // iterate over copy of current types
        // we may modify the set during this loop
        for(var componentType : Set.copyOf(componentTypes))
        {
            componentType.getFor(this).onRegistered(this::safeRegisterComponent);
        }

        registrarConsumer.accept(this::safeRegisterComponent);
        registered = true;
        componentTypes = ImmutableSet.copyOf(componentTypes);

        // recreate state container
        //
        // components can register new properties
        // and since this is called after super that means
        // the existing state container will not contain
        // these newly registered properties
        // thus we recreate the state container
        // register these new properties to it
        // and then replace the existing state container ours
        var defaultBlockState = BlockHooks.replaceBlockStateDefinition(this);

        for(var componentType : componentTypes)
        {
            defaultBlockState = componentType.getFor(this).registerDefaultBlockState(defaultBlockState);
        }

        registerDefaultState(defaultBlockState);
    }

    private <T extends BlockComponent> T unsafeRegisterComponent(BlockComponentType<T> componentType)
    {
        Validate.isTrue(!registered, "Attempt to register BlockComponentType: '%s' post registration".formatted(componentType.registryName()));
        if(hasComponent(componentType)) throw new IllegalStateException("Attempt to register duplicate BlockComponentType: '%s' for Block: '%s'".formatted(componentType.registryName(), getClass().getName()));
        componentTypes.add(componentType);
        return componentType.getFor(this);
    }

    private <T extends BlockComponent> T safeRegisterComponent(BlockComponentType<T> componentType)
    {
        Validate.isTrue(!registered, "Attempt to register BlockComponentType: '%s' post registration".formatted(componentType.registryName()));
        if(!hasComponent(componentType)) componentTypes.add(componentType);
        return componentType.getFor(this);
    }

    @Override
    public void registerComponents(BlockComponentHolder.Registrar registrar) {}

    @Nullable
    @Override
    public final <T extends BlockComponent> T getComponent(BlockComponentType<T> componentType)
    {
        return hasComponent(componentType) ? componentType.getFor(this) : null;
    }

    @Override
    public final <T extends BlockComponent> Optional<T> getOptionalComponent(BlockComponentType<T> componentType)
    {
        return Optional.ofNullable(getComponent(componentType));
    }

    @Override
    public final <T extends BlockComponent> T getRequiredComponent(BlockComponentType<T> componentType)
    {
        Validate.isTrue(hasComponent(componentType), "Missing required BlockComponentType: '%s' for Block: '%s'".formatted(componentType.registryName(), getClass().getName()));
        return componentType.getFor(this);
    }

    @Override
    public final Set<BlockComponentType<?>> getComponentTypes()
    {
        return componentTypes;
    }

    @Override
    public final Stream<BlockComponent> components()
    {
        return componentTypes.stream().map(componentType -> componentType.getFor(this));
    }

    @Override
    public final <T extends BlockComponent> boolean hasComponent(BlockComponentType<T> componentType)
    {
        return componentTypes.contains(componentType);
    }

    @Override
    public final Block toBlock()
    {
        return this;
    }

    protected final void runForEach(Consumer<BlockComponent> consumer)
    {
        components().forEach(consumer);
    }

    // nullable if 'defaultValue' is null
    protected final <T> T callForEach(BiFunction<BlockComponent, T, T> function, Predicate<T> breakOut, BiFunction<T, T, T> merger, T defaultValue)
    {
        var current = defaultValue;

        for(var componentType : componentTypes)
        {
            var next = function.apply(componentType.getFor(this), current);
            if(breakOut.test(next)) return next;
            current = merger.apply(current, next);
        }

        return current;
    }

    protected final <T> T callForEach(BiFunction<BlockComponent, T, T> function, BiFunction<T, T, T> merger, T defaultValue)
    {
        return callForEach(function, value -> false, merger, defaultValue);
    }

    protected final <T> T callForEach(BiFunction<BlockComponent, T, T> function, Predicate<T> breakOut, T defaultValue)
    {
        return callForEach(function, breakOut, (current, next) -> next, defaultValue);
    }

    protected final <T> T callForEach(BiFunction<BlockComponent, T, T> function, T defaultValue)
    {
        return callForEach(function, value -> false, (current, next) -> next, defaultValue);
    }
    // endregion

    // region: Block
    @OverridingMethodsMustInvokeSuper
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return callForEach(
                (component, current) -> component.use(blockState, level, pos, player, hand, hit),
                next -> next == InteractionResult.FAIL || next.consumesAction(),
                InteractionResult.PASS
        );
    }

    @Nullable
    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return callForEach(
                (component, current) -> component.getStateForPlacement(ctx, current),
                next -> next == null || !next.is(this),
                (current, next) -> (!current.is(this) || next == null || !next.is(this)) ? null : next,
                defaultBlockState()
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        return callForEach(
                (component, current) -> component.canSurvive(blockState, level, pos),
                next -> !next,
                true
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
        runForEach(component -> component.onPlace(blockState, level, pos, oldBlockState, isMoving));
        super.onPlace(blockState, level, pos, oldBlockState, isMoving);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        runForEach(component -> component.onRemove(blockState, level, pos, newBlockState, isMoving));
        super.onRemove(blockState, level, pos, newBlockState, isMoving);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        // this method gets run twice
        // once by super constructor, to build vanilla state definition
        // and once by us to build our replacement state definition
        // if called by super our fields have not yet initialized causing NPE
        // this if check delays until everything has been initialized
        if(registered) runForEach(component -> component.createBlockStateDefinition(builder::add));
        super.createBlockStateDefinition(builder);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return callForEach(
                (component, current) -> component.getRenderShape(blockState),
                next -> next != RenderShape.MODEL,
                RenderShape.MODEL
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        runForEach(component -> component.setPlacedBy(level, pos, blockState, placer, stack));
        super.setPlacedBy(level, pos, blockState, placer, stack);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation)
    {
        return callForEach(
                (component, current) -> component.rotate(blockState, rotation),
                next -> !next.is(this),
                (current, next) -> (!current.is(this) || !next.is(this)) ? blockState : next,
                blockState
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror)
    {
        return callForEach(
                (component, current) -> component.mirror(blockState, mirror),
                next -> !next.is(this),
                (current, next) -> (!current.is(this) || !next.is(this)) ? blockState : next,
                blockState
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        return callForEach(
                (component, current) -> component.updateShape(blockState, direction, neighborState, level, currentPos, neighborPos),
                next -> !next.is(this),
                (current, next) -> (!current.is(this) || !next.is(this)) ? blockState : next,
                blockState
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        runForEach(component -> component.neighborChanged(blockState, level, pos, block, fromPos, isMoving));
        super.neighborChanged(blockState, level, pos, block, fromPos, isMoving);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        runForEach(component -> component.playerWillDestroy(level, pos, blockState, player));
        super.playerWillDestroy(level, pos, blockState, player);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter level, BlockPos pos, PathComputationType type)
    {
        return callForEach(
                (component, current) -> component.isPathFindable(blockState, level, pos, type),
                next -> !next,
                true
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public PushReaction getPistonPushReaction(BlockState blockState)
    {
        return callForEach(
                (component, current) -> component.getPistonPushReaction(blockState),
                Objects::isNull,
                blockState.getMaterial().getPushReaction()
        );
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public long getSeed(BlockState blockState, BlockPos pos)
    {
        return callForEach(
                (component, current) -> component.getSeed(blockState, pos),
                (current, next) -> next > 0L ? Math.max(current, next) : current,
                -1L
        );
    }
    // endregion

    // region: Forge Overrides
    @PlatformOnly(Platform.FORGE)
    @Deprecated
    public final boolean isBed(BlockState blockState, BlockGetter level, BlockPos pos, @Nullable Entity player)
    {
        return BedBlockComponent.isComponableBed(blockState);
    }

    @PlatformOnly(Platform.FORGE)
    @Deprecated
    public final void setBedOccupied(BlockState blockState, Level level, BlockPos pos, LivingEntity sleeper, boolean occupied)
    {
        BedBlockComponent.setOccupied(level, pos, blockState, occupied);
    }

    @PlatformOnly(Platform.FORGE)
    @Deprecated
    public final Direction getBedDirection(BlockState blockState, LevelReader level, BlockPos pos)
    {
        return blockState.getValue(BedBlockComponent.FACING);
    }
    // endregion
}
