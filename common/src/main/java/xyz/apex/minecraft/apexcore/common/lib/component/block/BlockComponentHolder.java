package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.common.collect.ImmutableList;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.Component;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentManager;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentType;
import xyz.apex.minecraft.apexcore.common.lib.util.BlockUtil;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Base implementation for block components holders.
 * <p>
 * While this block implements various logic interfaces, this does not mean
 * the blocks extending this class may have that logic, the interfaces are
 * implemented for our components to hook into them cleanly.
 * <p>
 * Example: {@link SimpleWaterloggedBlock}
 * Even though this interface is implemented, that does not mean all child classes of this class
 * are water loggable, either must implement the logic themselves or register some component which adds the logic for them.
 */
@SuppressWarnings("deprecation")
public class BlockComponentHolder extends Block implements ComponentHolder<Block, BlockComponentHolder>, SimpleWaterloggedBlock
{
    protected final ComponentManager<Block, BlockComponentHolder> componentManager;

    public BlockComponentHolder(Properties properties)
    {
        super(properties);

        componentManager = new ComponentManager<>(this);
        // convert from generic ComponentHolder.Registrar to Block specific Registrar
        componentManager.registerComponents(registrar -> registerComponents(registrar::register));

        BlockUtil.rebuildStateDefinition(
                this,
                builder -> blockComponents().forEach(component -> component.createBlockStateDefinition(builder)),
                defaultBlockState -> {
                    for(var component : getBlockComponents())
                    {
                        defaultBlockState = component.registerDefaultBlockState(defaultBlockState);
                    }
                    return defaultBlockState;
                }
        );
    }

    // region: ComponentHolder
    protected void registerComponents(Registrar registrar)
    {
    }

