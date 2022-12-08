// TODO: Revamp for new creative mode tab system
/*
package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class ItemGroupCategoryManager
{
	private static final Map<CreativeModeTab, ItemGroupCategoryManager> categoryManagers = Maps.newHashMap();

	private final Set<ItemGroupCategory> categories = Sets.newLinkedHashSet();
	private final Set<ItemGroupCategory> enabledCategories = Sets.newHashSet();
	private final CreativeModeTab itemGroup;

	private ItemGroupCategoryManager(CreativeModeTab itemGroup)
	{
		this.itemGroup = itemGroup;
	}

	public void addCategory(ItemGroupCategory category)
	{
		categories.add(category);
	}

	public void addCategories(ItemGroupCategory... categories)
	{
		Collections.addAll(this.categories, categories);
	}

	public void addCategories(Collection<ItemGroupCategory> categories)
	{
		this.categories.addAll(categories);
	}

	public Set<ItemGroupCategory> getCategories()
	{
		return categories;
	}

	public CreativeModeTab getItemGroup()
	{
		return itemGroup;
	}

	public Set<ItemGroupCategory> getEnabledCategories()
	{
		return enabledCategories;
	}

	public boolean isCategoryAllowed(ItemGroupCategory category)
	{
		return categories.contains(category);
	}

	public boolean isCategoryEnabled(ItemGroupCategory category)
	{
		return isCategoryAllowed(category) && enabledCategories.contains(category);
	}

	public void enableCategory(ItemGroupCategory category)
	{
		if(!isCategoryAllowed(category))
			return;
		if(isCategoryEnabled(category))
			return;

		enabledCategories.add(category);
	}

	public void disableCategory(ItemGroupCategory category)
	{
		if(!isCategoryAllowed(category))
			return;
		if(!isCategoryEnabled(category))
			return;

		enabledCategories.remove(category);
	}

	public void toggleCategory(ItemGroupCategory category)
	{
		if(!isCategoryAllowed(category))
			return;

		if(isCategoryEnabled(category))
			disableCategory(category);
		else
			enableCategory(category);
	}

	public void disableCategories()
	{
		enabledCategories.clear();
	}

	public void applyFilter(NonNullList<ItemStack> items)
	{
		items.clear();

		if(enabledCategories.isEmpty())
		{
			itemGroup.fillItemList(items);
			return;
		}

		var itemGroupItems = NonNullList.<ItemStack>create();
		itemGroup.fillItemList(itemGroupItems);

		for(var stack : itemGroupItems)
		{
			for(var category : enabledCategories)
			{
				if(category.test(stack))
				{
					items.add(stack);
					break;
				}
			}
		}
	}

	public static ItemGroupCategoryManager getInstance(CreativeModeTab itemGroup)
	{
		return categoryManagers.computeIfAbsent(itemGroup, ItemGroupCategoryManager::new);
	}
}
*/
