package xyz.apex.minecraft.apexcore.common.lib.component;

/**
 * Base interface used for a component factories.
 *
 * @param <V> Base game object type.
 * @param <H> Component holder type.
 * @param <T> Component type.
 */
@FunctionalInterface
public interface ComponentFactory<V, H extends ComponentHolder<V, H>, T extends Component<V, H>>
{
    /**
     * Returns new component for given component holder.
     *
     * @param componentHolder Component holder to build component for.
     * @return New component for given component holder.
     */
    T create(H componentHolder);
}
