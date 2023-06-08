package xyz.apex.minecraft.apexcore.common.lib.hook;


import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Various for registering new and modifying existing creative mode tabs.
 */
@ApiStatus.NonExtendable
public interface CreativeModeTabHooks
{
    /**
     * Registers new creative mode tab.
     *
     * @param ownerId          ID of owner to register the tab for.
     * @param registrationName Registration name.
     * @param builder          Builder used to construct the tab.
     */
    void register(String ownerId, String registrationName, UnaryOperator<CreativeModeTab.Builder> builder);

    /**
     * Registers listener to modify given creative mode tab.
     *
     * @param creativeModeTab Tab to be modified.
     * @param modifier        Listener to be invoked.
     */
    void modify(CreativeModeTab creativeModeTab, Consumer<CreativeModeTab.Output> modifier);

    /**
     * Registers listener to modify creative mode tab for given registry name.
     *
     * @param ownerId          ID of owner tab was registered for.
     * @param registrationName Registration name for the tab.
     * @param modifier         Listener to be invoked.
     */
    void modify(String ownerId, String registrationName, Consumer<CreativeModeTab.Output> modifier);

    /**
     * @return Global instance.
     */
    static CreativeModeTabHooks get()
    {
        return Hooks.get().creativeModeTabs();
    }

    /**
     * Returns translatable component for creative mode tab translation key.
     *
     * @param ownerId          ID of owner tab was registered for.
     * @param registrationName Registration name for the tab.
     * @return Translatable component for creative mode tab translation key.
     */
    static Component getDisplayName(String ownerId, String registrationName)
    {
        return Component.translatable(Util.makeDescriptionId("itemGroup", new ResourceLocation(ownerId, registrationName)));
    }
}
