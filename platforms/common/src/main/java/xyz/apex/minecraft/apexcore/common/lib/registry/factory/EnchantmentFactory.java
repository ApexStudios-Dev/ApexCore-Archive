package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Factory interface for constructing new Enchantment entries.
 *
 * @param <T> Type of Enchantment to be constructed.
 */
@FunctionalInterface
public interface EnchantmentFactory<T extends Enchantment>
{
    /**
     * Returns new Enchantment instance with given properties.
     *
     * @param rarity Rarity of this Enchantment.
     * @param category Category for this Enchantment.
     * @param slots Equipment slots for this Enchantment.
     * @return Newly constructed Enchantment with given properties.
     */
    T create(Enchantment.Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots);
}
