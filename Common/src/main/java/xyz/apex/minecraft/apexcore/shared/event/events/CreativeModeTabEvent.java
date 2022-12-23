package xyz.apex.minecraft.apexcore.shared.event.events;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import xyz.apex.minecraft.apexcore.shared.event.Event;
import xyz.apex.minecraft.apexcore.shared.event.SimpleEvent;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface CreativeModeTabEvent
{
    Event<Register> REGISTER = new SimpleEvent<>();
    Event<BuildContents> BUILD_CONTENTS = new SimpleEvent<>();

    @FunctionalInterface
    interface Registrar
    {
        CreativeModeTab registerCreativeModeTab(String registryName, List<Object> beforeEntries, List<Object> afterEntries, Consumer<CreativeModeTab.Builder> configurator);
    }

    record Register(Registrar registrar)
    {
        private static final List<Object> DEFAULT_AFTER_ENTRIES = List.of(CreativeModeTabs.SPAWN_EGGS);

        public CreativeModeTab registerCreativeModeTab(String registryName, List<Object> beforeEntries, List<Object> afterEntries, Consumer<CreativeModeTab.Builder> configurator)
        {
            return registrar.registerCreativeModeTab(registryName, beforeEntries, afterEntries, configurator);
        }

        public CreativeModeTab registerCreativeModeTab(String registryName, Consumer<CreativeModeTab.Builder> configurator)
        {
            return registerCreativeModeTab(registryName, List.of(), DEFAULT_AFTER_ENTRIES, configurator);
        }
    }

    final class BuildContents implements CreativeModeTab.Output
    {
        private final CreativeModeTab tab;
        private final FeatureFlagSet flags;
        private final boolean hasPermissions;
        private final BiConsumer<ItemStack, CreativeModeTab.TabVisibility> consumer;

        public BuildContents(CreativeModeTab tab, FeatureFlagSet flags, boolean hasPermissions, BiConsumer<ItemStack, CreativeModeTab.TabVisibility> consumer)
        {
            this.tab = tab;
            this.flags = flags;
            this.hasPermissions = hasPermissions;
            this.consumer = consumer;
        }

        public CreativeModeTab getTab()
        {
            return tab;
        }

        public FeatureFlagSet getFlags()
        {
            return flags;
        }

        public boolean hasPermissions()
        {
            return hasPermissions;
        }

        @Override
        public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility)
        {
            consumer.accept(stack, tabVisibility);
        }
    }
}
