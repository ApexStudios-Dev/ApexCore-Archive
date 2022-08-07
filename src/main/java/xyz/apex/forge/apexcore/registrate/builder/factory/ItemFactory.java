package xyz.apex.forge.apexcore.registrate.builder.factory;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemFactory<ITEM extends Item>
{
	ItemFactory<Item> DEFAULT = Item::new;

	ITEM create(Item.Properties properties);
}