package xyz.apex.minecraft.apexcore.forge.lib.hook;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.event.CreativeModeTabEvent;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.forge.core.ModEvents;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

final class CreativeModeTabHooksImpl implements CreativeModeTabHooks
{
    @Override
    public void register(String ownerId, String registrationName, UnaryOperator<CreativeModeTab.Builder> builder)
    {
        ModEvents.active().addListener(CreativeModeTabEvent.Register.class, event -> event.registerCreativeModeTab(
                new ResourceLocation(ownerId, registrationName),
                // set title to match translation key fabric uses
                //
                // set the title before applying the UnaryOperator so that
                // mods can override the title if they wanted to
                bdr -> builder.apply(bdr.title(CreativeModeTabHooks.getDisplayName(ownerId, registrationName)))
        ));
    }

    @Override
    public void modify(CreativeModeTab creativeModeTab, Consumer<CreativeModeTab.Output> modifier)
    {
        ModEvents.active().addListener(CreativeModeTabEvent.BuildContents.class, event -> {
            if(event.getTab() != creativeModeTab) return;
            modifier.accept(event);
        });
    }

    @Override
    public void modify(String ownerId, String registrationName, Consumer<CreativeModeTab.Output> modifier)
    {
        ModEvents.active().addListener(CreativeModeTabEvent.BuildContents.class, event -> {
            if(event.getTab() != CreativeModeTabRegistry.getTab(new ResourceLocation(ownerId, registrationName)))
                return;
            modifier.accept(event);
        });
    }
}
