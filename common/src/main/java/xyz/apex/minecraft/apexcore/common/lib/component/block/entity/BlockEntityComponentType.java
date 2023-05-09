package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentTypeImpl;

import java.util.Map;

/**
 * Component type implementation for all block entity component types.
 *
 * @param <T> Block entity component type.
 */
public final class BlockEntityComponentType<T extends BlockEntityComponent> extends ComponentTypeImpl<BlockEntity, BlockEntityComponentHolder, T>
{
    private static final Map<ResourceLocation, BlockEntityComponentType<?>> COMPONENT_TYPE_MAP = Maps.newHashMap();

    private BlockEntityComponentType(ResourceLocation registryName, BlockEntityComponentFactory<T> componentFactory)
    {
        super(BlockEntity.class, registryName, componentFactory);
    }

    /**
     * Registers and returns new block entity component type.
     *
     * @param ownerId          Owner id to register component type for.
     * @param registrationName Registration name of component type.
     * @param componentFactory Factory used to construct components.
     * @param <T>              Type of component.
     * @return Newly registered item component type.
     */
    public static <T extends BlockEntityComponent> BlockEntityComponentType<T> register(String ownerId, String registrationName, BlockEntityComponentFactory<T> componentFactory)
    {
        var registryName = new ResourceLocation(ownerId, registrationName);
        var instance = new BlockEntityComponentType<>(registryName, componentFactory);
        if(COMPONENT_TYPE_MAP.put(registryName, instance) != null)
            throw new IllegalStateException("Attempt to register duplicate Block ComponentType: %s".formatted(registrationName));
        return instance;
    }
}
