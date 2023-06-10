package xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;

@ApiStatus.NonExtendable
public interface BlockEntityComponentTypes
{
    BlockEntityComponentType<InventoryBlockEntityComponent> INVENTORY = InventoryBlockEntityComponent.COMPONENT_TYPE;
    BlockEntityComponentType<NameableBlockEntityComponent> NAMEABLE = NameableBlockEntityComponent.COMPONENT_TYPE;

    @ApiStatus.Internal
    static void bootstrap()
    {
    }
}
