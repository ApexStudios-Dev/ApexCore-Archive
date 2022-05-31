package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

@FunctionalInterface
public interface ArmorItemFactory<ITEM extends ArmorItem>
{
	ArmorItemFactory<ArmorItem> DEFAULT = ArmorItem::new;
	DyeableFactory<DyeableArmorItem> DYEABLE_DEFAULT = DyeableArmorItem::new;

	ITEM create(ArmorMaterial armorMaterial, EquipmentSlot slotType, Item.Properties properties);

	interface DyeableFactory<ITEM extends ArmorItem & DyeableLeatherItem> extends ArmorItemFactory<ITEM>
	{
	}
}
