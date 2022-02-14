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

public final class CreativeModeTabCategoryManager
{
	private static final Map<CreativeModeTab, CreativeModeTabCategoryManager> categoryManagers = Maps.newHashMap();

	private final Set<CreativeModeTabCategory> categories = Sets.newLinkedHashSet();
	private final Set<CreativeModeTabCategory> enabledCategories = Sets.newHashSet();
	private final CreativeModeTab tab;

	private CreativeModeTabCategoryManager(CreativeModeTab tab)
	{
		this.tab = tab;
	}

	public void addCategory(CreativeModeTabCategory category)
	{
		categories.add(category);
	}

	public void addCategories(CreativeModeTabCategory... categories)
	{
		Collections.addAll(this.categories, categories);
	}

	public void addCategories(Collection<CreativeModeTabCategory> categories)
	{
		this.categories.addAll(categories);
	}

	public Set<CreativeModeTabCategory> getCategories()
	{
		return categories;
	}

	public CreativeModeTab getTab()
	{
		return tab;
	}

	public Set<CreativeModeTabCategory> getEnabledCategories()
	{
		return enabledCategories;
	}

	public boolean isCategoryAllowed(CreativeModeTabCategory category)
	{
		return categories.contains(category);
	}

	public boolean isCategoryEnabled(CreativeModeTabCategory category)
	{
		return isCategoryAllowed(category) && enabledCategories.contains(category);
	}

	public void enableCategory(CreativeModeTabCategory category)
	{
		if(!isCategoryAllowed(category))
			return;
		if(isCategoryEnabled(category))
			return;

		enabledCategories.add(category);
	}

	public void disableCategory(CreativeModeTabCategory category)
	{
		if(!isCategoryAllowed(category))
			return;
		if(!isCategoryEnabled(category))
			return;

		enabledCategories.remove(category);
	}

	public void toggleCategory(CreativeModeTabCategory category)
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
			tab.fillItemList(items);
			return;
		}

		var tabItems = NonNullList.<ItemStack>create();
		tab.fillItemList(tabItems);

		for(var stack : tabItems)
		{
			for(CreativeModeTabCategory category : enabledCategories)
			{
				if(category.test(stack))
				{
					items.add(stack);
					break;
				}
			}
		}
	}

	public static CreativeModeTabCategoryManager getInstance(CreativeModeTab itemGroup)
	{
		return categoryManagers.computeIfAbsent(itemGroup, CreativeModeTabCategoryManager::new);
	}
}
