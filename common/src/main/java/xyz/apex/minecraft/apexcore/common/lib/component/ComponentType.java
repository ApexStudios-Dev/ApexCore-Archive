package xyz.apex.minecraft.apexcore.common.lib.component;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.component.item.ItemComponentTypes;

/**
 * Base interface for all component types.
 *
 * @param <V> Base game object type.
 * @param <H> Component holder type.
 * @param <T> Component type.
 */
public sealed interface ComponentType<V, H extends ComponentHolder<V, H>, T extends Component<V, H>> permits ComponentTypeImpl
{
    /**
     * @return Base game object class type.
     */
    Class<V> baseType();

    /**
     * @return Registry name for this component type.
     */
    ResourceLocation registryName();

    /**
     * Returns component instance for given component holder.
     * <p>
     * Returns the same instance for multiple invocations, if same component holder is passed.
     *
     * @param componentHolder Component holder to lookup component for
     * @return Component instance for given component holder.
     */
    T get(H componentHolder);

    @ApiStatus.Internal
    static void bootstrap()
    {
        BlockComponentTypes.bootstrap();
        ItemComponentTypes.bootstrap();
    }
}
