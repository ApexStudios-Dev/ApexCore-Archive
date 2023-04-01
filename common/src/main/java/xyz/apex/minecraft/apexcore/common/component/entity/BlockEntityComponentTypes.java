package xyz.apex.minecraft.apexcore.common.component.entity;

import xyz.apex.minecraft.apexcore.common.component.entity.types.*;

public interface BlockEntityComponentTypes
{
    BlockEntityComponentType<NameableBlockEntityComponent> NAMEABLE = NameableBlockEntityComponent.COMPONENT_TYPE;
    BlockEntityComponentType<MenuProviderBlockEntityComponent> MENU_PROVIDER = MenuProviderBlockEntityComponent.COMPONENT_TYPE;
    BlockEntityComponentType<ContainerBlockEntityComponent> CONTAINER = ContainerBlockEntityComponent.COMPONENT_TYPE;
    BlockEntityComponentType<LootableContainerBlockEntityComponent> LOOTABLE = LootableContainerBlockEntityComponent.COMPONENT_TYPE;
    BlockEntityComponentType<LockableContainerBlockEntityComponent> LOCKABLE = LockableContainerBlockEntityComponent.COMPONENT_TYPE;

    static void bootstrap() {}
}
