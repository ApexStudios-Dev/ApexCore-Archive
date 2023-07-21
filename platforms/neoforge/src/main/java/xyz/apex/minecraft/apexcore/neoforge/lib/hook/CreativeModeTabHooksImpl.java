package xyz.apex.minecraft.apexcore.neoforge.lib.hook;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.neoforge.core.ModEvents;

import java.util.function.Consumer;

final class CreativeModeTabHooksImpl implements CreativeModeTabHooks
{
    @Override
    public CreativeModeTab.Builder createNewBuilder()
    {
        return CreativeModeTab.builder();
    }

    @Override
    public void modify(ResourceKey<CreativeModeTab> creativeModeTab, Consumer<CreativeModeTab.Output> modifier)
    {
        ModEvents.active().addListener(BuildCreativeModeTabContentsEvent.class,event -> {
            if(event.getTab() != CreativeModeTabRegistry.getTab(creativeModeTab.location()))
                return;
            modifier.accept(event);
        });
    }
}
