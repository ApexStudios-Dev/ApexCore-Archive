package xyz.apex.minecraft.apexcore.common.lib.component.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.Item;
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
 * Base implementation for item components holders.
 */
public class ItemComponentHolder extends Item implements ComponentHolder<Item, ItemComponentHolder>
{
    protected final ComponentManager<Item, ItemComponentHolder> componentManager;

    public ItemComponentHolder(Properties properties)
    {
        super(properties);

        componentManager = new ComponentManager<>(this);
        // convert from generic ComponentHolder.Registrar to Item specific Registrar
        componentManager.registerComponents(registrar -> registerComponents(registrar::register));
    }

    // region: ComponentHolder
    protected void registerComponents(Registrar registrar)
    {
    }

    @Nullable
    public final <T extends ItemComponent> T getComponent(ItemComponentType<T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    public final <T extends ItemComponent> Optional<T> getOptionalComponent(ItemComponentType<T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    public final <T extends ItemComponent> T getComponentOrThrow(ItemComponentType<T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    public final boolean hasComponent(ItemComponentType<?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    public final Collection<ItemComponent> getItemComponents()
    {
        return itemComponents().collect(ImmutableList.toImmutableList());
    }

    public final Stream<ItemComponent> itemComponents()
    {
        return componentManager.components().filter(ItemComponent.class::isInstance).map(ItemComponent.class::cast);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponent(ItemComponentType)} instead.
     */
    @Deprecated
    @Nullable
    @Override
    public final <T extends Component<Item, ItemComponentHolder>> T getComponent(ComponentType<Item, ItemComponentHolder, T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getOptionalComponent(ItemComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<Item, ItemComponentHolder>> Optional<T> getOptionalComponent(ComponentType<Item, ItemComponentHolder, T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponentOrThrow(ItemComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<Item, ItemComponentHolder>> T getComponentOrThrow(ComponentType<Item, ItemComponentHolder, T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #hasComponent(ItemComponentType)} instead.
     */
    @Deprecated
    @Override
    public final boolean hasComponent(ComponentType<Item, ItemComponentHolder, ?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    @Override
    public final boolean registeredComponents()
    {
        return componentManager.registeredComponents();
    }

    @Override
    public final Set<ComponentType<Item, ItemComponentHolder, ?>> getComponentTypes()
    {
        return componentManager.getComponentTypes();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getItemComponents()} instead.
     */
    @Deprecated
    @Override
    public final Collection<Component<Item, ItemComponentHolder>> getComponents()
    {
        return componentManager.getComponents();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #itemComponents()} instead.
     */
    @Deprecated
    @Override
    public final Stream<Component<Item, ItemComponentHolder>> components()
    {
        return componentManager.components();
    }

    @Override
    public final Item toGameObject()
    {
        return this;
    }
    // endregion

    @FunctionalInterface
    public interface Registrar extends ComponentHolder.Registrar<Item, ItemComponentHolder>
    {
        default <T extends ItemComponent> void register(ItemComponentType<T> componentType, Consumer<T> consumer)
        {
            register((ComponentType<Item, ItemComponentHolder, T>) componentType, consumer);
        }

        default void register(ItemComponentType<?> componentType)
        {
            register((ComponentType<Item, ItemComponentHolder, ?>) componentType);
        }

        default void register(ItemComponentType<?>... componentTypes)
        {
            register((ComponentType<Item, ItemComponentHolder, ?>[]) componentTypes);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(ItemComponentType, Consumer)} instead.
         */
        @Deprecated
        @Override
        <T extends Component<Item, ItemComponentHolder>> void register(ComponentType<Item, ItemComponentHolder, T> componentType, Consumer<T> consumer);

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(ItemComponentType)} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<Item, ItemComponentHolder, ?> componentType)
        {
            ComponentHolder.Registrar.super.register(componentType);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(ItemComponentType[])} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<Item, ItemComponentHolder, ?>... componentTypes)
        {
            ComponentHolder.Registrar.super.register(componentTypes);
        }
    }
}
