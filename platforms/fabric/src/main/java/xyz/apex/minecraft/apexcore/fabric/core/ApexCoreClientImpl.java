package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.types.*;

import java.util.Objects;

@SideOnly(PhysicalSide.CLIENT)
public final class ApexCoreClientImpl implements ApexCoreClient, ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        bootstrap();
        setupEvents();
    }

    private void setupEvents()
    {
        ClientTickEvents.START_CLIENT_TICK.register(client -> TickEvents.START_CLIENT.post().handle());
        ClientTickEvents.END_CLIENT_TICK.register(client -> TickEvents.END_CLIENT.post().handle());

        // should be non-null when invoked
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ClientConnectionEvents.LOGGING_IN.post().handle(Objects.requireNonNull(client.player), Objects.requireNonNull(client.gameMode), handler.getConnection()));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientConnectionEvents.LOGGING_OUT.post().handle(handler.getConnection()));

        net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenEvents.INIT.post().handle(screen);

            var widgets = Screens.getButtons(screen);
            ScreenEvents.MODIFY_WIDGETS.post().handle(screen, widgets, widgets::add, widgets::remove);
        });

        net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.beforeRender(screen).register((screen1, graphics, mouseX, mouseY, partialTick) -> ScreenEvents.PRE_RENDER.post().handle(screen1, graphics, mouseX, mouseY, partialTick));
            net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.afterRender(screen).register((screen1, graphics, mouseX, mouseY, partialTick) -> ScreenEvents.POST_RENDER.post().handle(screen1, graphics, mouseX, mouseY, partialTick));
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ClientEvents.STARTING.post().handle(client);
            ClientEvents.STARTED.post().handle(client);
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ClientEvents.STOPPING.post().handle(client);
            ClientEvents.STOPPED.post().handle(client);
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> LevelRendererEvents.AFTER_ENTITIES.post().handle(
                context.worldRenderer(),
                context.matrixStack(),
                context.projectionMatrix(),
                context.tickDelta(),
                context.camera(),
                Objects.requireNonNull(context.frustum()) // none null during this event
        ));

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> LevelRendererEvents.AFTER_TRANSLUCENT.post().handle(
                context.worldRenderer(),
                context.matrixStack(),
                context.projectionMatrix(),
                context.tickDelta(),
                context.camera(),
                Objects.requireNonNull(context.frustum()) // none null during this event
        ));

        WorldRenderEvents.BLOCK_OUTLINE.register((renderContext, outlineContext) -> {
            var canceled = LevelRendererEvents.BLOCK_HIGHLIGHT.post().handle(
                    renderContext.worldRenderer(),
                    renderContext.matrixStack(),
                    Minecraft.getInstance().renderBuffers().bufferSource(),
                    renderContext.tickDelta(),
                    renderContext.camera()
            );

            // invert because fabric wants invert of what forge wants
            return !canceled;
        });
    }
}
