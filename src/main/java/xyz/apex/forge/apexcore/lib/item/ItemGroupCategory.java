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

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.Lazy;
import xyz.apex.java.utility.nullness.NonnullPredicate;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;
import java.util.Map;

import static xyz.apex.forge.utility.registrator.AbstractRegistrator.LANG_EXT_PROVIDER;
import static com.tterrag.registrate.providers.ProviderType.LANG;

public final class ItemGroupCategory implements NonnullPredicate<ItemStack>
{
	private static final int cycleTime = 1000;

	private final Builder builder;
	private final Map<CreativeModeTab, NonNullList<ItemStack>> categoryIcons = Maps.newHashMap();
	private long cycleStartTime;
	private long cycleTickTime;
	private long cyclePausedDuration = 0L;
	private final boolean cycleIcons;
	private final Lazy<ItemStack> defaultIcon;

	private ItemGroupCategory(Builder builder)
	{
		this.builder = builder;

		defaultIcon = Lazy.of(() -> builder.defaultIcon == null ? Items.BARRIER.getDefaultInstance() : builder.defaultIcon.get());
		cycleIcons = builder.cycleIcons;
		cycleTickTime = System.currentTimeMillis();
		cycleStartTime = cycleTickTime - ((long) builder.cycleOffset * cycleTime);
	}

	private NonNullList<ItemStack> getCategoryIcons(CreativeModeTab itemGroup)
	{
		var itemGroupItems = NonNullList.<ItemStack>create();
		var icons = NonNullList.<ItemStack>create();
		itemGroup.fillItemList(itemGroupItems);

		for(var stack : itemGroupItems)
		{
			if(test(stack))
				icons.add(stack);
		}

		var defaultIcon = this.defaultIcon.get();

		if(icons.isEmpty())
			icons.add(defaultIcon);
		else
		{
			for(var icon : icons)
			{
				if(ItemStack.isSame(icon, defaultIcon))
					return icons;
			}

			icons.add(defaultIcon);
		}

		return icons;
	}

	@Override
	public boolean test(ItemStack stack)
	{
		return builder.categoryPredicate.test(stack);
	}

	public boolean isCyclingIcons()
	{
		return cycleIcons;
	}

	public void tick(boolean doCycle)
	{
		if(cycleIcons)
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
	}

	public ItemStack getCategoryIcon(CreativeModeTab itemGroup)
	{
		if(cycleIcons)
		{
			var icons = categoryIcons.computeIfAbsent(itemGroup, this::getCategoryIcons);

			if(icons.isEmpty())
				return defaultIcon.get();

			long index = ((cycleTickTime - cycleStartTime) / cycleTime) % icons.size();
			return icons.get(Math.toIntExact(index));
		}
		else
			return defaultIcon.get();
	}

	public String getCategoryNameKey()
	{
		return "itemGroup.category." + builder.categoryName;
	}

	public Component getCategoryName()
	{
		var categoryNameKey = getCategoryNameKey();
		return new TranslatableComponent(categoryNameKey);
	}

	public <T extends AbstractRegistrator<T>> ItemGroupCategory addTranslationGenerator(T registrator, String englishName)
	{
		registrator.addDataGenerator(LANG, provider -> provider.add(getCategoryNameKey(), englishName));
		return this;
	}

	public <T extends AbstractRegistrator<T>> ItemGroupCategory addTranslationGenerator(T registrator, String languageKey, String translationValue)
	{
		registrator.addDataGenerator(LANG_EXT_PROVIDER, provider -> provider.add(languageKey, getCategoryNameKey(), translationValue));
		return this;
	}

	public static Builder builder(String categoryName)
	{
		return new Builder(categoryName);
	}

	public static final class Builder
	{
		private final String categoryName;
		private int cycleOffset = (int) ((Math.random() * 10000) % Integer.MAX_VALUE);
		private boolean cycleIcons = false;
		private NonnullPredicate<ItemStack> categoryPredicate = stack -> true;
		@Nullable private NonnullSupplier<ItemStack> defaultIcon;

		private Builder(String categoryName)
		{
			this.categoryName = categoryName;
		}

		public Builder predicate(NonnullPredicate<ItemStack> categoryPredicate)
		{
			this.categoryPredicate = categoryPredicate;
			return this;
		}

		public Builder defaultIcon(NonnullSupplier<ItemStack> defaultIcon)
		{
			this.defaultIcon = defaultIcon;
			return this;
		}

		public Builder tagged(Tag<Item> itemTag)
		{
			return predicate(s -> s.is(itemTag));
		}

		public Builder cycleOffset(int cycleOffset)
		{
			cycleIcons = true;
			this.cycleOffset = cycleOffset;
			return this;
		}

		public Builder cycleIcons()
		{
			cycleIcons = true;
			return this;
		}

		public ItemGroupCategory build()
		{
			return new ItemGroupCategory(this);
		}
	}
}
