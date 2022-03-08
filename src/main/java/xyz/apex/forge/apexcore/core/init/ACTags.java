package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public final class ACTags
{
	private static final ACRegistry REGISTRY = ACRegistry.getRegistry();

	static void bootstrap()
	{
		Items.bootstrap();
		Blocks.bootstrap();
		Fluids.bootstrap();
		EntityTypes.bootstrap();
	}

	public static final class Items
	{
		// region: Wools
		public static final ITag.INamedTag<Item> WOOLS_WHITE = REGISTRY.forgeItemTag("wools/white");
		public static final ITag.INamedTag<Item> WOOLS_ORANGE = REGISTRY.forgeItemTag("wools/orange");
		public static final ITag.INamedTag<Item> WOOLS_MAGENTA = REGISTRY.forgeItemTag("wools/magenta");
		public static final ITag.INamedTag<Item> WOOLS_LIGHT_BLUE = REGISTRY.forgeItemTag("wools/light_blue");
		public static final ITag.INamedTag<Item> WOOLS_YELLOW = REGISTRY.forgeItemTag("wools/yellow");
		public static final ITag.INamedTag<Item> WOOLS_LIME = REGISTRY.forgeItemTag("wools/lime");
		public static final ITag.INamedTag<Item> WOOLS_PINK = REGISTRY.forgeItemTag("wools/pink");
		public static final ITag.INamedTag<Item> WOOLS_GRAY = REGISTRY.forgeItemTag("wools/gray");
		public static final ITag.INamedTag<Item> WOOLS_LIGHT_GRAY = REGISTRY.forgeItemTag("wools/light_gray");
		public static final ITag.INamedTag<Item> WOOLS_CYAN = REGISTRY.forgeItemTag("wools/cyan");
		public static final ITag.INamedTag<Item> WOOLS_PURPLE = REGISTRY.forgeItemTag("wools/purple");
		public static final ITag.INamedTag<Item> WOOLS_BLUE = REGISTRY.forgeItemTag("wools/blue");
		public static final ITag.INamedTag<Item> WOOLS_BROWN = REGISTRY.forgeItemTag("wools/brown");
		public static final ITag.INamedTag<Item> WOOLS_GREEN = REGISTRY.forgeItemTag("wools/green");
		public static final ITag.INamedTag<Item> WOOLS_RED = REGISTRY.forgeItemTag("wools/red");
		public static final ITag.INamedTag<Item> WOOLS_BLACK = REGISTRY.forgeItemTag("wools/black");
		public static final ITag.INamedTag<Item> WOOLS = REGISTRY.forgeItemTag("wools");
		// endregion

		public static final ITag.INamedTag<Item> HATS = REGISTRY.moddedItemTag("hats");

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.ITEM_TAGS, provider -> {
				// region: Wools
				provider.tag(WOOLS_WHITE).add(net.minecraft.item.Items.WHITE_WOOL);
				provider.tag(WOOLS_ORANGE).add(net.minecraft.item.Items.ORANGE_WOOL);
				provider.tag(WOOLS_MAGENTA).add(net.minecraft.item.Items.MAGENTA_WOOL);
				provider.tag(WOOLS_LIGHT_BLUE).add(net.minecraft.item.Items.LIGHT_BLUE_WOOL);
				provider.tag(WOOLS_YELLOW).add(net.minecraft.item.Items.YELLOW_WOOL);
				provider.tag(WOOLS_LIME).add(net.minecraft.item.Items.LIME_WOOL);
				provider.tag(WOOLS_PINK).add(net.minecraft.item.Items.PINK_WOOL);
				provider.tag(WOOLS_GRAY).add(net.minecraft.item.Items.GRAY_WOOL);
				provider.tag(WOOLS_LIGHT_GRAY).add(net.minecraft.item.Items.LIGHT_GRAY_WOOL);
				provider.tag(WOOLS_CYAN).add(net.minecraft.item.Items.CYAN_WOOL);
				provider.tag(WOOLS_PURPLE).add(net.minecraft.item.Items.PURPLE_WOOL);
				provider.tag(WOOLS_BLUE).add(net.minecraft.item.Items.BLUE_WOOL);
				provider.tag(WOOLS_BROWN).add(net.minecraft.item.Items.BROWN_WOOL);
				provider.tag(WOOLS_GREEN).add(net.minecraft.item.Items.GREEN_WOOL);
				provider.tag(WOOLS_RED).add(net.minecraft.item.Items.RED_WOOL);
				provider.tag(WOOLS_BLACK).add(net.minecraft.item.Items.BLACK_WOOL);

				provider.tag(WOOLS).addTags(WOOLS_WHITE, WOOLS_ORANGE, WOOLS_MAGENTA, WOOLS_LIGHT_BLUE, WOOLS_YELLOW, WOOLS_LIME, WOOLS_PINK, WOOLS_GRAY, WOOLS_LIGHT_GRAY, WOOLS_CYAN, WOOLS_PURPLE, WOOLS_BLUE, WOOLS_BROWN, WOOLS_GREEN, WOOLS_RED, WOOLS_BLACK);
				provider.tag(ItemTags.WOOL).addTags(WOOLS, WOOLS_WHITE, WOOLS_ORANGE, WOOLS_MAGENTA, WOOLS_LIGHT_BLUE, WOOLS_YELLOW, WOOLS_LIME, WOOLS_PINK, WOOLS_GRAY, WOOLS_LIGHT_GRAY, WOOLS_CYAN, WOOLS_PURPLE, WOOLS_BLUE, WOOLS_BROWN, WOOLS_GREEN, WOOLS_RED, WOOLS_BLACK);
				// endregion

				provider.tag(HATS);
			});
		}
	}

	public static final class Blocks
	{
		// region: Wools
		public static final ITag.INamedTag<Block> WOOLS_WHITE = REGISTRY.forgeBlockTag("wools/white");
		public static final ITag.INamedTag<Block> WOOLS_ORANGE = REGISTRY.forgeBlockTag("wools/orange");
		public static final ITag.INamedTag<Block> WOOLS_MAGENTA = REGISTRY.forgeBlockTag("wools/magenta");
		public static final ITag.INamedTag<Block> WOOLS_LIGHT_BLUE = REGISTRY.forgeBlockTag("wools/light_blue");
		public static final ITag.INamedTag<Block> WOOLS_YELLOW = REGISTRY.forgeBlockTag("wools/yellow");
		public static final ITag.INamedTag<Block> WOOLS_LIME = REGISTRY.forgeBlockTag("wools/lime");
		public static final ITag.INamedTag<Block> WOOLS_PINK = REGISTRY.forgeBlockTag("wools/pink");
		public static final ITag.INamedTag<Block> WOOLS_GRAY = REGISTRY.forgeBlockTag("wools/gray");
		public static final ITag.INamedTag<Block> WOOLS_LIGHT_GRAY = REGISTRY.forgeBlockTag("wools/light_gray");
		public static final ITag.INamedTag<Block> WOOLS_CYAN = REGISTRY.forgeBlockTag("wools/cyan");
		public static final ITag.INamedTag<Block> WOOLS_PURPLE = REGISTRY.forgeBlockTag("wools/purple");
		public static final ITag.INamedTag<Block> WOOLS_BLUE = REGISTRY.forgeBlockTag("wools/blue");
		public static final ITag.INamedTag<Block> WOOLS_BROWN = REGISTRY.forgeBlockTag("wools/brown");
		public static final ITag.INamedTag<Block> WOOLS_GREEN = REGISTRY.forgeBlockTag("wools/green");
		public static final ITag.INamedTag<Block> WOOLS_RED = REGISTRY.forgeBlockTag("wools/red");
		public static final ITag.INamedTag<Block> WOOLS_BLACK = REGISTRY.forgeBlockTag("wools/black");
		public static final ITag.INamedTag<Block> WOOLS = REGISTRY.forgeBlockTag("wools");
		// endregion

		public static final ITag.INamedTag<Block> CROPS = REGISTRY.forgeBlockTag("crops");
		public static final ITag.INamedTag<Block> MUSHROOMS = REGISTRY.forgeBlockTag("mushrooms");

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.BLOCK_TAGS, provider -> {
				// region: Crops
				provider.tag(CROPS);
				provider.tag(BlockTags.CROPS).addTags(CROPS);
				// endregion

				// region: Mushrooms
				provider.tag(MUSHROOMS).add(net.minecraft.block.Blocks.BROWN_MUSHROOM, net.minecraft.block.Blocks.RED_MUSHROOM);
				// endregion

				// region: Wools
				provider.tag(WOOLS_WHITE).add(net.minecraft.block.Blocks.WHITE_WOOL);
				provider.tag(WOOLS_ORANGE).add(net.minecraft.block.Blocks.ORANGE_WOOL);
				provider.tag(WOOLS_MAGENTA).add(net.minecraft.block.Blocks.MAGENTA_WOOL);
				provider.tag(WOOLS_LIGHT_BLUE).add(net.minecraft.block.Blocks.LIGHT_BLUE_WOOL);
				provider.tag(WOOLS_YELLOW).add(net.minecraft.block.Blocks.YELLOW_WOOL);
				provider.tag(WOOLS_LIME).add(net.minecraft.block.Blocks.LIME_WOOL);
				provider.tag(WOOLS_PINK).add(net.minecraft.block.Blocks.PINK_WOOL);
				provider.tag(WOOLS_GRAY).add(net.minecraft.block.Blocks.GRAY_WOOL);
				provider.tag(WOOLS_LIGHT_GRAY).add(net.minecraft.block.Blocks.LIGHT_GRAY_WOOL);
				provider.tag(WOOLS_CYAN).add(net.minecraft.block.Blocks.CYAN_WOOL);
				provider.tag(WOOLS_PURPLE).add(net.minecraft.block.Blocks.PURPLE_WOOL);
				provider.tag(WOOLS_BLUE).add(net.minecraft.block.Blocks.BLUE_WOOL);
				provider.tag(WOOLS_BROWN).add(net.minecraft.block.Blocks.BROWN_WOOL);
				provider.tag(WOOLS_GREEN).add(net.minecraft.block.Blocks.GREEN_WOOL);
				provider.tag(WOOLS_RED).add(net.minecraft.block.Blocks.RED_WOOL);
				provider.tag(WOOLS_BLACK).add(net.minecraft.block.Blocks.BLACK_WOOL);

				provider.tag(WOOLS).addTags(WOOLS_WHITE, WOOLS_ORANGE, WOOLS_MAGENTA, WOOLS_LIGHT_BLUE, WOOLS_YELLOW, WOOLS_LIME, WOOLS_PINK, WOOLS_GRAY, WOOLS_LIGHT_GRAY, WOOLS_CYAN, WOOLS_PURPLE, WOOLS_BLUE, WOOLS_BROWN, WOOLS_GREEN, WOOLS_RED, WOOLS_BLACK);
				provider.tag(BlockTags.WOOL).addTags(WOOLS, WOOLS_WHITE, WOOLS_ORANGE, WOOLS_MAGENTA, WOOLS_LIGHT_BLUE, WOOLS_YELLOW, WOOLS_LIME, WOOLS_PINK, WOOLS_GRAY, WOOLS_LIGHT_GRAY, WOOLS_CYAN, WOOLS_PURPLE, WOOLS_BLUE, WOOLS_BROWN, WOOLS_GREEN, WOOLS_RED, WOOLS_BLACK);
				// endregion
			});
		}
	}

	public static final class Fluids
	{
		private static void bootstrap() { }
	}

	public static final class EntityTypes
	{
		// region: Forge / Vanilla Missing
		public static final ITag.INamedTag<EntityType<?>> COWS = REGISTRY.forgeEntityTypeTag("cows");
		public static final ITag.INamedTag<EntityType<?>> CHICKENS = REGISTRY.forgeEntityTypeTag("chickens");
		// endregion

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.ENTITY_TAGS, provider -> {
				// region: Forge / Vanilla Missing
				provider.tag(COWS).add(EntityType.COW);
				provider.tag(CHICKENS).add(EntityType.CHICKEN);
				// endregion
			});
		}
	}
}
