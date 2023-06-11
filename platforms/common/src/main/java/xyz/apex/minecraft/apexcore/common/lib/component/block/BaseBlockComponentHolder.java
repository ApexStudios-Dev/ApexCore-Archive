package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.errorprone.annotations.ForOverride;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.helper.BlockHelper;

import java.util.*;
import java.util.function.Consumer;

public non-sealed class BaseBlockComponentHolder extends BaseEntityBlock implements BlockComponentHolder
{
    private final Map<BlockComponentType<?>, BlockComponent> componentRegistry = registerComponents();

    // methods tied to these do not have a BlockPos parameter
    // which means we can not easily callback the block entity
    // not the best solution but this works
    private final Map<BlockState, Boolean> isSignalSource = Maps.newHashMap();
    private final Map<BlockState, Boolean> hasAnalogOutputSignal = Maps.newHashMap();

    public BaseBlockComponentHolder(Properties properties)
    {
        super(properties);
    }

    @Nullable
    protected BlockEntityType<?> getBlockEntityType()
    {
        return null;
    }

    @Nullable
    protected <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState)
    {
        return null;
    }

    // region: Components
    @OverridingMethodsMustInvokeSuper
    @ForOverride
    protected void registerComponents(BlockComponentRegistrar registrar)
    {
    }

    private Map<BlockComponentType<?>, BlockComponent> registerComponents()
    {
        var registrar = new BlockComponentRegistrar() {
            private final Multimap<BlockComponentType<?>, Consumer<BlockComponent>> listeners = HashMultimap.create();

            @SuppressWarnings("unchecked")
            @Override
            public <C extends BlockComponent> void register(BlockComponentType<C> componentType, Consumer<C> consumer)
            {
                listeners.put(componentType, (Consumer<BlockComponent>) consumer);
            }
        };

        registerComponents(registrar);

        var map = Maps.<BlockComponentType<?>, BlockComponent>newHashMap();

        for(var componentType : registrar.listeners.keys())
        {
            var component = componentType.newInstance(this);
            registrar.listeners.get(componentType).forEach(listener -> listener.accept(component));
            ((BaseBlockComponent) component).postRegistration();
            map.put(componentType, component);
        }

        BlockHelper.rebuildStateDefinition(this, builder -> map.values().forEach(component -> component.createBlockStateDefinition(builder)), defaultBlockState -> {
            for(var component : map.values())
            {
                defaultBlockState = component.registerDefaultBlockState(defaultBlockState);
            }

            return defaultBlockState;
        });

        return ImmutableMap.copyOf(map);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public final <C extends BlockComponent> C getComponent(BlockComponentType<C> componentType)
    {
        var component = componentRegistry.get(componentType);
        return component == null ? null : (C) component;
    }

    @Override
    public final <C extends BlockComponent> Optional<C> findComponent(BlockComponentType<C> componentType)
    {
        return Optional.ofNullable(getComponent(componentType));
    }

    @Override
    public final <C extends BlockComponent> C getRequiredComponent(BlockComponentType<C> componentType)
    {
        return Objects.requireNonNull(getComponent(componentType), "Missing required Block Component: %s".formatted(componentType));
    }

    @Override
    public final boolean hasComponent(BlockComponentType<?> componentType)
    {
        return componentRegistry.containsKey(componentType);
    }

    @Override
    public final Set<BlockComponentType<?>> getComponentTypes()
    {
        return componentRegistry.keySet();
    }

    @Override
    public final Collection<BlockComponent> getComponents()
    {
        return componentRegistry.values();
    }

    @Override
    public final Block getGameObject()
    {
        return this;
    }
    // endregion

    // region: Events
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        var placementBlockState = defaultBlockState();

        for(var component : getComponents())
        {
            placementBlockState = component.getStateForPlacement(placementBlockState, context);

            if(placementBlockState == null)
                return null;
        }

        return placementBlockState;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack tool)
    {
        if(blockEntity instanceof BlockEntityComponentHolder blockEntityComponentHolder)
            blockEntityComponentHolder.playerDestroy(level, player, tool);

        getComponents().forEach(component -> component.playerDestroy(level, player, pos, blockState, blockEntity, tool));
        super.playerDestroy(level, player, pos, blockState, blockEntity, tool);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.setPlacedBy(level, pos, blockState, placer, stack);

        if(level.getBlockEntity(pos) instanceof BlockEntityComponentHolder blockEntityComponentHolder)
            blockEntityComponentHolder.setPlacedBy(level, placer, stack);

        getComponents().forEach(component -> component.setPlacedBy(level, pos, blockState, placer, stack));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        if(level.getBlockEntity(pos) instanceof BlockEntityComponentHolder blockEntityComponentHolder)
            blockEntityComponentHolder.playerWillDestroy(level, player);

        getComponents().forEach(component -> component.playerWillDestroy(level, pos, blockState, player));
        super.playerWillDestroy(level, pos, blockState, player);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborBlockState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        var newBlockState = blockState;

        for(var component : getComponents())
        {
            newBlockState = component.updateShape(newBlockState, direction, neighborBlockState, level, currentPos, neighborPos);
        }

        return newBlockState;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        getComponents().forEach(component -> component.neighborChanged(blockState, level, pos, block, fromPos, isMoving));
        super.neighborChanged(blockState, level, pos, block, fromPos, isMoving);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
        super.onPlace(blockState, level, pos, oldBlockState, isMoving);
        getComponents().forEach(component -> component.onPlace(blockState, level, pos, oldBlockState, isMoving));

        if(level.getBlockEntity(pos) instanceof BlockEntityComponentHolder blockEntityComponentHolder)
            blockEntityComponentHolder.onPlace(level, oldBlockState);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        if(level.getBlockEntity(pos) instanceof BlockEntityComponentHolder blockEntityComponentHolder)
            blockEntityComponentHolder.onRemove(level, newBlockState);

        getComponents().forEach(component -> component.onRemove(blockState, level, pos, newBlockState, isMoving));
        super.onRemove(blockState, level, pos, newBlockState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(level.getBlockEntity(pos) instanceof BlockEntityComponentHolder blockEntityComponentHolder)
        {
            var result = blockEntityComponentHolder.use(level, player, hand, hit);

            if(result.consumesAction())
                return result;
        }

        for(var component : getComponents())
        {
            var result = component.use(blockState, level, pos, player, hand, hit);

            if(result.consumesAction())
                return result;
        }

        return InteractionResult.PASS;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        for(var component : getComponents())
        {
            var renderShape = component.getRenderShape(blockState);

            if(renderShape != null)
                return renderShape;
        }

        return RenderShape.MODEL; // super gives us INVISIBLE
    }

    @Override
    public FluidState getFluidState(BlockState blockState)
    {
        for(var component : getComponents())
        {
            var fluidState = component.getFluidState(blockState);

            if(fluidState != null && !fluidState.isEmpty())
                return fluidState;
        }

        return super.getFluidState(blockState);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation)
    {
        var newBlockState = blockState;

        for(var component : getComponents())
        {
            newBlockState = component.rotate(newBlockState, rotation);
        }

        return newBlockState;
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror)
    {
        var newBlockState = blockState;

        for(var component : getComponents())
        {
            newBlockState = component.mirror(newBlockState, mirror);
        }

        return newBlockState;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext useContext)
    {
        for(var component : getComponents())
        {
            if(!component.canBeReplaced(blockState, useContext))
                return false;
        }

        return super.canBeReplaced(blockState, useContext);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid)
    {
        for(var component : getComponents())
        {
            if(!component.canBeReplaced(blockState, fluid))
                return false;
        }

        return super.canBeReplaced(blockState, fluid);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        for(var component : getComponents())
        {
            if(!component.canSurvive(blockState, level, pos))
                return false;
        }

        return super.canSurvive(blockState, level, pos);
    }

    @Nullable
    @Override
    public final <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType)
    {
        return blockEntityType == getBlockEntityType() ? getTicker(level, blockState) : null;
    }

    @Override
    public boolean isSignalSource(BlockState blockState)
    {
        var isSignalSource = this.isSignalSource.get(blockState);

        if(isSignalSource != null && isSignalSource)
            return true;

        return getComponents().stream().anyMatch(component -> component.isSignalSource(blockState));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState)
    {
        var hasAnalogOutputSignal = this.hasAnalogOutputSignal.get(blockState);

        if(hasAnalogOutputSignal != null && hasAnalogOutputSignal)
            return true;

        return getComponents().stream().anyMatch(component -> component.hasAnalogOutputSignal(blockState));
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        if(level.getBlockEntity(pos) instanceof BlockEntityComponentHolder blockEntityComponentHolder)
        {
            var analogOutputSignal = blockEntityComponentHolder.getAnalogOutputSignal(level);

            if(analogOutputSignal > 0)
                return analogOutputSignal;
        }

        for(var component : getComponents())
        {
            var analogOutputSignal = component.getAnalogOutputSignal(blockState, level, pos);

            if(analogOutputSignal > 0)
                return analogOutputSignal;
        }

        return 0;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction direction)
    {
        if(level.getBlockEntity(pos) instanceof BlockEntityComponentHolder blockEntityComponentHolder)
        {
            var analogOutputSignal = blockEntityComponentHolder.getSignal(level, direction);

            if(analogOutputSignal > 0)
                return analogOutputSignal;
        }

        for(var component : getComponents())
        {
            var analogOutputSignal = component.getSignal(blockState, level, pos, direction);

            if(analogOutputSignal > 0)
                return analogOutputSignal;
        }

        return 0;
    }

    @Nullable
    @Override
    public final BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        var blockEntityType = getBlockEntityType();
        var blockEntity = blockEntityType == null ? null : blockEntityType.create(pos, blockState);

        if(blockEntity instanceof BlockEntityComponentHolder blockEntityComponentHolder)
        {
            isSignalSource.put(blockState, blockEntityComponentHolder.isSignalSource());
            hasAnalogOutputSignal.put(blockState, blockEntityComponentHolder.hasAnalogOutputSignal());
        }

        return blockEntity;
    }

    @Override
    public final ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState blockState)
    {
        for(var component : getComponents())
        {
            if(component instanceof BucketPickup pickup)
            {
                var bucket = pickup.pickupBlock(level, pos, blockState);

                if(!bucket.isEmpty())
                    return bucket;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public final Optional<SoundEvent> getPickupSound()
    {
        for(var component : getComponents())
        {
            if(component instanceof BucketPickup pickup)
            {
                var pickupSound = pickup.getPickupSound();

                if(pickupSound.isPresent())
                    return pickupSound;
            }
        }

        return Optional.empty();
    }

    @Override
    public final boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState blockState, Fluid fluid)
    {
        for(var component : getComponents())
        {
            if(component instanceof LiquidBlockContainer container && container.canPlaceLiquid(level, pos, blockState, fluid))
                return true;
        }

        return false;
    }

    @Override
    public final boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState blockState, FluidState fluidState)
    {
        for(var component : getComponents())
        {
            if(component instanceof LiquidBlockContainer container && container.placeLiquid(level, pos, blockState, fluidState))
                return true;
        }

        return false;
    }
    // endregion
}
