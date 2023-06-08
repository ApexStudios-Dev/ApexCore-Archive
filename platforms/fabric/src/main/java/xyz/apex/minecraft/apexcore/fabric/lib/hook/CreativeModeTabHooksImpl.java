package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

final class CreativeModeTabHooksImpl implements CreativeModeTabHooks
{

    @Override
    public void register(String ownerId, String registrationName, UnaryOperator<CreativeModeTab.Builder> builder)
    {
        builder.apply(FabricItemGroup.builder(new ResourceLocation(ownerId, registrationName))
                // while fabric sets a title component by default
                // we are replacing it to ensure that
                // both forge & fabric use the same translation keys
                //
                // set the title before applying the UnaryOperator so that
                // mods can override the title if they wanted to
                .title(CreativeModeTabHooks.getDisplayName(ownerId, registrationName))
        ).build();
    }

    @Override
    public void modify(CreativeModeTab creativeModeTab, Consumer<CreativeModeTab.Output> modifier)
    {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(modifier::accept);
    }

    @Override
    public void modify(String ownerId, String registrationName, Consumer<CreativeModeTab.Output> modifier)
    {
        ItemGroupEvents.modifyEntriesEvent(new ResourceLocation(ownerId, registrationName)).register(modifier::accept);
    }
}
