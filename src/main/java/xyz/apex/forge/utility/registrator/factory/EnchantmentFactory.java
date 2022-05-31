package xyz.apex.forge.utility.registrator.factory;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface EnchantmentFactory<ENCHANTMENT extends Enchantment>
{
	EnchantmentFactory<Enchantment> DEFAULT = Default::new;

	ENCHANTMENT create(Enchantment.Rarity enchantmentRarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] slotTypes);

	// because constructor is protected,
	// and I see no way for access transformers
	// to make a constructor public
	class Default extends Enchantment
	{
		public Default(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] slotTypes)
		{
			super(rarity, enchantmentCategory, slotTypes);
		}
	}
}
