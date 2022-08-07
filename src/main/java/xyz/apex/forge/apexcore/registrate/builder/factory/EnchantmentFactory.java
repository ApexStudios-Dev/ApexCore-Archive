package xyz.apex.forge.apexcore.registrate.builder.factory;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@FunctionalInterface
public interface EnchantmentFactory<ENCHANTMENT extends Enchantment>
{
	ENCHANTMENT create(Enchantment.Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots);
}