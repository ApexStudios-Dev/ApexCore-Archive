package xyz.apex.minecraft.apexcore.common.lib.component;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Base interface for all component holders.
 * <p>
 * Recommended to not manually implement this interface.
 *
 * @param <V> Base game object type.
 * @param <H> Component holder type.
 */
public interface ComponentHolder<V, H extends ComponentHolder<V, H>>
{
    /**
     * Returns component instance for given component type, if it was registered.
     *
     * @param componentType Type of component.
     * @param <T>           Type of component.
     * @return Component for given component type, if it was registered.
     */
    @Nullable <T extends Component<V, H>> T getComponent(ComponentType<V, H, T> componentType);

    /**
     * Returns optional holding result of {@link #getComponent(ComponentType)} or empty.
     *
     * @param componentType Type of component.
     * @param <T>           Type of component.
     * @return Optional holding result of {@link #getComponent(ComponentType)}.
     */
    <T extends Component<V, H>> Optional<T> getOptionalComponent(ComponentType<V, H, T> componentType);

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
    <T extends Component<V, H>> T getComponentOrThrow(ComponentType<V, H, T> componentType) throws NoSuchElementException;

    /**
     * Returns true if the given component type is registered.
     *
     * @param componentType Type of component.
     * @return True if the given component type is registered.
     */
    boolean hasComponent(ComponentType<V, H, ?> componentType);

    /**
     * @return True after component registration has occurred.
     */
    boolean registeredComponents();

    /**
     * @return Set containing all registered component types.
     */
    Set<ComponentType<V, H, ?>> getComponentTypes();

    /**
     * @return Collection containing all registered components.
     */
    Collection<Component<V, H>> getComponents();

    /**
     * @return Stream containing all registered components.
     */
    Stream<Component<V, H>> components();

    /**
     * @return Game object this component holder is bound to.
     */
    V toGameObject();

    /**
     * Registrar used when registering components.
     *
     * @param <V> Base game object type.
     * @param <H> Component holder type.
     */
    @FunctionalInterface
    interface Registrar<V, H extends ComponentHolder<V, H>>
    {
        /**
         * Registers the given component.
         * <p>
         * Queues up given listener to be invoked post registration, for any last minute modifications to the component.
         *
         * @param componentType Component type to be registered.
         * @param consumer      Listener to be invoked post registration.
         * @param <T>           Component type.
         */
        <T extends Component<V, H>> void register(ComponentType<V, H, T> componentType, Consumer<T> consumer);

        /**
         * Registers the given component type.
         *
         * @param componentType Component type to be registered.
         */
        default void register(ComponentType<V, H, ?> componentType)
        {
            register(componentType, component -> {
            });
        }

        /**
         * Registers all given component types.
         *
         * @param componentTypes Component types to register.
         */
        default void register(ComponentType<V, H, ?>... componentTypes)
        {
            for(var componentType : componentTypes)
            {
                register(componentType);
            }
        }
    }
}
