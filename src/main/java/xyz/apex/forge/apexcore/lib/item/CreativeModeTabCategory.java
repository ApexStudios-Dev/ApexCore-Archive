package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.Maps;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import xyz.apex.java.utility.nullness.NonnullPredicate;

import java.util.Map;

public final class CreativeModeTabCategory implements NonnullPredicate<ItemStack>
{
	private static final int cycleTime = 1000;

	private final Builder builder;
	private final Map<CreativeModeTab, NonNullList<ItemStack>> categoryIcons = Maps.newHashMap();
	private long cycleStartTime;
	private long cycleTickTime;
	private long cyclePausedDuration = 0L;

	private CreativeModeTabCategory(Builder builder)
	{
		this.builder = builder;

		cycleTickTime = System.currentTimeMillis();
		cycleStartTime = cycleTickTime - ((long) builder.cycleOffset * cycleTime);
	}

	private NonNullList<ItemStack> getCategoryIcons(CreativeModeTab tab)
	{
		var itemGroupItems = NonNullList.<ItemStack>create();
		var icons = NonNullList.<ItemStack>create();
		tab.fillItemList(itemGroupItems);

		for(ItemStack stack : itemGroupItems)
		{
			if(test(stack))
				icons.add(stack);
		}

		return icons;
	}

	@Override
	public boolean test(ItemStack stack)
	{
		return builder.categoryPredicate.test(stack);
	}

	public void tick(boolean cycleIcons)
	{
		if(cycleIcons)
		{
			if(cyclePausedDuration > 0L)
			{
				cycleStartTime += cyclePausedDuration;
				cyclePausedDuration = 0L;
			}

			cycleTickTime = System.currentTimeMillis();
		}
		else
			cyclePausedDuration = System.currentTimeMillis() - cycleTickTime;
	}

	public ItemStack getCategoryIcon(CreativeModeTab itemGroup)
	{
		var icons = categoryIcons.computeIfAbsent(itemGroup, this::getCategoryIcons);

		if(icons.isEmpty())
			return Items.BARRIER.getDefaultInstance();

		var index = ((cycleTickTime - cycleStartTime) / cycleTime) % icons.size();
		return icons.get(Math.toIntExact(index));
	}

	public String getCategoryNameKey()
	{
		return "itemGroup.category.%s".formatted(builder.categoryName);
	}

	public Component getCategoryName()
	{
		var categoryNameKey = getCategoryNameKey();
		return new TranslatableComponent(categoryNameKey);
	}

	public static Builder builder(String categoryName)
	{
		return new Builder(categoryName);
	}

	public static final class Builder
	{
		private final String categoryName;
		private int cycleOffset = (int) ((Math.random() * 10000) % Integer.MAX_VALUE);
		private NonnullPredicate<ItemStack> categoryPredicate = stack -> true;

		private Builder(String categoryName)
		{
			this.categoryName = categoryName;
		}

		public Builder predicate(NonnullPredicate<ItemStack> categoryPredicate)
		{
			this.categoryPredicate = categoryPredicate;
			return this;
		}

		public Builder tagged(Tag<Item> itemTag)
		{
			return predicate(s -> s.is(itemTag));
		}

		public Builder cycleOffset(int cycleOffset)
		{
			this.cycleOffset = cycleOffset;
			return this;
		}

		public CreativeModeTabCategory build()
		{
			return new CreativeModeTabCategory(this);
		}
	}
}
