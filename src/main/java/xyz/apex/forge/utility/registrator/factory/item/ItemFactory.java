package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemFactory<ITEM extends Item>
{
	ItemFactory<Item> DEFAULT = Item::new;

	ITEM create(Item.Properties properties);
}
