package xyz.apex.minecraft.apexcore.forge.lib.hooks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterRendererHooks;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
final class RegisterRendererHooksImpl implements RegisterRendererHooks
{
    private final Map<String, ModData> modDataMap = Maps.newConcurrentMap();

    @Override
    public void setBlockRenderType(Supplier<? extends Block> block, Supplier<Supplier<RenderType>> renderType)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        active().blockRenderTypes.add(Pair.of(block, renderType));
    }

    @Override
    public void setFluidRenderType(Supplier<? extends Fluid> fluid, Supplier<Supplier<RenderType>> renderType)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        active().fluidRenderTypes.add(Pair.of(fluid, renderType));
    }

    private ModData active()
    {
        return modDataMap.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), ModData::new);
    }

    private static final class ModData
    {
        private final List<Pair<Supplier<? extends Block>, Supplier<Supplier<RenderType>>>> blockRenderTypes = Lists.newArrayList();
        private final List<Pair<Supplier<? extends Fluid>, Supplier<Supplier<RenderType>>>> fluidRenderTypes = Lists.newArrayList();

        private final String modId;

        private ModData(String modId)
        {
            this.modId = modId;

            var modBus = FMLJavaModLoadingContext.get().getModEventBus();

            modBus.addListener(this::onClientSetup);
            modBus.addListener(this::onLoadComplete);
        }

        private void onClientSetup(FMLClientSetupEvent event)
        {
            blockRenderTypes.forEach(pair -> ItemBlockRenderTypes.setRenderLayer(pair.getFirst().get(), pair.getSecond().get().get()));
            blockRenderTypes.clear();

            fluidRenderTypes.forEach(pair -> ItemBlockRenderTypes.setRenderLayer(pair.getFirst().get(), pair.getSecond().get().get()));
            fluidRenderTypes.clear();
        }

        private void onLoadComplete(FMLLoadCompleteEvent event)
        {
            if(!blockRenderTypes.isEmpty())
                ApexCore.LOGGER.warn("Failed to register '{}' block render type registrations for mod '{}'!", blockRenderTypes.size(), modId);
            if(!fluidRenderTypes.isEmpty())
                ApexCore.LOGGER.warn("Failed to register '{}' block render type registrations for mod '{}'!", fluidRenderTypes.size(), modId);
        }
    }
}
