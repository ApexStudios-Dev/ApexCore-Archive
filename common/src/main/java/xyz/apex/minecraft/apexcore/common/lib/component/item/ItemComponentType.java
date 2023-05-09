package xyz.apex.minecraft.apexcore.common.lib.component.item;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentTypeImpl;

import java.util.Map;

/**
 * Component type implementation for all item component types.
 *
 * @param <T> Item component type.
 */
public final class ItemComponentType<T extends ItemComponent> extends ComponentTypeImpl<Item, ItemComponentHolder, T>
{
    private static final Map<ResourceLocation, ItemComponentType<?>> COMPONENT_TYPE_MAP = Maps.newHashMap();

    private ItemComponentType(ResourceLocation registryName, ItemComponentFactory<T> componentFactory)
    {
        super(Item.class, registryName, componentFactory);
    }

    /**
     * Registers and returns new item component type.
     *
     * @param ownerId          Owner id to register component type for.
     * @param registrationName Registration name of component type.
     * @param componentFactory Factory used to construct components.
     * @param <T>              Type of component.
     * @return Newly registered item component type.
     */
    public static <T extends ItemComponent> ItemComponentType<T> register(String ownerId, String registrationName, ItemComponentFactory<T> componentFactory)
    {
        var registryName = new ResourceLocation(ownerId, registrationName);
        var instance = new ItemComponentType<>(registryName, componentFactory);
        if(COMPONENT_TYPE_MAP.put(registryName, instance) != null)
            throw new IllegalStateException("Attempt to register duplicate Block ComponentType: %s".formatted(registrationName));
        return instance;
    }
}
