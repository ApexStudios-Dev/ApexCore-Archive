package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent;
import xyz.apex.minecraft.apexcore.shared.platform.EnvironmentExecutor;

import java.util.Map;

public final class FabricPlatformModEvents extends FabricPlatformHolder
{
    private final Map<String, ModEvents> modEvents = Maps.newHashMap();

    FabricPlatformModEvents(FabricPlatform platform)
    {
        super(platform);
    }

    void register(String modId)
    {
        if(!modEvents.containsKey(modId)) modEvents.put(modId, new ModEvents(platform, modId));
    }

    private record ModEvents(FabricPlatform platform, String modId)
    {
        private ModEvents
        {
            platform.getLogger().debug("Registering Fabric events for mod: {}", modId);

            EnvironmentExecutor.runForClient(() -> () -> ClientLifecycleEvents.CLIENT_STARTED.register(this::onClientStarted));
            ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        }

        private void registerCreativeModeTabs()
        {
            CreativeModeTabEvent.REGISTER.post(new CreativeModeTabEvent.Register((registryName, beforeEntries, afterEntries, configurator) -> {
                var builder = FabricItemGroup.builder(new ResourceLocation(modId, registryName));
                configurator.accept(builder);
                return builder.build();
            }));
        }

        @Environment(EnvType.CLIENT)
        private void onClientStarted(Minecraft mc)
        {
            registerCreativeModeTabs();
        }

        private void onServerStarted(MinecraftServer server)
        {
            if(platform.isClient()) return;
            registerCreativeModeTabs();
        }
    }
}
