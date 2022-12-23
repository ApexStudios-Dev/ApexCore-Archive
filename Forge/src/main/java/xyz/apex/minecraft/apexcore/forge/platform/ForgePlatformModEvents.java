package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;

import java.util.Map;

final class ForgePlatformModEvents extends ForgePlatformHolder
{
    private final Map<String, ModEvents> modBuses = Maps.newHashMap();

    ForgePlatformModEvents(ForgePlatform platform)
    {
        super(platform);
    }

    IEventBus getModBus(String modId)
    {
        if(modBuses.containsKey(modId)) return modBuses.get(modId).modBus;
        else
        {
            // this *MUST* match inorder to obtain the correct IEventBus
            Validate.isTrue(ModLoadingContext.get().getActiveNamespace().equals(modId));

            var modEvents = new ModEvents(platform, modId, FMLJavaModLoadingContext.get().getModEventBus());
            modBuses.put(modId, modEvents);
            return modEvents.modBus;
        }
    }

    private record ModEvents(ForgePlatform platform, String modId, IEventBus modBus) implements PlatformHolder
    {
        private ModEvents
        {
            platform.getLogger().debug("Registering IModBus events for mod: {}", modId);

            modBus.addListener(this::onRegisterCreativeModeTab);
            modBus.addListener(this::onBuildCreativeModeTabContents);
        }

        private void onRegisterCreativeModeTab(CreativeModeTabEvent.Register event)
        {
            platform.getLogger().debug("Registering CreativeModeTabs for mod: {}", modId);

            xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.REGISTER.post(new xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.Register(
                    (registryName, beforeEntries, afterEntries, configurator) -> event.registerCreativeModeTab(new ResourceLocation(modId, registryName), beforeEntries, afterEntries, configurator)
            ));
        }

        private void onBuildCreativeModeTabContents(CreativeModeTabEvent.BuildContents event)
        {
            platform.getLogger().debug("Building CreativeModeTab Contents for mod: {}", modId);

            xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.BUILD_CONTENTS.post(new xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent.BuildContents(
                    event.getTab(),
                    event.getFlags(),
                    event.hasPermissions(),
                    event.getEntries()::put
            ));
        }
    }
}
