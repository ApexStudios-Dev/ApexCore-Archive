package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

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
		// TODO: Move to ApexCore or PR into Forge
		public static final ITag.INamedTag<Item> WOOLS_WHITE = REGISTRY.itemTagOptionalForge("wools/white");
		public static final ITag.INamedTag<Item> WOOLS_ORANGE = REGISTRY.itemTagOptionalForge("wools/orange");
		public static final ITag.INamedTag<Item> WOOLS_MAGENTA = REGISTRY.itemTagOptionalForge("wools/magenta");
		public static final ITag.INamedTag<Item> WOOLS_LIGHT_BLUE = REGISTRY.itemTagOptionalForge("wools/light_blue");
		public static final ITag.INamedTag<Item> WOOLS_YELLOW = REGISTRY.itemTagOptionalForge("wools/yellow");
		public static final ITag.INamedTag<Item> WOOLS_LIME = REGISTRY.itemTagOptionalForge("wools/lime");
		public static final ITag.INamedTag<Item> WOOLS_PINK = REGISTRY.itemTagOptionalForge("wools/pink");
		public static final ITag.INamedTag<Item> WOOLS_GRAY = REGISTRY.itemTagOptionalForge("wools/gray");
		public static final ITag.INamedTag<Item> WOOLS_LIGHT_GRAY = REGISTRY.itemTagOptionalForge("wools/light_gray");
		public static final ITag.INamedTag<Item> WOOLS_CYAN = REGISTRY.itemTagOptionalForge("wools/cyan");
		public static final ITag.INamedTag<Item> WOOLS_PURPLE = REGISTRY.itemTagOptionalForge("wools/purple");
		public static final ITag.INamedTag<Item> WOOLS_BLUE = REGISTRY.itemTagOptionalForge("wools/blue");
		public static final ITag.INamedTag<Item> WOOLS_BROWN = REGISTRY.itemTagOptionalForge("wools/brown");
		public static final ITag.INamedTag<Item> WOOLS_GREEN = REGISTRY.itemTagOptionalForge("wools/green");
		public static final ITag.INamedTag<Item> WOOLS_RED = REGISTRY.itemTagOptionalForge("wools/red");
		public static final ITag.INamedTag<Item> WOOLS_BLACK = REGISTRY.itemTagOptionalForge("wools/black");
		public static final ITag.INamedTag<Item> WOOLS = REGISTRY.itemTagOptionalForge("wools");
		// endregion

		public static final ITag.INamedTag<Item> HATS = REGISTRY.itemTagModded("hats");

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
		// TODO: Move to ApexCore or PR into Forge
		public static final ITag.INamedTag<Block> WOOLS_WHITE = REGISTRY.blockTagOptionalForge("wools/white");
		public static final ITag.INamedTag<Block> WOOLS_ORANGE = REGISTRY.blockTagOptionalForge("wools/orange");
		public static final ITag.INamedTag<Block> WOOLS_MAGENTA = REGISTRY.blockTagOptionalForge("wools/magenta");
		public static final ITag.INamedTag<Block> WOOLS_LIGHT_BLUE = REGISTRY.blockTagOptionalForge("wools/light_blue");
		public static final ITag.INamedTag<Block> WOOLS_YELLOW = REGISTRY.blockTagOptionalForge("wools/yellow");
		public static final ITag.INamedTag<Block> WOOLS_LIME = REGISTRY.blockTagOptionalForge("wools/lime");
		public static final ITag.INamedTag<Block> WOOLS_PINK = REGISTRY.blockTagOptionalForge("wools/pink");
		public static final ITag.INamedTag<Block> WOOLS_GRAY = REGISTRY.blockTagOptionalForge("wools/gray");
		public static final ITag.INamedTag<Block> WOOLS_LIGHT_GRAY = REGISTRY.blockTagOptionalForge("wools/light_gray");
		public static final ITag.INamedTag<Block> WOOLS_CYAN = REGISTRY.blockTagOptionalForge("wools/cyan");
		public static final ITag.INamedTag<Block> WOOLS_PURPLE = REGISTRY.blockTagOptionalForge("wools/purple");
		public static final ITag.INamedTag<Block> WOOLS_BLUE = REGISTRY.blockTagOptionalForge("wools/blue");
		public static final ITag.INamedTag<Block> WOOLS_BROWN = REGISTRY.blockTagOptionalForge("wools/brown");
		public static final ITag.INamedTag<Block> WOOLS_GREEN = REGISTRY.blockTagOptionalForge("wools/green");
		public static final ITag.INamedTag<Block> WOOLS_RED = REGISTRY.blockTagOptionalForge("wools/red");
		public static final ITag.INamedTag<Block> WOOLS_BLACK = REGISTRY.blockTagOptionalForge("wools/black");
		public static final ITag.INamedTag<Block> WOOLS = REGISTRY.blockTagOptionalForge("wools");
		// endregion

		public static final Tags.IOptionalNamedTag<Block> CROPS = REGISTRY.blockTagOptionalForge("crops");
		public static final Tags.IOptionalNamedTag<Block> MUSHROOMS = REGISTRY.blockTagOptionalForge("mushrooms");

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
		public static final Tags.IOptionalNamedTag<EntityType<?>> COWS = REGISTRY.entityTypeTagOptionalForge("cows");
		public static final Tags.IOptionalNamedTag<EntityType<?>> CHICKENS = REGISTRY.entityTypeTagOptionalForge("chickens");
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
