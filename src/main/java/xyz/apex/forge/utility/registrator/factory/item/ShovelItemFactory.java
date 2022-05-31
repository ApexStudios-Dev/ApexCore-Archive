package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

@FunctionalInterface
public interface ShovelItemFactory<ITEM extends ShovelItem>
{
	ShovelItemFactory<ShovelItem> DEFAULT = ShovelItem::new;

	ITEM create(Tier itemTier, float attackDamage, float attackSpeed, Item.Properties properties);
}
