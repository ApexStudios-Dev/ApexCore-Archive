package xyz.apex.minecraft.apexcore.neoforge.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.types.*;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ApexDataProvider;
import xyz.apex.minecraft.apexcore.neoforge.lib.EventBuses;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
public final class ApexCoreClientImpl implements ApexCoreClient
{
    @Override
    public void bootstrap()
    {
        ApexCoreClient.super.bootstrap();
        setupEvents();
    }

    private void setupEvents()
    {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientPlayerNetworkEvent.LoggingIn.class, event -> ClientConnectionEvents.LOGGING_IN.post().handle(event.getPlayer(), event.getMultiPlayerGameMode(), event.getConnection()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientPlayerNetworkEvent.LoggingOut.class, event -> ClientConnectionEvents.LOGGING_OUT.post().handle(event.getConnection()));

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TickEvent.ClientTickEvent.class, event -> {
            switch(event.phase)
            {
                case START -> TickEvents.START_CLIENT.post().handle();
                case END -> TickEvents.END_CLIENT.post().handle();
            }
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TickEvent.RenderTickEvent.class, event -> {
            switch(event.phase)
            {
                case START -> TickEvents.START_RENDER.post().handle(event.renderTickTime);
                case END -> TickEvents.END_RENDER.post().handle(event.renderTickTime);
            }
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, InputEvent.Key.class, event -> InputEvents.KEY.post().handle(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, InputEvent.InteractionKeyMappingTriggered.class, event -> InputEvents.CLICK.post().handle(event.isAttack(), event.isUseItem(), event.isPickBlock(), event.getHand()));

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ScreenEvent.Init.Post.class, event -> {
            var screen = event.getScreen();
            ScreenEvents.INIT.post().handle(screen);
            var widgets = event.getListenersList().stream().filter(AbstractWidget.class::isInstance).map(AbstractWidget.class::cast).toList();
            ScreenEvents.MODIFY_WIDGETS.post().handle(screen, widgets, event::addListener, event::removeListener);
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ScreenEvent.Render.Pre.class, event -> ScreenEvents.PRE_RENDER.post().handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ScreenEvent.Render.Post.class, event -> ScreenEvents.POST_RENDER.post().handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, false, ScreenEvent.Opening.class, event -> ScreenEvents.OPENED.post().handle(event.getScreen()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, false, ScreenEvent.Opening.class, event -> ScreenEvents.CLOSED.post().handle(event.getScreen()));

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

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, false, MovementInputUpdateEvent.class, event -> ClientEvents.PLAYER_INPUT.post().handle(event.getEntity(), event.getInput()));

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

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderHighlightEvent.Block.class, event -> {
            if(LevelRendererEvents.BLOCK_HIGHLIGHT.post().handle(event.getLevelRenderer(), event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick(), event.getCamera()))
                event.setCanceled(true);
        });

        EventBuses.addListener(ApexCore.ID, eventBus -> eventBus.addListener(EventPriority.NORMAL, false, GatherDataEvent.class, event -> ApexDataProvider.register(func -> {
            var generator = event.getGenerator();
            generator.addProvider(
                    event.includeClient() || event.includeServer(),
                    func.apply(generator.getPackOutput(), event.getLookupProvider())
            );
        })));
    }
}
