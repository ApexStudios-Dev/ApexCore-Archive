package xyz.apex.minecraft.apexcore.common.lib.multiblock;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface MultiBlockTypes
{
    // top left 0,0,0
    MultiBlockType MB_3x3x3 = MultiBlockType.builder()
                .with("XXX", "XXX", "XXX")
                .with("XXX", "X X", "XXX")
                .with("XXX", "XXX", "XXX")
            .build();

    @ApiStatus.Internal
    static void bootstrap()
    {
    }
}
