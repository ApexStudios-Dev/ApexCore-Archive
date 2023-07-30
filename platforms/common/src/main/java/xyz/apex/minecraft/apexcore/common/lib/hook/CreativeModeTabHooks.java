package xyz.apex.minecraft.apexcore.common.lib.hook;


import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.function.Consumer;

/**
 * Various for registering new and modifying existing creative mode tabs.
 */
@ApiStatus.NonExtendable
public interface CreativeModeTabHooks
{
    /**
     * Returns new raw CreativeModeTab builder instance.
     * <p>
     * For internal usages only, recommended to use {@link CreativeModeTabBuilder} instead.
     *
     * @return New raw CreativeModeTab builder instance.
     */
    @ApiStatus.Internal
    CreativeModeTab.Builder createNewBuilder();

    /**
     * Registers listener to modify given creative mode tab.
     *
     * @param creativeModeTab Tab to be modified.
     * @param modifier        Listener to be invoked.
     */
    void modify(ResourceKey<CreativeModeTab> creativeModeTab, Consumer<CreativeModeTab.Output> modifier);

    /**
     * @return Global instance.
     */
    static CreativeModeTabHooks get()
    {
        return ApexCore.CREATIVE_MODE_TAB_HOOKS;
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
