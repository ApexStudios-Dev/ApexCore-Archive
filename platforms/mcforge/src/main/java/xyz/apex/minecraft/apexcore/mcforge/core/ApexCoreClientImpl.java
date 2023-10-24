package xyz.apex.minecraft.apexcore.mcforge.core;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.core.client.BlockPlacementRenderer;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.support.SupportManager;
import xyz.apex.minecraft.apexcore.mcforge.lib.EventBusHelper;
import xyz.apex.minecraft.apexcore.mcforge.lib.EventBuses;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
public final class ApexCoreClientImpl implements ApexCoreClient
{
    @Override
    public void bootstrap()
    {
        ApexCoreClient.super.bootstrap();

        EventBuses.addListener(ApexCore.ID, modBus -> {
            EventBusHelper.addListener(modBus, FMLLoadCompleteEvent.class, event -> SupportManager.INSTANCE.loadFromRemote());

            if(ApexCore.IS_EARLY_BUILD)
                EventBusHelper.addListener(modBus, RegisterGuiOverlaysEvent.class, event -> event.registerAboveAll("%s_early_build_overlay".formatted(ApexCore.ID), (gui, graphics, partialTick, width, height) -> renderEarlyBuildOverlay(graphics, true)));
        });

        if(ApexCore.IS_EARLY_BUILD)
            EventBusHelper.addListener(MinecraftForge.EVENT_BUS, ScreenEvent.BackgroundRendered.class, event -> renderEarlyBuildOverlay(event.getGuiGraphics(), false));

        EventBusHelper.addListener(MinecraftForge.EVENT_BUS, ClientPlayerNetworkEvent.LoggingOut.class, event -> SupportManager.INSTANCE.loadFromRemote());

        EventBusHelper.addListener(MinecraftForge.EVENT_BUS, RenderLevelStageEvent.class, event -> {
            if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES)
                BlockPlacementRenderer.INSTANCE.renderBlockPlacement(event.getPoseStack(), event.getCamera());
        });
    }
}
