package xyz.apex.forge.apexcore.core.init;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;

import xyz.apex.forge.apexcore.lib.item.CreativeModeTabCategory;
import xyz.apex.forge.apexcore.lib.item.CreativeModeTabCategoryManager;

// Example categories, disabled by default
// Call ACCreativeModeTabCategories#enable() from your mod constructor to enable them
public final class ACCreativeModeTabCategories
{
	public static final CreativeModeTabCategory ENCHANTED_BOOKS = CreativeModeTabCategory
			.builder("enchanted_books")
				.predicate(stack -> stack.getItem() == Items.ENCHANTED_BOOK)
			.build();

	public static final CreativeModeTabCategory TOOLS = CreativeModeTabCategory
			.builder("tools")
				.predicate(stack -> stack.getItem() instanceof DiggerItem)
			.build();

	public static final CreativeModeTabCategory WEAPONS = CreativeModeTabCategory
			.builder("weapons")
				.predicate(stack -> {
					var item = stack.getItem();

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

	public static final CreativeModeTabCategory ARMOR = CreativeModeTabCategory
			.builder("armor")
				.predicate(stack -> stack.getItem() instanceof ArmorItem)
			.build();

	public static final CreativeModeTabCategory STAIRS = CreativeModeTabCategory
			.builder("stairs")
				.tagged(ItemTags.STAIRS)
			.build();

	public static final CreativeModeTabCategory SLABS = CreativeModeTabCategory
			.builder("slabs")
				.tagged(ItemTags.SLABS)
			.build();

	public static final CreativeModeTabCategory ORES = CreativeModeTabCategory
			.builder("ores")
				.tagged(Tags.Items.ORES)
			.build();

	public static final CreativeModeTabCategory STORAGE_BLOCKS = CreativeModeTabCategory
			.builder("storage_blocks")
				.tagged(Tags.Items.STORAGE_BLOCKS)
			.build();

	public static final CreativeModeTabCategory WOOLS = CreativeModeTabCategory
			.builder("wool")
				.tagged(ACTags.Items.WOOLS)
			.build();

	public static final CreativeModeTabCategory LOGS = CreativeModeTabCategory
			.builder("logs")
				.tagged(ItemTags.LOGS)
			.build();

	private static boolean enabled = false;

	public static void enable()
	{
		if(enabled)
			return;

		var manager = CreativeModeTabCategoryManager.getInstance(CreativeModeTab.TAB_COMBAT);
		manager.addCategories(ENCHANTED_BOOKS, WEAPONS, ARMOR);

		manager = CreativeModeTabCategoryManager.getInstance(CreativeModeTab.TAB_TOOLS);
		manager.addCategories(ENCHANTED_BOOKS, WEAPONS, TOOLS);

		manager = CreativeModeTabCategoryManager.getInstance(CreativeModeTab.TAB_BUILDING_BLOCKS);
		manager.addCategories(STAIRS, SLABS, ORES, STORAGE_BLOCKS, WOOLS, LOGS);

		enabled = true;
	}
}
