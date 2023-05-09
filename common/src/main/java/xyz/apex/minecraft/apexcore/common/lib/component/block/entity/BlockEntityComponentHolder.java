package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.Component;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentManager;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Base implementation for block entity components holders.
 */
public class BlockEntityComponentHolder extends BlockEntity implements ComponentHolder<BlockEntity, BlockEntityComponentHolder>
{
    protected final ComponentManager<BlockEntity, BlockEntityComponentHolder> componentManager;

    public BlockEntityComponentHolder(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);

        componentManager = new ComponentManager<>(this);
        // convert from generic ComponentHolder.Registrar to BlockEntity specific Registrar
        componentManager.registerComponents(registrar -> registerComponents(registrar::register));
    }

    // region: ComponentHolder
    protected void registerComponents(Registrar registrar)
    {
    }

    @Nullable
    public final <T extends BlockEntityComponent> T getComponent(BlockEntityComponentType<T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    public final <T extends BlockEntityComponent> Optional<T> getOptionalComponent(BlockEntityComponentType<T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    public final <T extends BlockEntityComponent> T getComponentOrThrow(BlockEntityComponentType<T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    public final boolean hasComponent(BlockEntityComponentType<?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    public final Collection<BlockEntityComponent> getBlockEntityComponents()
    {
        return blockEntityComponents().collect(ImmutableList.toImmutableList());
    }

    public final Stream<BlockEntityComponent> blockEntityComponents()
    {
        return componentManager.components().filter(BlockEntityComponent.class::isInstance).map(BlockEntityComponent.class::cast);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponent(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Nullable
    @Override
    public final <T extends Component<BlockEntity, BlockEntityComponentHolder>> T getComponent(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getOptionalComponent(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<BlockEntity, BlockEntityComponentHolder>> Optional<T> getOptionalComponent(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponentOrThrow(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<BlockEntity, BlockEntityComponentHolder>> T getComponentOrThrow(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #hasComponent(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Override
    public final boolean hasComponent(ComponentType<BlockEntity, BlockEntityComponentHolder, ?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    @Override
    public final boolean registeredComponents()
    {
        return componentManager.registeredComponents();
    }

    @Override
    public final Set<ComponentType<BlockEntity, BlockEntityComponentHolder, ?>> getComponentTypes()
    {
        return componentManager.getComponentTypes();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getBlockEntityComponents()} instead.
     */
    @Deprecated
    @Override
    public final Collection<Component<BlockEntity, BlockEntityComponentHolder>> getComponents()
    {
        return componentManager.getComponents();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #blockEntityComponents()} instead.
     */
    @Deprecated
    @Override
    public final Stream<Component<BlockEntity, BlockEntityComponentHolder>> components()
    {
        return componentManager.components();
    }

    @Override
    public final BlockEntity toGameObject()
    {
        return this;
    }
    // endregion

    @FunctionalInterface
    public interface Registrar extends ComponentHolder.Registrar<BlockEntity, BlockEntityComponentHolder>
    {
        default <T extends BlockEntityComponent> void register(BlockEntityComponentType<T> componentType, Consumer<T> consumer)
        {
            register((ComponentType<BlockEntity, BlockEntityComponentHolder, T>) componentType, consumer);
        }

        default void register(BlockEntityComponentType<?> componentType)
        {
            register((ComponentType<BlockEntity, BlockEntityComponentHolder, ?>) componentType);
        }

        default void register(BlockEntityComponentType<?>... componentTypes)
        {
            register((ComponentType<BlockEntity, BlockEntityComponentHolder, ?>[]) componentTypes);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockEntityComponentType, Consumer)} instead.
         */
        @Deprecated
        @Override
        <T extends Component<BlockEntity, BlockEntityComponentHolder>> void register(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType, Consumer<T> consumer);

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockEntityComponentType)} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<BlockEntity, BlockEntityComponentHolder, ?> componentType)
        {
            ComponentHolder.Registrar.super.register(componentType);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockEntityComponentType[])} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<BlockEntity, BlockEntityComponentHolder, ?>... componentTypes)
        {
            ComponentHolder.Registrar.super.register(componentTypes);
        }
    }
}
