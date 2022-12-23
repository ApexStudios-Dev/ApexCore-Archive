package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent;

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

            ClientLifecycleEvents.CLIENT_STARTED.register(this::onClientStarted);
        }

        private void onClientStarted(Minecraft mc)
        {
            CreativeModeTabEvent.REGISTER.post(new CreativeModeTabEvent.Register((registryName, beforeEntries, afterEntries, configurator) -> {
                var builder = FabricItemGroup.builder(new ResourceLocation(modId, registryName));
                configurator.accept(builder);
                return builder.build();
            }));
        }
    }
}
