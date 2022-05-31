package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

@FunctionalInterface
public interface HoeItemFactory<ITEM extends HoeItem>
{
	HoeItemFactory<HoeItem> DEFAULT = HoeItem::new;

	ITEM create(Tier itemTier, int attackDamage, float attackSpeed, Item.Properties properties);
}
