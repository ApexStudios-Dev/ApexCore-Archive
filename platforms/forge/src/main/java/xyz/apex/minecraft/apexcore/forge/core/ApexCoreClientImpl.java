package xyz.apex.minecraft.apexcore.forge.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ClientEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.LevelRendererEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ScreenEvents;
import xyz.apex.minecraft.apexcore.forge.lib.EventBuses;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
public final class ApexCoreClientImpl implements ApexCoreClient
{
    ApexCoreClientImpl()
    {
        bootstrap();
        setupEvents();
    }

    private void setupEvents()
    {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ScreenEvent.Init.Post.class, event -> {
            var screen = event.getScreen();
            ScreenEvents.INIT.post().handle(screen);
            var widgets = event.getListenersList().stream().filter(AbstractWidget.class::isInstance).map(AbstractWidget.class::cast).toList();
            ScreenEvents.MODIFY_WIDGETS.post().handle(screen, widgets, event::addListener, event::removeListener);
        });

        ApexCoreImpl.wrapEvent(ScreenEvent.Render.Pre.class, MinecraftForge.EVENT_BUS, ScreenEvents.PRE_RENDER, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getScreen(), forgeEvent.getGuiGraphics(), forgeEvent.getMouseX(), forgeEvent.getMouseY(), forgeEvent.getPartialTick()));
        ApexCoreImpl.wrapEvent(ScreenEvent.Render.Post.class, MinecraftForge.EVENT_BUS, ScreenEvents.POST_RENDER, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getScreen(), forgeEvent.getGuiGraphics(), forgeEvent.getMouseX(), forgeEvent.getMouseY(), forgeEvent.getPartialTick()));

        EventBuses.addListener(ApexCore.ID, bus -> bus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, event -> {
            var client = Minecraft.getInstance();
            ClientEvents.STARTING.post().handle(client);
            event.enqueueWork(() -> ClientEvents.STARTED.post().handle(client));
        }));

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, GameShuttingDownEvent.class, event -> {
            var client = Minecraft.getInstance();
            ClientEvents.STOPPING.post().handle(client);
            ClientEvents.STOPPED.post().handle(client);
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderLevelStageEvent.class, event -> {
            var levelRenderer = event.getLevelRenderer();
            var pose = event.getPoseStack();
            var projection = event.getProjectionMatrix();
            var partialTick = event.getPartialTick();
            var camera = event.getCamera();
            var frustum = event.getFrustum();
            var stage = event.getStage();

            if(stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES)
                LevelRendererEvents.AFTER_ENTITIES.post().handle(levelRenderer, pose, projection, partialTick, camera, frustum);
            else if(stage == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
                LevelRendererEvents.AFTER_TRANSLUCENT.post().handle(levelRenderer, pose, projection, partialTick, camera, frustum);
        });

        ApexCoreImpl.wrapEvent(RenderHighlightEvent.Block.class, MinecraftForge.EVENT_BUS, LevelRendererEvents.BLOCK_HIGHLIGHT, (forgeEvent, ourEvent) -> {
            if(ourEvent.handle(forgeEvent.getLevelRenderer(), forgeEvent.getPoseStack(), forgeEvent.getMultiBufferSource(), forgeEvent.getPartialTick(), forgeEvent.getCamera()))
                forgeEvent.setCanceled(true);
        });
    }
}
