package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockComponent;

@ApiStatus.NonExtendable
public interface BlockComponentTypes
{
    BlockComponentType<WaterLoggedBlockComponent> WATERLOGGED = WaterLoggedBlockComponent.COMPONENT_TYPE;
    BlockComponentType<HorizontalFacingBlockComponent> HORIZONTAL_FACING = HorizontalFacingBlockComponent.COMPONENT_TYPE;
    BlockComponentType<MultiBlockComponent> MULTI_BLOCK = MultiBlockComponent.COMPONENT_TYPE;

    @ApiStatus.Internal
    static void bootstrap()
    {
    }
}
