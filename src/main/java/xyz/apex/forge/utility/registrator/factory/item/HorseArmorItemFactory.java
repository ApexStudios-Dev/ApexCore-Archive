package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface HorseArmorItemFactory<ITEM extends HorseArmorItem>
{
	HorseArmorItemFactory<HorseArmorItem> DEFAULT = HorseArmorItem::new;
	DyeableFactory<DyeableHorseArmorItem> DYEABLE_DEFAULT = DyeableHorseArmorItem::new;

	ITEM create(int protection, ResourceLocation texture, Item.Properties properties);

	interface DyeableFactory<ITEM extends HorseArmorItem & DyeableLeatherItem> extends HorseArmorItemFactory<ITEM>
	{
	}
}
