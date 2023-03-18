package xyz.apex.minecraft.apexcore.common.component;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class ComponentType<T extends Component>
{
    private static final Map<ResourceLocation, ComponentType<?>> COMPONENT_TYPE_MAP = Maps.newHashMap();

    private final ResourceLocation registryName;
    private final Class<T> componentType;
    private final Constructor<T> constructor;
    private final Set<ComponentType<?>> requiredTypes;

    private ComponentType(Builder<T> builder)
    {
        registryName = builder.registryName;
        componentType = builder.componentType;
        requiredTypes = ImmutableSet.copyOf(builder.requiredTypes);

        try
        {
            var actualArgs = new Class[builder.constructorArgs.length + 1];
            actualArgs[0] = ComponentBlock.class;
            System.arraycopy(builder.constructorArgs, 0, actualArgs, 1, builder.constructorArgs.length);
            constructor = componentType.getConstructor(actualArgs);
        }
        catch(NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ResourceLocation getRegistryName()
    {
        return registryName;
    }

    public Set<ComponentType<?>> getRequiredTypes()
    {
        return requiredTypes;
    }

    public Class<T> getComponentType()
    {
        return componentType;
    }

    T newInstance(ComponentBlock block, Object... constructorArgs)
    {
        try
        {
            var actualArgs = new Object[constructorArgs.length + 1];
            actualArgs[0] = block;
            System.arraycopy(constructorArgs, 0, actualArgs, 1, constructorArgs.length);
            return constructor.newInstance(actualArgs);
        }
        catch(InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    T cast(@Nullable Object obj)
    {
        return componentType.isInstance(obj) ? componentType.cast(obj) : null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(obj instanceof ComponentType<?> other) return registryName.equals(other.registryName);
        return false;
    }

    @Override
    public int hashCode()
    {
        return registryName.hashCode();
    }

    @Override
    public String toString()
    {
        return "ComponentType[%s]".formatted(registryName);
    }

    public static <T extends Component> Builder<T> builder(ResourceLocation registryName, Class<T> componentType, Class<?>... constructorArgs)
    {
        return new Builder<>(registryName, componentType, constructorArgs);
    }

    public static <T extends Component> ComponentType<T> register(ResourceLocation registryName, Class<T> componentType, Class<?>... constructorArgs)
    {
        return builder(registryName, componentType, constructorArgs).register();
    }

    @Nullable
    public static ComponentType<?> get(ResourceLocation registryName)
    {
        return COMPONENT_TYPE_MAP.get(registryName);
    }

    public static final class Builder<T extends Component>
    {
        private final ResourceLocation registryName;
        private final Class<T> componentType;
        private final Class<?>[] constructorArgs;
        private final Set<ComponentType<?>> requiredTypes = Sets.newHashSet();

        private Builder(ResourceLocation registryName, Class<T> componentType, Class<?>[] constructorArgs)
        {
            this.registryName = registryName;
            this.componentType = componentType;
            this.constructorArgs = constructorArgs;
        }

        public Builder<T> requires(ComponentType<?> componentType)
        {
            requiredTypes.add(componentType);
            return this;
        }

        public Builder<T> requires(ComponentType<?>... componentTypes)
        {
            requiredTypes.addAll(Arrays.asList(componentTypes));
            return this;
        }

        public Builder<T> requires(Collection<ComponentType<?>> componentTypes)
        {
            requiredTypes.addAll(componentTypes);
            return this;
        }

        public Builder<T> requires(Iterable<ComponentType<?>> componentTypes)
        {
            Iterables.addAll(requiredTypes, componentTypes);
            return this;
        }

        public ComponentType<T> register()
        {
            if(COMPONENT_TYPE_MAP.containsKey(registryName)) throw new IllegalStateException("Attempt to register duplicate component type: '%s'".formatted(registryName));

            var result = new ComponentType<>(this);
            COMPONENT_TYPE_MAP.put(registryName, result);
            return result;
        }
    }
}
