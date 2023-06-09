package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

final class BlockComponentTypeImpl<T extends BlockComponent> implements BlockComponentType<T>
{
    private static final Map<ResourceLocation, BlockComponentType<?>> REGISTRY = Maps.newHashMap();

    private final ResourceLocation registryName;
    private final BlockComponentFactory<T> componentFactory;

    private BlockComponentTypeImpl(ResourceLocation registryName, BlockComponentFactory<T> componentFactory)
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
    public T newInstance(BlockComponentHolder componentHolder)
    {
        return componentFactory.create(componentHolder);
    }

    @Override
    public int hashCode()
    {
        return registryName.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof BlockComponentType<?> other))
            return false;
        return registryName.equals(other.registryName());
    }

    @Override
    public String toString()
    {
        return "BlockComponentType<%s>".formatted(registryName);
    }

    static <T extends BlockComponent> BlockComponentType<T> register(String ownerId, String componentName, BlockComponentFactory<T> componentFactory)
    {
        var componentType = new BlockComponentTypeImpl<>(new ResourceLocation(ownerId, componentName), componentFactory);

        if(REGISTRY.put(componentType.registryName, componentType) != null)
            throw new IllegalStateException("Attempt to register Block ComponentType with duplicate registry name: '%s'".formatted(componentType.registryName));

        return componentType;
    }
}
