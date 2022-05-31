package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

@FunctionalInterface
public interface SwordItemFactory<ITEM extends SwordItem>
{
	SwordItemFactory<SwordItem> DEFAULT = SwordItem::new;

	ITEM create(Tier itemTier, int attackDamage, float attackSpeed, Item.Properties properties);
}
