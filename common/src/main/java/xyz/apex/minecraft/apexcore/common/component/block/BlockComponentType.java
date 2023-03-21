package xyz.apex.minecraft.apexcore.common.component.block;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public sealed interface BlockComponentType<T extends BlockComponent> permits BlockComponentType.Impl
{
    ResourceLocation registryName();

    T getFor(BlockComponentHolder holder);

    Set<BlockComponentType<?>> requiredTypes();

    static <T extends BlockComponent> BlockComponentType<T> register(ResourceLocation registryName, Function<BlockComponentHolder, T> componentFactory, BlockComponentType<?>... requiredTypes)
    {
        return Impl.register(registryName, componentFactory, requiredTypes);
    }

    static <T extends BlockComponent> BlockComponentType<T> register(ResourceLocation registryName, Function<BlockComponentHolder, T> componentFactory)
    {
        return Impl.register(registryName, componentFactory);
    }

    final class Impl<T extends BlockComponent> implements BlockComponentType<T>
    {
        private static final Map<ResourceLocation, BlockComponentType<?>> registry = Maps.newHashMap();

        private final ResourceLocation registryName;
        private final Set<BlockComponentType<?>> requiredTypes;
        private final Map<BlockComponentHolder, T> components = Maps.newHashMap();
        private final Function<BlockComponentHolder, T> componentFactory;

        private Impl(ResourceLocation registryName, Function<BlockComponentHolder, T> componentFactory, BlockComponentType<?>... requiredTypes)
        {
            this.registryName = registryName;
            this.requiredTypes = ImmutableSet.copyOf(requiredTypes);
            this.componentFactory = componentFactory;
        }

        @Override
        public ResourceLocation registryName()
        {
            return registryName;
        }

        @Override
        public T getFor(BlockComponentHolder holder)
        {
            return components.computeIfAbsent(holder, componentFactory);
        }

        @Override
        public Set<BlockComponentType<?>> requiredTypes()
        {
            return requiredTypes;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj) return true;
            if(!(obj instanceof BlockComponentType<?> other)) return false;
            return registryName.equals(other.registryName());
        }

        @Override
        public int hashCode()
        {
            return registryName.hashCode();
        }

        @Override
        public String toString()
        {
            return "BlockComponentType[%s]".formatted(requiredTypes);
        }

        private static <T extends BlockComponent> BlockComponentType<T> register(ResourceLocation registryName, Function<BlockComponentHolder, T> componentFactory, BlockComponentType<?>... requiredTypes)
        {
            var instance = new Impl<>(registryName, componentFactory, requiredTypes);
            if(registry.put(registryName, instance) != null) throw new IllegalStateException("Attempt to register duplicate BlockComponentType: '%s'".formatted(registryName));
            return instance;
        }
    }
}
