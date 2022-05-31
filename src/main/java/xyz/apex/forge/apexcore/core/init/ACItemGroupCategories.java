package xyz.apex.forge.apexcore.core.init;

import net.minecraft.item.*;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import xyz.apex.forge.apexcore.lib.item.ItemGroupCategory;
import xyz.apex.forge.apexcore.lib.item.ItemGroupCategoryManager;

// Example categories, disabled by default
// Call ACItemGroupCategories#enable() from your mod constructor to enable them
public final class ACItemGroupCategories
{
	public static final ItemGroupCategory ENCHANTED_BOOKS = ItemGroupCategory
			.builder("enchanted_books")
				.cycleIcons()
				.predicate(stack -> stack.getItem() == Items.ENCHANTED_BOOK)
			.build();

	public static final ItemGroupCategory TOOLS = ItemGroupCategory
			.builder("tools")
				.cycleIcons()
				.predicate(stack -> stack.getItem() instanceof ToolItem)
			.build();

	public static final ItemGroupCategory WEAPONS = ItemGroupCategory
			.builder("weapons")
				.cycleIcons()
				.predicate(stack -> {
					Item item = stack.getItem();

					if(item instanceof SwordItem)
						return true;
					if(item instanceof CrossbowItem)
						return true;
					if(item instanceof BowItem)
						return true;
					if(item instanceof AxeItem)
						return true;
					if(item instanceof FlintAndSteelItem)
						return true;
					if(item instanceof TridentItem)
						return true;

					return false;
                 })
			.build();

	public static final ItemGroupCategory ARMOR = ItemGroupCategory
			.builder("armor")
				.cycleIcons()
				.predicate(stack -> stack.getItem() instanceof ArmorItem)
			.build();

	public static final ItemGroupCategory STAIRS = ItemGroupCategory
			.builder("stairs")
				.cycleIcons()
				.tagged(ItemTags.STAIRS)
			.build();

	public static final ItemGroupCategory SLABS = ItemGroupCategory
			.builder("slabs")
				.cycleIcons()
				.tagged(ItemTags.SLABS)
			.build();

	public static final ItemGroupCategory ORES = ItemGroupCategory
			.builder("ores")
				.cycleIcons()
				.tagged(Tags.Items.ORES)
			.build();

	public static final ItemGroupCategory STORAGE_BLOCKS = ItemGroupCategory
			.builder("storage_blocks")
				.cycleIcons()
				.tagged(Tags.Items.STORAGE_BLOCKS)
			.build();

	public static final ItemGroupCategory WOOLS = ItemGroupCategory
			.builder("wool")
				.cycleIcons()
				.tagged(ACTags.Items.WOOLS)
			.build();

	public static final ItemGroupCategory LOGS = ItemGroupCategory
			.builder("logs")
				.cycleIcons()
				.tagged(ItemTags.LOGS)
			.build();

	private static boolean enabled = false;

	public static void enable()
	{
		if(enabled)
			return;

		ItemGroupCategoryManager manager = ItemGroupCategoryManager.getInstance(ItemGroup.TAB_COMBAT);
		manager.addCategories(ENCHANTED_BOOKS, WEAPONS, ARMOR);

		manager = ItemGroupCategoryManager.getInstance(ItemGroup.TAB_TOOLS);
		manager.addCategories(ENCHANTED_BOOKS, WEAPONS, TOOLS);

		manager = ItemGroupCategoryManager.getInstance(ItemGroup.TAB_BUILDING_BLOCKS);
		manager.addCategories(STAIRS, SLABS, ORES, STORAGE_BLOCKS, WOOLS, LOGS);

		enabled = true;
	}
}
