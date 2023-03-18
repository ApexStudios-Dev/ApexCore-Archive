package xyz.apex.minecraft.apexcore.common.component;

import xyz.apex.minecraft.apexcore.common.component.types.BedComponent;
import xyz.apex.minecraft.apexcore.common.component.types.DoorComponent;
import xyz.apex.minecraft.apexcore.common.component.types.HorizontalFacingComponent;

public interface ComponentTypes
{
    ComponentType<HorizontalFacingComponent> HORIZONTAL_FACING = HorizontalFacingComponent.COMPONENT_TYPE;
    ComponentType<BedComponent> BED = BedComponent.COMPONENT_TYPE;
    ComponentType<DoorComponent> DOOR = DoorComponent.COMPONENT_TYPE;
}
