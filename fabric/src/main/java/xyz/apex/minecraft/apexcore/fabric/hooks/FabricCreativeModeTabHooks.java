package xyz.apex.minecraft.apexcore.fabric.hooks;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import xyz.apex.minecraft.apexcore.common.hooks.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class FabricCreativeModeTabHooks extends FabricPlatformHolder implements CreativeModeTabHooks
{
    FabricCreativeModeTabHooks(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public void register(ResourceLocation registryName, UnaryOperator<CreativeModeTab.Builder> consumer)
    {
        consumer.apply(FabricItemGroup.builder(registryName)).build();
    }

    @Override
    public void modify(CreativeModeTab creativeModeTab, Consumer<CreativeModeTab.Output> consumer)
    {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(consumer::accept);
    }

    @Override
    public void modify(ResourceLocation registryName, Consumer<CreativeModeTab.Output> consumer)
    {
        ItemGroupEvents.modifyEntriesEvent(registryName).register(consumer::accept);
    }
}
