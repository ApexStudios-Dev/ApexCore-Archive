package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;

import java.util.function.Consumer;

@ApiStatus.Internal
public final class CreativeModeTabHooksImpl implements CreativeModeTabHooks
{
    @Override
    public CreativeModeTab.Builder createNewBuilder()
    {
        return FabricItemGroup.builder();
    }

    @Override
    public void modify(ResourceKey<CreativeModeTab> creativeModeTab, Consumer<CreativeModeTab.Output> modifier)
    {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(modifier::accept);
    }
}
