package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ClientEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ScreenEvents;

@SideOnly(PhysicalSide.CLIENT)
public final class ApexCoreClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        setupEvents();
    }

    private void setupEvents()
    {
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
    }
}
