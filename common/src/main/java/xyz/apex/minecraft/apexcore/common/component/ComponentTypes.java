package xyz.apex.minecraft.apexcore.common.component;

import xyz.apex.minecraft.apexcore.common.component.components.BedComponent;
import xyz.apex.minecraft.apexcore.common.component.components.BlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.component.components.DoorComponent;
import xyz.apex.minecraft.apexcore.common.component.components.HorizontalFacingComponent;
import xyz.apex.minecraft.apexcore.common.inventory.InventoryComponent;
import xyz.apex.minecraft.apexcore.common.multiblock.MultiBlockComponent;

public interface ComponentTypes
{
    ComponentType<BlockEntityComponent> BLOCK_ENTITY = BlockEntityComponent.COMPONENT_TYPE;
    ComponentType<HorizontalFacingComponent> HORIZONTAL_FACING = HorizontalFacingComponent.COMPONENT_TYPE;
    ComponentType<BedComponent> BED = BedComponent.COMPONENT_TYPE;
    ComponentType<DoorComponent> DOOR = DoorComponent.COMPONENT_TYPE;

    ComponentType<MultiBlockComponent> MULTI_BLOCK = MultiBlockComponent.COMPONENT_TYPE;
    ComponentType<InventoryComponent> INVENTORY = InventoryComponent.COMPONENT_TYPE;
}
