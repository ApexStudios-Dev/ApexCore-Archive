package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

@ApiStatus.NonExtendable
public interface BlockComponentTypes
{
    BlockComponentType<FluidLoggedBlockComponent> WATERLOGGED = FluidLoggedBlockComponent.WATER;
    BlockComponentType<FluidLoggedBlockComponent> LAVALOGGED = FluidLoggedBlockComponent.LAVA;
    BlockComponentType<HorizontalFacingBlockComponent> HORIZONTAL_FACING = HorizontalFacingBlockComponent.COMPONENT_TYPE;

    @ApiStatus.Internal
    static void bootstrap()
    {
    }
}
