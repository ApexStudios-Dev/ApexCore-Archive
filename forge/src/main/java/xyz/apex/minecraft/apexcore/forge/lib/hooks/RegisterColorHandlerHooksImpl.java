package xyz.apex.minecraft.apexcore.forge.lib.hooks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterColorHandlerHooks;
import xyz.apex.minecraft.apexcore.forge.lib.EventBuses;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
final class RegisterColorHandlerHooksImpl implements RegisterColorHandlerHooks
{
    private final Map<String, ModData> modDataMap = Maps.newConcurrentMap();

    @Override
    public void registerBlockColor(Supplier<? extends Block> block, Supplier<Supplier<BlockColor>> blockColor)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        active().blockColorHandlers.add(Pair.of(block, blockColor));
    }

    @Override
    public void registerItemColor(Supplier<ItemLike> item, Supplier<Supplier<ItemColor>> itemColor)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        active().itemColorHandlers.add(Pair.of(item, itemColor));
    }

    private ModData active()
    {
        return modDataMap.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), ModData::new);
    }

    private static final class ModData
    {
        private final List<Pair<Supplier<? extends Block>, Supplier<Supplier<BlockColor>>>> blockColorHandlers = Lists.newArrayList();
        private final List<Pair<Supplier<ItemLike>, Supplier<Supplier<ItemColor>>>> itemColorHandlers = Lists.newArrayList();

        private final String modId;

        private ModData(String modId)
        {
            this.modId = modId;

            EventBuses.addListener(modId, modBus -> {
                modBus.addListener(this::onRegisterBlockColorHandler);
                modBus.addListener(this::onRegisterItemColorHandler);
                modBus.addListener(this::onLoadComplete);
            });
        }

        private void onRegisterBlockColorHandler(RegisterColorHandlersEvent.Block event)
        {
            blockColorHandlers.forEach(pair -> event.register(pair.getValue().get().get(), pair.getKey().get()));
            blockColorHandlers.clear();
        }

        private void onRegisterItemColorHandler(RegisterColorHandlersEvent.Item event)
        {
            itemColorHandlers.forEach(pair -> event.register(pair.getValue().get().get(), pair.getKey().get()));
            itemColorHandlers.clear();
        }

        private void onLoadComplete(FMLLoadCompleteEvent event)
        {
            if(!blockColorHandlers.isEmpty())
                ApexCore.LOGGER.warn("Failed to register '{}' block color handlers for mod '{}'!", blockColorHandlers.size(), modId);
            if(!itemColorHandlers.isEmpty())
                ApexCore.LOGGER.warn("Failed to register '{}' item color handlers for mod '{}'!", itemColorHandlers.size(), modId);
        }
    }
}
