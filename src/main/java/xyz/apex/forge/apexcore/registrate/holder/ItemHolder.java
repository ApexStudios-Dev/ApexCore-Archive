package xyz.apex.forge.apexcore.registrate.holder;

import net.minecraft.world.item.Item;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.ItemBuilder;
import xyz.apex.forge.apexcore.registrate.builder.factory.ItemFactory;

public interface ItemHolder<OWNER extends CoreRegistrate<OWNER> & ItemHolder<OWNER>>
{
	@SuppressWarnings("unchecked")
	private OWNER self()
	{
		return (OWNER) this;
	}

	default ItemBuilder<OWNER, Item, OWNER> item()
	{
		return item(self(), self().currentName(), ItemFactory.DEFAULT);
	}

	default ItemBuilder<OWNER, Item, OWNER> item(String name)
	{
		return item(self(), name, ItemFactory.DEFAULT);
	}

	default <PARENT> ItemBuilder<OWNER, Item, PARENT> item(PARENT parent)
	{
		return item(parent, self().currentName(), ItemFactory.DEFAULT);
	}

	default <PARENT> ItemBuilder<OWNER, Item, PARENT> item(PARENT parent, String name)
	{
		return item(parent, name, ItemFactory.DEFAULT);
	}

	default <ITEM extends Item> ItemBuilder<OWNER, ITEM, OWNER> item(ItemFactory<ITEM> itemFactory)
	{
		return item(self(), self().currentName(), itemFactory);
	}

	default <ITEM extends Item> ItemBuilder<OWNER, ITEM, OWNER> item(String name, ItemFactory<ITEM> itemFactory)
	{
		return item(self(), name, itemFactory);
	}

	default <ITEM extends Item, PARENT> ItemBuilder<OWNER, ITEM, PARENT> item(PARENT parent, ItemFactory<ITEM> itemFactory)
	{
		return item(parent, self().currentName(), itemFactory);
	}

	default <ITEM extends Item, PARENT> ItemBuilder<OWNER, ITEM, PARENT> item(PARENT parent, String name, ItemFactory<ITEM> itemFactory)
	{
		// obtain instance so that future calls to creativeModeTab won't affect this builder
		var creativeModeTab = self().currentCreativeModeTab();

		return self().entry(name, callback -> new ItemBuilder<>(self(), parent, name, callback, itemFactory)
				.transform(ItemBuilder::applyDefaults)
				.transform(builder -> creativeModeTab == null ? builder : builder.tab(creativeModeTab::get))
		);
	}
}
