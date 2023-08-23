package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.core.client.BlockPlacementRenderer;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.Internal
public final class ApexCoreClientImpl implements ApexCoreClient
{
    @Override
    public void bootstrap()
    {
        ApexCoreClient.super.bootstrap();

        WorldRenderEvents.BLOCK_OUTLINE.register((renderContext, outlineContext) -> {
            BlockPlacementRenderer.INSTANCE.renderBlockPlacement(renderContext.matrixStack(), renderContext.camera());
            return true;
        });
    }
}
