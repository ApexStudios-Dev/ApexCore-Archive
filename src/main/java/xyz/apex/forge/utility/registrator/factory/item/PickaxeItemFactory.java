package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

@FunctionalInterface
public interface PickaxeItemFactory<ITEM extends PickaxeItem>
{
	PickaxeItemFactory<PickaxeItem> DEFAULT = PickaxeItem::new;

	ITEM create(Tier itemTier, int attackDamage, float attackSpeed, Item.Properties properties);
}
