package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

final class ForgePlatformModEvents extends ForgePlatformHolder
{
    private final Map<String, ModEvents> modBuses = Maps.newHashMap();
    private final Map<String, List<Pair<Block, Supplier<Supplier<RenderType>>>>> renderTypeRegistrations = Maps.newHashMap();

    ForgePlatformModEvents(ForgePlatform platform)
    {
        super(platform);
    }

    @OnlyIn(Dist.CLIENT)
    void registerRenderType(String modId, Block block, Supplier<Supplier<RenderType>> renderTypeSupplier)
    {
        renderTypeRegistrations.computeIfAbsent(modId, $ -> Lists.newArrayList()).add(Pair.of(block, renderTypeSupplier));
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

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(this::onClientSetup));
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

        @OnlyIn(Dist.CLIENT)
        private void onClientSetup(FMLClientSetupEvent event)
        {
            var registrations = platform.modEvents.renderTypeRegistrations.get(modId);
            if(registrations == null || registrations.isEmpty()) return;

            registrations.forEach(entry -> {
                var renderType = entry.getRight().get().get();
                if(renderType != null) ItemBlockRenderTypes.setRenderLayer(entry.getLeft(), renderType);
            });
        }
    }
}
