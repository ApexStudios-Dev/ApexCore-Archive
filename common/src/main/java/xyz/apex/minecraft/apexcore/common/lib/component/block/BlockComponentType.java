package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentTypeImpl;

import java.util.Map;

/**
 * Component type implementation for all block component types.
 *
 * @param <T> Block component type.
 */
public final class BlockComponentType<T extends BlockComponent> extends ComponentTypeImpl<Block, BlockComponentHolder, T>
{
    private static final Map<ResourceLocation, BlockComponentType<?>> COMPONENT_TYPE_MAP = Maps.newHashMap();

    private BlockComponentType(ResourceLocation registryName, BlockComponentFactory<T> componentFactory)
    {
        super(Block.class, registryName, componentFactory);
    }

    /**
     * Registers and returns new block component type.
     *
     * @param ownerId          Owner id to register component type for.
     * @param registrationName Registration name of component type.
     * @param componentFactory Factory used to construct components.
     * @param <T>              Type of component.
     * @return Newly registered item component type.
     */
    public static <T extends BlockComponent> BlockComponentType<T> register(String ownerId, String registrationName, BlockComponentFactory<T> componentFactory)
    {
        var registryName = new ResourceLocation(ownerId, registrationName);
        var instance = new BlockComponentType<>(registryName, componentFactory);
        if(COMPONENT_TYPE_MAP.put(registryName, instance) != null)
            throw new IllegalStateException("Attempt to register duplicate Block ComponentType: %s".formatted(registrationName));
        return instance;
    }
}
