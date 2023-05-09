package xyz.apex.minecraft.apexcore.common.lib.component;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Base implementation for all components.
 * <p>
 * To be implemented per type for any type specific implementations.
 *
 * @param <V> Base game object type.
 * @param <H> Component holder type.
 */
public non-sealed class BaseComponent<V, H extends ComponentHolder<V, H>> implements Component<V, H>
{
    protected final H componentHolder;

    protected BaseComponent(H componentHolder)
    {
        this.componentHolder = componentHolder;
    }

    @Nullable
    @Override
    public final <T extends Component<V, H>> T getComponent(ComponentType<V, H, T> componentType)
    {
        return componentHolder.getComponent(componentType);
    }

    @Override
    public final <T extends Component<V, H>> Optional<T> getOptionalComponent(ComponentType<V, H, T> componentType)
    {
        return componentHolder.getOptionalComponent(componentType);
    }

    @Override
    public final <T extends Component<V, H>> T getComponentOrThrow(ComponentType<V, H, T> componentType) throws NoSuchElementException
    {
        return componentHolder.getComponentOrThrow(componentType);
    }

    @Override
    public final boolean hasComponent(ComponentType<V, H, ?> componentType)
    {
        return componentHolder.hasComponent(componentType);
    }

    @Override
    public final boolean registeredComponents()
    {
        return componentHolder.registeredComponents();
    }

    @Override
    public final Set<ComponentType<V, H, ?>> getComponentTypes()
    {
        return componentHolder.getComponentTypes();
    }

    @Override
    public final Collection<Component<V, H>> getComponents()
    {
        return componentHolder.getComponents();
    }

    @Override
    public final Stream<Component<V, H>> components()
    {
        return componentHolder.components();
    }

    @Override
    public final H getComponentHolder()
    {
        return componentHolder;
    }

    @Override
    public final V toGameObject()
    {
        return componentHolder.toGameObject();
    }
}
