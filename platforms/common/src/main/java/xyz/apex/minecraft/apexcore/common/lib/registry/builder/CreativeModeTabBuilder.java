package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.BaseRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * CreativeModeTab Builder implementation.
 * <p>
 * Used to build and register CreativeModeTab entries.
 *
 * @param <O> Type of Registrar.
 * @param <P> Type of Parent.
 */
public final class CreativeModeTabBuilder<O extends AbstractRegistrar<O>, P> extends AbstractBuilder<O, P, CreativeModeTab, CreativeModeTab, CreativeModeTabBuilder<O, P>, RegistryEntry<CreativeModeTab>>
{
    private Function<CreativeModeTab.Builder, CreativeModeTab.Builder> propertiesModifier = Function.identity();

    @ApiStatus.Internal
    public CreativeModeTabBuilder(O registrar, P parent, String registrationName)
    {
        super(registrar, parent, Registries.CREATIVE_MODE_TAB, registrationName);
    }

    public CreativeModeTabBuilder<O, P> properties(UnaryOperator<CreativeModeTab.Builder> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    /**
     * Sets the title for this CreativeModeTab.
     *
     * @param title Title to be set.
     * @return This Builder.
     */
    public CreativeModeTabBuilder<O, P> title(Component title)
    {
        return properties(properties -> properties.title(title));
    }

    /**
     * Sets supplier used to look up the display item stack for this CreativeModeTab.
     *
     * @param icon Item icon stack look up supplier.
     * @return This Builder.
     */
    public CreativeModeTabBuilder<O, P> icon(Supplier<ItemStack> icon)
    {
        return properties(properties -> properties.icon(icon));
    }

    /**
     * Sets the item display generator used when building list of item stacks to be displayed on this CreativeModeTab.
     *
     * @param displayItemsGenerator Item display generator to be used.
     * @return This Builder.
     */
    public CreativeModeTabBuilder<O, P> displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator)
    {
        return properties(properties -> properties.displayItems(displayItemsGenerator));
    }

    /**
     * Marks this CreativeModeTab as being aligned to the right.
     *
     * @return This Builder.
     */
    public CreativeModeTabBuilder<O, P> alignedRight()
    {
        return properties(CreativeModeTab.Builder::alignedRight);
    }

    /**
     * Marks this CreativeModeTab as not rendering the title element.
     *
     * @return This Builder.
     */
    public CreativeModeTabBuilder<O, P> hideTitle()
    {
        return properties(CreativeModeTab.Builder::hideTitle);
    }

    /**
     * Marks this CreativeModeTab as having no scroll bar.
     *
     * @return This Builder.
     */
    public CreativeModeTabBuilder<O, P> noScrollBar()
    {
        return properties(CreativeModeTab.Builder::noScrollBar);
    }

    /**
     * Sets the background texture suffix for this CreativeModeTab.
     *
     * @param backgroundSuffix Background texture suffix.
     * @return This Builder.
     */
    public CreativeModeTabBuilder<O, P> backgroundSuffix(String backgroundSuffix)
    {
        return properties(properties -> properties.backgroundSuffix(backgroundSuffix));
    }

    @Override
    protected RegistryEntry<CreativeModeTab> createRegistryEntry()
    {
        return new BaseRegistryEntry<>(registrar, registryKey);
    }

    @Override
    protected CreativeModeTab createEntry()
    {
        return propertiesModifier.apply(CreativeModeTabHooks
                .get()
                .createNewBuilder()
                .title(Component.translatable(registryName().toLanguageKey("itemGroup")))
        ).build();
    }

    @Override
    protected String getDescriptionId(RegistryEntry<CreativeModeTab> entry)
    {
        return entry.value().getDisplayName().getContents() instanceof TranslatableContents translatable ? translatable.getKey() : registryName().toLanguageKey("itemGroup");
    }
}
