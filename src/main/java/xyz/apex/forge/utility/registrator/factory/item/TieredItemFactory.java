package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

@FunctionalInterface
public interface TieredItemFactory<ITEM extends TieredItem>
{
	TieredItemFactory<TieredItem> DEFAULT = TieredItem::new;

	ITEM create(Tier itemTier, Item.Properties properties);
}
