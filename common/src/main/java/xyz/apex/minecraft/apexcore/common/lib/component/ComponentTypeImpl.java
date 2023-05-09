package xyz.apex.minecraft.apexcore.common.lib.component;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Objects;

/**
 * Internal class for subtypes to extend, rather than implementing this class everywhere.
 *
 * @param <V> Base game object type.
 * @param <H> Component holder type.
 * @param <T> Component type.
 */
@ApiStatus.Internal
public non-sealed class ComponentTypeImpl<V, H extends ComponentHolder<V, H>, T extends Component<V, H>> implements ComponentType<V, H, T>
{
    private final Class<V> baseType;
    private final ResourceLocation registryName;
    private final ComponentFactory<V, H, T> componentFactory;
    private final Map<V, T> componentMap = Maps.newHashMap();

    protected ComponentTypeImpl(Class<V> baseType, ResourceLocation registryName, ComponentFactory<V, H, T> componentFactory)
    {
        this.baseType = baseType;
        this.registryName = registryName;
        this.componentFactory = componentFactory;
    }

    @Override
    public final Class<V> baseType()
    {
        return baseType;
    }

    @Override
    public final ResourceLocation registryName()
    {
        return registryName;
    }

    @Override
    public final T get(H componentHolder)
    {
        return componentMap.computeIfAbsent(componentHolder.toGameObject(), $ -> componentFactory.create(componentHolder));
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(!(obj instanceof ComponentType<?, ?, ?> componentType)) return false;
        return baseType == componentType.baseType() && registryName.equals(componentType.registryName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(baseType, registryName);
    }

    @Override
    public String toString()
    {
        return "ComponentType<%s>(%s)".formatted(baseType.getName(), registryName);
    }
}
