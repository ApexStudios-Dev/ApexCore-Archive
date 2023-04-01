package xyz.apex.minecraft.apexcore.common.component.entity;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Function;

public sealed interface BlockEntityComponentType<T extends BlockEntityComponent> permits BlockEntityComponentType.Impl
{
    ResourceLocation registryName();

    T getFor(BlockEntityComponentHolder holder);

    static <T extends BlockEntityComponent> BlockEntityComponentType<T> register(ResourceLocation registryName, Function<BlockEntityComponentHolder, T> componentFactory)
    {
        return Impl.register(registryName, componentFactory);
    }

    final class Impl<T extends BlockEntityComponent> implements BlockEntityComponentType<T>
    {
        private static final Map<ResourceLocation, BlockEntityComponentType<?>> registry = Maps.newHashMap();

        private final ResourceLocation registryName;
        private final Map<BlockEntityComponentHolder, T> components = Maps.newHashMap();
        private final Function<BlockEntityComponentHolder, T> componentFactory;

        private Impl(ResourceLocation registryName, Function<BlockEntityComponentHolder, T> componentFactory)
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
        public T getFor(BlockEntityComponentHolder holder)
        {
            return components.computeIfAbsent(holder, componentFactory);
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj) return true;
            if(!(obj instanceof BlockEntityComponentType<?> other)) return false;
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
            return "BlockEntityComponentType[%s]".formatted(registryName);
        }

        private static <T extends BlockEntityComponent> BlockEntityComponentType<T> register(ResourceLocation registryName, Function<BlockEntityComponentHolder, T> componentFactory)
        {
            var instance = new Impl<>(registryName, componentFactory);
            if(registry.put(registryName, instance) != null) throw new IllegalStateException("Attempt to register duplicate BlockComponentType: '%s'".formatted(registryName));
            return instance;
        }
    }
}
