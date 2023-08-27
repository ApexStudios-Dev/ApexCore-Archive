package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.core.client.BlockPlacementRenderer;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.support.SupportManager;

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

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> SupportManager.INSTANCE.loadFromRemote());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> SupportManager.INSTANCE.loadFromRemote());

        if(ApexCore.IS_EARLY_BUILD)
        {
            HudRenderCallback.EVENT.register((graphics, partialTick) -> renderEarlyBuildOverlay(graphics, true));
            ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> ScreenEvents.afterRender(screen).register(($, graphics, mouseX, mouseY, tickDelta) -> renderEarlyBuildOverlay(graphics, false)));
        }
    }
}
