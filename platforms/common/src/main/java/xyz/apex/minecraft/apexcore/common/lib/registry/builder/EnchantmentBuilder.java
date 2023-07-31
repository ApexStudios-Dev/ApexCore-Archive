package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryProviderListener;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.EnchantmentEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.EnchantmentFactory;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.tag.TagsProvider;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Enchantment Builder implementation.
 * <p>
 * Used to build and register Enchantment entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of Enchantment [Entry].
 * @param <P> Type of Parent.
 */
public final class EnchantmentBuilder<O extends AbstractRegistrar<O>, T extends Enchantment, P> extends AbstractBuilder<O, P, Enchantment, T, EnchantmentBuilder<O, T, P>, EnchantmentEntry<T>>
{
    private final EnchantmentFactory<T> enchantmentFactory;
    private final EnchantmentCategory enchantmentCategory;
    private final Set<EquipmentSlot> equipmentSlots = EnumSet.noneOf(EquipmentSlot.class);
    private Enchantment.Rarity rarity = Enchantment.Rarity.COMMON;

    @ApiStatus.Internal
    public EnchantmentBuilder(O registrar, P parent, String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory)
    {
        super(registrar, parent, Registries.ENCHANTMENT, registrationName);

        this.enchantmentCategory = enchantmentCategory;
        this.enchantmentFactory = enchantmentFactory;
    }

    /**
     * Sets the rarity for this enchantment.
     *
     * @param rarity Rarity for this enchantment.
     * @return This Builder.
     */
    public EnchantmentBuilder<O, T, P> rarity(Enchantment.Rarity rarity)
    {
        this.rarity = rarity;
        return this;
    }

    /**
     * Marks equipment slot as valid slot for this enchantment.
     *
     * @param equipmentSlot Valid equipment slot.
     * @return This Builder.
     */
    public EnchantmentBuilder<O, T, P> equipmentSlot(EquipmentSlot equipmentSlot)
    {
        this.equipmentSlots.add(equipmentSlot);
        return this;
    }

    /**
     * Marks given equipment slots as valid for this enchantment.
     *
     * @param equipmentSlots Valid equipment slots.
     * @return This Builder.
     */
    public EnchantmentBuilder<O, T, P> equipmentSlots(EquipmentSlot... equipmentSlots)
    {
        Collections.addAll(this.equipmentSlots, equipmentSlots);
        return this;
    }

    /**
     * Marks all armor equipment slots as valid for this enchantment.
     *
     * @return This Builder.
     */
    public EnchantmentBuilder<O, T, P> armorSlots()
    {
        return equipmentSlots(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
    }

    /**
     * Resister a set of Tags for this Enchantment.
     *
     * @param tags Tags to be set.
     * @return This Builder.
     */
    public EnchantmentBuilder<O, T, P> tag(TagKey<Enchantment>... tags)
    {
        return tag((provider, lookup, entry) -> Stream.of(tags).map(provider::tag).forEach(tag -> tag.addElement(entry.value())));
    }

    /**
     * Set the Tag generator for this Enchantment.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public EnchantmentBuilder<O, T, P> tag(RegistryProviderListener<TagsProvider<Enchantment>, T, EnchantmentEntry<T>> listener)
    {
        return setProvider(ProviderTypes.ENCHANTMENT_TAGS, listener);
    }

    /**
     * Clears the currently registered Tag generator.
     *
     * @return This Builder.
     */
    public EnchantmentBuilder<O, T, P> noTags()
    {
        return clearProvider(ProviderTypes.ENCHANTMENT_TAGS);
    }

    @Override
    protected EnchantmentEntry<T> createRegistryEntry()
    {
        return new EnchantmentEntry<>(registrar, registryKey);
    }

    @Override
    protected T createEntry()
    {
        return enchantmentFactory.create(rarity, enchantmentCategory, equipmentSlots.toArray(EquipmentSlot[]::new));
    }

    @Override
    protected String getDescriptionId(EnchantmentEntry<T> entry)
    {
        return entry.value().getDescriptionId();
    }
}
