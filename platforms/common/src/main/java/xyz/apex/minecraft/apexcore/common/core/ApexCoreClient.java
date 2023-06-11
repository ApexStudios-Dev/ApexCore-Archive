package xyz.apex.minecraft.apexcore.common.core;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.client.BlockPlacementRenderer;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

@ApiStatus.Internal
@ApiStatus.NonExtendable
@SideOnly(PhysicalSide.CLIENT)
public interface ApexCoreClient
{
    default void bootstrap()
    {
        BlockPlacementRenderer.INSTANCE.register();
    }
}
