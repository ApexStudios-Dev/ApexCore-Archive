package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.DelegatedRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * CreativeModeTab builder used to construct new creative mode tab instances.
 *
 * @param <P> Type of parent element.
 * @param <M> Type of builder manager.
 */
public final class CreativeModeTabBuilder<P, M extends BuilderManager<M>> extends AbstractBuilder<P, CreativeModeTab, CreativeModeTab, RegistryEntry<CreativeModeTab>, CreativeModeTabBuilder<P, M>, M>
{
    private Function<CreativeModeTab.Builder, CreativeModeTab.Builder> propertiesModifier = Function.identity();

    CreativeModeTabBuilder(P parent, M builderManager, String registrationName)
    {
        super(parent, builderManager, Registries.CREATIVE_MODE_TAB, registrationName, DelegatedRegistryEntry::new);
    }

    /**
     * Add a new property modifier to modify the finalized creative mode tab properties.
     *
     * @param propertiesModifier Modifier used to modify the finalized creative mode tab properties.
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> properties(UnaryOperator<CreativeModeTab.Builder> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    /**
     * Sets the title for this creative mode tab.
     *
     * @param title Title to be set.
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> title(Component title)
    {
        return properties(properties -> properties.title(title));
    }

    /**
     * Sets supplier used to look up the display item stack for this creative mode tab.
     *
     * @param icon Item icon stack look up supplier.
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> icon(Supplier<ItemStack> icon)
    {
        return properties(properties -> properties.icon(icon));
    }

    /**
     * Sets the item display generator used when building list of item stacks to be displayed on this creative mode tab.
     *
     * @param displayItemsGenerator Item display generator to be used.
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator)
    {
        return properties(properties -> properties.displayItems(displayItemsGenerator));
    }

    /**
     * Marks this creative mode tab as being aligned to the right.
     *
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> alignedRight()
    {
        return properties(CreativeModeTab.Builder::alignedRight);
    }

    /**
     * Marks this creative mode tab as not rendering the title element.
     *
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> hideTitle()
    {
        return properties(CreativeModeTab.Builder::hideTitle);
    }

    /**
     * Marks this creative mode tab as having no scroll bar.
     *
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> noScrollBar()
    {
        return properties(CreativeModeTab.Builder::noScrollBar);
    }

    /**
     * Sets the background texture suffix for this creative mode tab.
     *
     * @param backgroundSuffix Background texture suffix.
     * @return This builder instance.
     */
    public CreativeModeTabBuilder<P, M> backgroundSuffix(String backgroundSuffix)
    {
        return properties(properties -> properties.backgroundSuffix(backgroundSuffix));
    }

    @Override
    protected String getDescriptionId(ProviderType.RegistryContext<CreativeModeTab, CreativeModeTab> context)
    {
        if(context.value().getDisplayName().getContents() instanceof TranslatableContents translatable)
            return translatable.getKey();

        return Util.makeDescriptionId("itemGroup", context.registryName());
    }

    @Override
    protected CreativeModeTab createObject()
    {
        return propertiesModifier.apply(CreativeModeTabHooks.get()
                .createNewBuilder()
                // set default title, to ensure all platforms share similar translation key by default
                // itemGroup.<mod_id>.<registration_name>
                .title(Component.translatable(Util.makeDescriptionId("itemGroup", getRegistryName())))
        ).build();
    }
}
