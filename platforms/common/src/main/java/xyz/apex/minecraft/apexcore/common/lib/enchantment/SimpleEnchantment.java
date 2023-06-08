package xyz.apex.minecraft.apexcore.common.lib.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Simple enchantment implementation.
 * <p>
 * Enchantment is abstract and has protected constructor in vanilla,
 * this exists to "implement" the abstract methods and make constructor public.
 */
public class SimpleEnchantment extends Enchantment
{
    public SimpleEnchantment(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot... equipmentSlots)
    {
        super(rarity, enchantmentCategory, equipmentSlots);
    }
}
