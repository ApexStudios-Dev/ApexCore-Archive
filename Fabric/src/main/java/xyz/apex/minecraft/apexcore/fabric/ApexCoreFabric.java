package xyz.apex.minecraft.apexcore.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import xyz.apex.minecraft.apexcore.shared.ApexCore;
import xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent;

public final class ApexCoreFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ApexCore.bootstrap();

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> CreativeModeTabEvent.BUILD_CONTENTS.post(new CreativeModeTabEvent.BuildContents(
                group,
                entries.getEnabledFeatures(),
                entries.shouldShowOpRestrictedItems(),
                entries::accept
        )));
    }
}
