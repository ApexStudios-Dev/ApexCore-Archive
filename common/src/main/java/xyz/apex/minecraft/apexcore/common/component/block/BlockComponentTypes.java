package xyz.apex.minecraft.apexcore.common.component.block;

import xyz.apex.minecraft.apexcore.common.component.block.types.BedBlockComponent;
import xyz.apex.minecraft.apexcore.common.component.block.types.DoorBlockComponent;
import xyz.apex.minecraft.apexcore.common.component.block.types.HorizontalFacingBlockComponent;
import xyz.apex.minecraft.apexcore.common.multiblock.MultiBlockComponent;

public interface BlockComponentTypes
{
    BlockComponentType<HorizontalFacingBlockComponent> HORIZONTAL_FACING = HorizontalFacingBlockComponent.COMPONENT_TYPE;
    BlockComponentType<BedBlockComponent> BED = BedBlockComponent.COMPONENT_TYPE;
    BlockComponentType<DoorBlockComponent> DOOR = DoorBlockComponent.COMPONENT_TYPE;

    BlockComponentType<MultiBlockComponent> MULTI_BLOCK = MultiBlockComponent.COMPONENT_TYPE;

    static void bootstrap() {}
}
