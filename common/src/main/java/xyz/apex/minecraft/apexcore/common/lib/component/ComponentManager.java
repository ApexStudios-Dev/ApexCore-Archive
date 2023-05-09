package xyz.apex.minecraft.apexcore.common.lib.component;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Core manager used to register, maintain and otherwise manage components for a given component holder.
 *
 * @param <V> Base game object type.
 * @param <H> Component holder type.
 */
public final class ComponentManager<V, H extends ComponentHolder<V, H>>
{
    private Set<ComponentType<V, H, ?>> componentTypes = Collections.emptySet();
    private final H componentHolder;
    private boolean registeredComponents = false;

    public ComponentManager(H componentHolder)
    {
        this.componentHolder = componentHolder;
    }

    /**
     * Call to invoke component registration.
     *
     * @param onRegister Consumer used to register components.
     */
    public void registerComponents(Consumer<ComponentHolder.Registrar<V, H>> onRegister)
    {
        Validate.isTrue(!registeredComponents);

        var registrations = MultimapBuilder.hashKeys().linkedListValues().<ComponentType<V, H, ?>, Runnable>build();

        ComponentHolder.Registrar<V, H> registrar = new ComponentHolder.Registrar<>()
        {
            @Override
            public <T extends Component<V, H>> void register(ComponentType<V, H, T> componentType, Consumer<T> consumer)
            {
                registrations.put(componentType, () -> consumer.accept(componentType.get(componentHolder)));
            }
        };

        // Does not like this for some reason?
        // 'Target method is generic'
        // is error idea is giving me
        /*ComponentHolder.Registrar<V, H> registrar = (componentType, listener) -> {

        };*/

        onRegister.accept(registrar);
        componentTypes = ImmutableSet.copyOf(registrations.keySet());

        for(var componentType : componentTypes)
        {
            registrations.get(componentType).forEach(Runnable::run);
        }

        registeredComponents = true;
    }

    /**
     * Returns component instance for given component type, if it was registered.
     *
     * @param componentType Type of component.
     * @param <T>           Type of component.
     * @return Component for given component type, if it was registered.
     */
    @Nullable
    public <T extends Component<V, H>> T getComponent(ComponentType<V, H, T> componentType)
    {
        return componentTypes.contains(componentType) ? componentType.get(componentHolder) : null;
    }

    /**
     * Returns optional holding result of {@link #getComponent(ComponentType)} or empty.
     *
     * @param componentType Type of component.
     * @param <T>           Type of component.
     * @return Optional holding result of {@link #getComponent(ComponentType)}.
     */
    public <T extends Component<V, H>> Optional<T> getOptionalComponent(ComponentType<V, H, T> componentType)
    {
        return Optional.ofNullable(getComponent(componentType));
    }

    /**
     * Returns component instance for given component type, if it was registered.
     * <p>
     * Throws {@link NoSuchElementException} if component type is not registered.
     *
     * @param componentType Type of component.
     * @param <T>           Type of component.
     * @return Component instance for given component type.
     * @throws NoSuchElementException If component type was not registered.
     */
    public <T extends Component<V, H>> T getComponentOrThrow(ComponentType<V, H, T> componentType) throws NoSuchElementException
    {
        if(!componentTypes.contains(componentType))
            throw new NoSuchElementException("Attempt to obtain Component instance for unregistered ComponentType[%s]".formatted(componentType.registryName()));
        return componentType.get(componentHolder);
    }

    /**
     * Returns true if the given component type is registered.
     *
     * @param componentType Type of component.
     * @return True if the given component type is registered.
     */
    public boolean hasComponent(ComponentType<V, H, ?> componentType)
    {
        return componentTypes.contains(componentType);
    }

    /**
     * @return True after component registration has occurred.
     */
    public boolean registeredComponents()
    {
        return registeredComponents;
    }

    /**
     * @return Set containing all registered component types.
     */
    public Set<ComponentType<V, H, ?>> getComponentTypes()
    {
        return componentTypes;
    }

    /**
     * @return Collection containing all registered components.
     */
    public Collection<Component<V, H>> getComponents()
    {
        return components().toList();
    }

    /**
     * @return Stream containing all registered components.
     */
    public Stream<Component<V, H>> components()
    {
        return componentTypes.stream().map(componentType -> componentType.get(componentHolder));
    }

    /**
     * @return Component holder this component manager is bound to.
     */
    public H getComponentHolder()
    {
        return componentHolder;
    }

    /**
     * @return Game object this component manager is bound to.
     */
    public V toGameObject()
    {
        return componentHolder.toGameObject();
    }
}
