package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.Maps;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import xyz.apex.java.utility.nullness.NonnullPredicate;

import java.util.List;
import java.util.Map;

public final class ItemGroupCategory implements NonnullPredicate<ItemStack>
{
	private static final int cycleTime = 1000;

	private final Builder builder;
	private final Map<ItemGroup, NonNullList<ItemStack>> categoryIcons = Maps.newHashMap();
	private long cycleStartTime;
	private long cycleTickTime;
	private long cyclePausedDuration = 0L;

	private ItemGroupCategory(Builder builder)
	{
		this.builder = builder;

		cycleTickTime = System.currentTimeMillis();
		cycleStartTime = cycleTickTime - ((long) builder.cycleOffset * cycleTime);
	}

	private NonNullList<ItemStack> getCategoryIcons(ItemGroup itemGroup)
	{
		NonNullList<ItemStack> itemGroupItems = NonNullList.create();
		NonNullList<ItemStack> icons = NonNullList.create();
		itemGroup.fillItemList(itemGroupItems);

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

	public ItemStack getCategoryIcon(ItemGroup itemGroup)
	{
		List<ItemStack> icons = categoryIcons.computeIfAbsent(itemGroup, this::getCategoryIcons);

		if(icons.isEmpty())
			return Items.BARRIER.getDefaultInstance();

		long index = ((cycleTickTime - cycleStartTime) / cycleTime) % icons.size();
		return icons.get(Math.toIntExact(index));
	}

	public String getCategoryNameKey()
	{
		return "itemGroup.category." + builder.categoryName;
	}

	public ITextComponent getCategoryName()
	{
		String categoryNameKey = getCategoryNameKey();
		return new TranslationTextComponent(categoryNameKey);
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

		public Builder tagged(ITag<Item> itemTag)
		{
			return predicate(s -> s.getItem().is(itemTag));
		}

		public Builder cycleOffset(int cycleOffset)
		{
			this.cycleOffset = cycleOffset;
			return this;
		}

		public ItemGroupCategory build()
		{
			return new ItemGroupCategory(this);
		}
	}
}
