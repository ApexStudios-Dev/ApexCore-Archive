package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface BlockEntityComponentTypes
{
    /**
     * Registers and returns new block entity component type.
     *
     * @param ownerId          Owner id to register component type for.
     * @param registrationName Registration name of component type.
     * @param componentFactory Factory used to construct components.
     * @param <T>              Type of component.
     * @return Newly registered item component type.
     */
    static <T extends BlockEntityComponent> BlockEntityComponentType<T> register(String ownerId, String registrationName, BlockEntityComponentFactory<T> componentFactory)
    {
        return BlockEntityComponentType.register(ownerId, registrationName, componentFactory);
    }

    static void bootstrap()
    {
    }
}
