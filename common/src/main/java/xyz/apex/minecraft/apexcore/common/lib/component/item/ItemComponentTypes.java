package xyz.apex.minecraft.apexcore.common.lib.component.item;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ItemComponentTypes
{
    /**
     * Registers and returns new item component type.
     *
     * @param ownerId          Owner id to register component type for.
     * @param registrationName Registration name of component type.
     * @param componentFactory Factory used to construct components.
     * @param <T>              Type of component.
     * @return Newly registered item component type.
     */
    static <T extends ItemComponent> ItemComponentType<T> register(String ownerId, String registrationName, ItemComponentFactory<T> componentFactory)
    {
        return ItemComponentType.register(ownerId, registrationName, componentFactory);
    }

    static void bootstrap()
    {
    }
}
