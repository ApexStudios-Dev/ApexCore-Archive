package xyz.apex.minecraft.apexcore.common.component.block;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Function;

public sealed interface BlockComponentType<T extends BlockComponent> permits BlockComponentType.Impl
{
    ResourceLocation registryName();

    T getFor(BlockComponentHolder holder);

    static <T extends BlockComponent> BlockComponentType<T> register(ResourceLocation registryName, Function<BlockComponentHolder, T> componentFactory)
    {
        return Impl.register(registryName, componentFactory);
    }

    final class Impl<T extends BlockComponent> implements BlockComponentType<T>
    {
        private static final Map<ResourceLocation, BlockComponentType<?>> registry = Maps.newHashMap();

        private final ResourceLocation registryName;
        private final Map<BlockComponentHolder, T> components = Maps.newHashMap();
        private final Function<BlockComponentHolder, T> componentFactory;

        private Impl(ResourceLocation registryName, Function<BlockComponentHolder, T> componentFactory)
        {
            this.registryName = registryName;
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
            return "BlockComponentType[%s]".formatted(registryName);
        }

        private static <T extends BlockComponent> BlockComponentType<T> register(ResourceLocation registryName, Function<BlockComponentHolder, T> componentFactory)
        {
            var instance = new Impl<>(registryName, componentFactory);
            if(registry.put(registryName, instance) != null) throw new IllegalStateException("Attempt to register duplicate BlockComponentType: '%s'".formatted(registryName));
            return instance;
        }
    }
}
