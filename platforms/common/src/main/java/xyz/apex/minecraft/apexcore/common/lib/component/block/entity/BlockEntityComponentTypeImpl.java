package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

final class BlockEntityComponentTypeImpl<T extends BlockEntityComponent> implements BlockEntityComponentType<T>
{
    private static final Map<ResourceLocation, BlockEntityComponentType<?>> REGISTRY = Maps.newHashMap();

    private final ResourceLocation registryName;
    private final BlockComponentFactory<T> componentFactory;

    private BlockEntityComponentTypeImpl(ResourceLocation registryName, BlockComponentFactory<T> componentFactory)
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
    public T newInstance(BlockEntityComponentHolder componentHolder)
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
        if(!(obj instanceof BlockEntityComponentType<?> other))
            return false;
        return registryName.equals(other.registryName());
    }

    @Override
    public String toString()
    {
        return "BlockComponentType<%s>".formatted(registryName);
    }

    static <T extends BlockEntityComponent> BlockEntityComponentType<T> register(String ownerId, String componentName, BlockComponentFactory<T> componentFactory)
    {
        var componentType = new BlockEntityComponentTypeImpl<>(new ResourceLocation(ownerId, componentName), componentFactory);

        if(REGISTRY.put(componentType.registryName, componentType) != null)
            throw new IllegalStateException("Attempt to register Block ComponentType with duplicate registry name: '%s'".formatted(componentType.registryName));

        return componentType;
    }

    @Nullable
    static BlockEntityComponentType<?> byName(ResourceLocation registryName)
    {
        return REGISTRY.get(registryName);
    }
}
