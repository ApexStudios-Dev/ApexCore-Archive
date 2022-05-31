package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

@FunctionalInterface
public interface AxeItemFactory<ITEM extends AxeItem>
{
	AxeItemFactory<AxeItem> DEFAULT = AxeItem::new;

	ITEM create(Tier itemTier, float attackDamage, float attackSpeed, Item.Properties properties);
}
