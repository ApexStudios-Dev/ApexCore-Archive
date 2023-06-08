package xyz.apex.minecraft.apexcore.common.lib.registry.factories;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Main factory used to construct new Enchantment instances during registrations.
 *
 * @param <T> Type of enchantment to be constructed.
 */
@FunctionalInterface
public interface EnchantmentFactory<T extends Enchantment>
{
    /**
     * Returns new enchantment instance for given properties.
     *
     * @param rarity              Rarity for this enchantment.
     * @param enchantmentCategory Category for this enchantment.
     * @param equipmentSlots      Equipment slots for this enchantment.
     * @return New enchantment instance for given properties.
     */
    T create(Enchantment.Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot... equipmentSlots);
}
