package xyz.apex.minecraft.apexcore.common.registry.builder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import xyz.apex.minecraft.apexcore.common.registry.entry.EnchantmentEntry;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class EnchantmentBuilder<T extends Enchantment> extends Builder<Enchantment, T, EnchantmentEntry<T>, EnchantmentBuilder<T>>
{
    private final EnchantmentFactory<T> enchantmentFactory;
    private final EnchantmentCategory enchantmentCategory;
    private Enchantment.Rarity rarity = Enchantment.Rarity.COMMON;
    private final Set<EquipmentSlot> equipmentSlots = EnumSet.noneOf(EquipmentSlot.class);

    private EnchantmentBuilder(String ownerId, String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory)
    {
        super(Registries.ENCHANTMENT, ownerId, registrationName);

        this.enchantmentFactory = enchantmentFactory;
        this.enchantmentCategory = enchantmentCategory;
    }

    public EnchantmentBuilder<T> rarity(Enchantment.Rarity rarity)
    {
        this.rarity = rarity;
        return this;
    }

    public EnchantmentBuilder<T> withEquipmentSlot(EquipmentSlot equipmentSlot)
    {
        equipmentSlots.add(equipmentSlot);
        return this;
    }

    public EnchantmentBuilder<T> withEquipmentSlots(EquipmentSlot... equipmentSlots)
    {
        Collections.addAll(this.equipmentSlots, equipmentSlots);
        return this;
    }

    public EnchantmentBuilder<T> withArmorSlots()
    {
        return withEquipmentSlots(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
    }

    @Override
    protected EnchantmentEntry<T> createRegistryEntry(ResourceLocation registryName)
    {
        return new EnchantmentEntry<>(registryName);
    }

    @Override
    protected T create()
    {
        return enchantmentFactory.create(rarity, enchantmentCategory, equipmentSlots.toArray(EquipmentSlot[]::new));
    }

    public static <T extends Enchantment> EnchantmentBuilder<T> builder(String ownerId, String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory)
    {
        return new EnchantmentBuilder<>(ownerId, registrationName, enchantmentCategory, enchantmentFactory);
    }

    public static EnchantmentBuilder<Enchantment> builder(String ownerId, String registrationName, EnchantmentCategory enchantmentCategory)
    {
        return builder(ownerId, registrationName, enchantmentCategory, EnchantmentBuilder::newSimpleEnchantment);
    }

    // cause class is stupidly marked abstract with no actual abstract methods
    public static Enchantment newSimpleEnchantment(Enchantment.Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot... equipmentSlots)
    {
        return new Enchantment(rarity, enchantmentCategory, equipmentSlots) {};
    }

    @FunctionalInterface
    public interface EnchantmentFactory<T extends Enchantment>
    {
        T create(Enchantment.Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot... equipmentSlots);
    }
}