    @Nullable
    public final <T extends BlockComponent> T getComponent(BlockComponentType<T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    public final <T extends BlockComponent> Optional<T> getOptionalComponent(BlockComponentType<T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    public final <T extends BlockComponent> T getComponentOrThrow(BlockComponentType<T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    public final boolean hasComponent(BlockComponentType<?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    public final Collection<BlockComponent> getBlockComponents()
    {
        return blockComponents().collect(ImmutableList.toImmutableList());
    }

    public final Stream<BlockComponent> blockComponents()
    {
        return componentManager.components().filter(BlockComponent.class::isInstance).map(BlockComponent.class::cast);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponent(BlockComponentType)} instead.
     */
    @Deprecated
    @Nullable
    @Override
    public final <T extends Component<Block, BlockComponentHolder>> T getComponent(ComponentType<Block, BlockComponentHolder, T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getOptionalComponent(BlockComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<Block, BlockComponentHolder>> Optional<T> getOptionalComponent(ComponentType<Block, BlockComponentHolder, T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponentOrThrow(BlockComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<Block, BlockComponentHolder>> T getComponentOrThrow(ComponentType<Block, BlockComponentHolder, T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #hasComponent(BlockComponentType)} instead.
     */
    @Deprecated
    @Override
    public final boolean hasComponent(ComponentType<Block, BlockComponentHolder, ?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    @Override
    public final boolean registeredComponents()
    {
        return componentManager.registeredComponents();
    }

    @Override
    public final Set<ComponentType<Block, BlockComponentHolder, ?>> getComponentTypes()
    {
        return componentManager.getComponentTypes();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getBlockComponents()} instead.
     */
    @Deprecated
    @Override
    public final Collection<Component<Block, BlockComponentHolder>> getComponents()
    {
        return componentManager.getComponents();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #blockComponents()} instead.
     */
    @Deprecated
    @Override
    public final Stream<Component<Block, BlockComponentHolder>> components()
    {
        return componentManager.components();
    }

    @Override
    public final Block toGameObject()
    {
        return this;
    }
    // endregion

    // region: Hooks
    @Nullable
    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        var placementBlockState = defaultBlockState();

        for(var component : getBlockComponents())
        {
            if(placementBlockState == null) return null;
            placementBlockState = component.getStateForPlacement(placementBlockState, ctx);
        }

        return placementBlockState;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack tool)
    {
        blockComponents().forEach(component -> component.playerDestroy(level, player, pos, blockState, blockEntity, tool));

        // update stat, cause exhaustion and drop resources
        super.playerDestroy(level, player, pos, blockState, blockEntity, tool);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        blockComponents().forEach(component -> component.setPlacedBy(level, pos, blockState, placer, stack));
    }

    /*@OverridingMethodsMustInvokeSuper
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        if(!registeredComponents()) return; // fields are not yet initialed during registration leading to NPE
        blockComponents().forEach(component -> component.createBlockStateDefinition(builder));
    }*/

    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborBlockState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        for(var component : getBlockComponents())
        {
            blockState = component.updateShape(blockState, direction, neighborBlockState, level, currentPos, neighborPos);
        }

        return blockState;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        blockComponents().forEach(component -> component.neighborChanged(blockState, level, pos, block, fromPos, isMoving));

        // send debug packet
        super.neighborChanged(blockState, level, pos, block, fromPos, isMoving);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
        blockComponents().forEach(component -> component.onPlace(blockState, level, pos, oldBlockState, isMoving));
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        blockComponents().forEach(component -> component.onRemove(blockState, level, pos, newBlockState, isMoving));

        // remove block entity
        super.onRemove(blockState, level, pos, newBlockState, isMoving);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        for(var component : getBlockComponents())
        {
            var result = component.use(blockState, level, pos, player, hand, hit);
            if(result.consumesAction()) return result;
        }

        return InteractionResult.PASS;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public FluidState getFluidState(BlockState blockState)
    {
        for(var component : getBlockComponents())
        {
            var fluidState = component.getFluidState(blockState);
            if(fluidState.isPresent()) return fluidState.get();
        }

        // return empty fluid state
        return super.getFluidState(blockState);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation)
    {
        for(var component : getBlockComponents())
        {
            blockState = component.rotate(blockState, rotation);
        }

        return blockState;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror)
    {
        for(var component : getBlockComponents())
        {
            blockState = component.mirror(blockState, mirror);
        }

        return blockState;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState blockState, Fluid fluid)
    {
        for(var component : getBlockComponents())
        {
            if(!(component instanceof SimpleWaterloggedBlock logged)) continue;
            if(logged.canPlaceLiquid(level, pos, blockState, fluid)) return true;
        }

        return false;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState blockState, FluidState fluidState)
    {
        for(var component : getBlockComponents())
        {
            if(!(component instanceof SimpleWaterloggedBlock logged)) continue;
            if(logged.placeLiquid(level, pos, blockState, fluidState)) return true;
        }

        return false;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState blockState)
    {
        for(var component : getBlockComponents())
        {
            if(!(component instanceof SimpleWaterloggedBlock logged)) continue;
            var bucket = logged.pickupBlock(level, pos, blockState);
            if(!bucket.isEmpty()) return bucket;
        }

        return ItemStack.EMPTY;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public Optional<SoundEvent> getPickupSound()
    {
        for(var component : getBlockComponents())
        {
            if(!(component instanceof SimpleWaterloggedBlock logged)) continue;
            var pickupSound = logged.getPickupSound();
            if(pickupSound.isPresent()) return pickupSound;
        }

        return Optional.empty();
    }
    // endregion

    @FunctionalInterface
    public interface Registrar extends ComponentHolder.Registrar<Block, BlockComponentHolder>
    {
        default <T extends BlockComponent> void register(BlockComponentType<T> componentType, Consumer<T> consumer)
        {
            register((ComponentType<Block, BlockComponentHolder, T>) componentType, consumer);
        }

        default void register(BlockComponentType<?> componentType)
        {
            register((ComponentType<Block, BlockComponentHolder, ?>) componentType);
        }

        default void register(BlockComponentType<?>... componentTypes)
        {
            register((ComponentType<Block, BlockComponentHolder, ?>[]) componentTypes);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockComponentType, Consumer)} instead.
         */
        @Deprecated
        @Override
        <T extends Component<Block, BlockComponentHolder>> void register(ComponentType<Block, BlockComponentHolder, T> componentType, Consumer<T> consumer);

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockComponentType)} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<Block, BlockComponentHolder, ?> componentType)
        {
            ComponentHolder.Registrar.super.register(componentType);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockComponentType[])} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<Block, BlockComponentHolder, ?>... componentTypes)
        {
            ComponentHolder.Registrar.super.register(componentTypes);
        }
    }
}
