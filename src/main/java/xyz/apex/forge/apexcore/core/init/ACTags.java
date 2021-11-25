package xyz.apex.forge.apexcore.core.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import xyz.apex.repack.com.tterrag.registrate.providers.ProviderType;

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
		// region: Forge / Vanilla Missing
		public static final Tags.IOptionalNamedTag<Item> PAPER = REGISTRY.itemTagOptionalForge(Names.PAPER);
		public static final Tags.IOptionalNamedTag<Item> APPLES = REGISTRY.itemTagOptionalForge(Names.APPLES);
		public static final Tags.IOptionalNamedTag<Item> SUGAR = REGISTRY.itemTagOptionalForge(Names.SUGAR);
		public static final Tags.IOptionalNamedTag<Item> GLASS_BOTTLES = REGISTRY.itemTagOptionalForge(Names.GLASS_BOTTLES);
		public static final Tags.IOptionalNamedTag<Item> SWEET_BERRIES = REGISTRY.itemTagOptionalForge(Names.SWEET_BERRIES);
		public static final Tags.IOptionalNamedTag<Item> MILK_BUCKET = REGISTRY.itemTagOptionalForge(Names.MILK_BUCKET);
		public static final Tags.IOptionalNamedTag<Item> BREAD = REGISTRY.itemTagOptionalForge(Names.BREAD);

		// region: Flowers
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_WHITE = REGISTRY.itemTagOptionalForge(Names.FLOWERS_WHITE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_ORANGE = REGISTRY.itemTagOptionalForge(Names.FLOWERS_ORANGE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_MAGENTA = REGISTRY.itemTagOptionalForge(Names.FLOWERS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_LIGHT_BLUE = REGISTRY.itemTagOptionalForge(Names.FLOWERS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_YELLOW = REGISTRY.itemTagOptionalForge(Names.FLOWERS_YELLOW);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_LIME = REGISTRY.itemTagOptionalForge(Names.FLOWERS_LIME);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_PINK = REGISTRY.itemTagOptionalForge(Names.FLOWERS_PINK);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_GRAY = REGISTRY.itemTagOptionalForge(Names.FLOWERS_GRAY);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_LIGHT_GRAY = REGISTRY.itemTagOptionalForge(Names.FLOWERS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_CYAN = REGISTRY.itemTagOptionalForge(Names.FLOWERS_CYAN);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_PURPLE = REGISTRY.itemTagOptionalForge(Names.FLOWERS_PURPLE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_BLUE = REGISTRY.itemTagOptionalForge(Names.FLOWERS_BLUE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_BROWN = REGISTRY.itemTagOptionalForge(Names.FLOWERS_BROWN);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_GREEN = REGISTRY.itemTagOptionalForge(Names.FLOWERS_GREEN);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_RED = REGISTRY.itemTagOptionalForge(Names.FLOWERS_RED);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_BLACK = REGISTRY.itemTagOptionalForge(Names.FLOWERS_BLACK);
		// endregion

		// region: Carpets
		public static final Tags.IOptionalNamedTag<Item> CARPETS_WHITE = REGISTRY.itemTagOptionalForge(Names.CARPETS_WHITE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_ORANGE = REGISTRY.itemTagOptionalForge(Names.CARPETS_ORANGE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_MAGENTA = REGISTRY.itemTagOptionalForge(Names.CARPETS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_LIGHT_BLUE = REGISTRY.itemTagOptionalForge(Names.CARPETS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_YELLOW = REGISTRY.itemTagOptionalForge(Names.CARPETS_YELLOW);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_LIME = REGISTRY.itemTagOptionalForge(Names.CARPETS_LIME);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_PINK = REGISTRY.itemTagOptionalForge(Names.CARPETS_PINK);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_GRAY = REGISTRY.itemTagOptionalForge(Names.CARPETS_GRAY);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_LIGHT_GRAY = REGISTRY.itemTagOptionalForge(Names.CARPETS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_CYAN = REGISTRY.itemTagOptionalForge(Names.CARPETS_CYAN);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_PURPLE = REGISTRY.itemTagOptionalForge(Names.CARPETS_PURPLE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_BLUE = REGISTRY.itemTagOptionalForge(Names.CARPETS_BLUE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_BROWN = REGISTRY.itemTagOptionalForge(Names.CARPETS_BROWN);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_GREEN = REGISTRY.itemTagOptionalForge(Names.CARPETS_GREEN);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_RED = REGISTRY.itemTagOptionalForge(Names.CARPETS_RED);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_BLACK = REGISTRY.itemTagOptionalForge(Names.CARPETS_BLACK);
		// endregion

		// region: Wools
		public static final Tags.IOptionalNamedTag<Item> WOOLS = REGISTRY.itemTagOptionalForge(Names.WOOLS);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_WHITE = REGISTRY.itemTagOptionalForge(Names.WOOLS_WHITE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_ORANGE = REGISTRY.itemTagOptionalForge(Names.WOOLS_ORANGE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_MAGENTA = REGISTRY.itemTagOptionalForge(Names.WOOLS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_LIGHT_BLUE = REGISTRY.itemTagOptionalForge(Names.WOOLS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_YELLOW = REGISTRY.itemTagOptionalForge(Names.WOOLS_YELLOW);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_LIME = REGISTRY.itemTagOptionalForge(Names.WOOLS_LIME);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_PINK = REGISTRY.itemTagOptionalForge(Names.WOOLS_PINK);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_GRAY = REGISTRY.itemTagOptionalForge(Names.WOOLS_GRAY);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_LIGHT_GRAY = REGISTRY.itemTagOptionalForge(Names.WOOLS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_CYAN = REGISTRY.itemTagOptionalForge(Names.WOOLS_CYAN);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_PURPLE = REGISTRY.itemTagOptionalForge(Names.WOOLS_PURPLE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_BLUE = REGISTRY.itemTagOptionalForge(Names.WOOLS_BLUE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_BROWN = REGISTRY.itemTagOptionalForge(Names.WOOLS_BROWN);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_GREEN = REGISTRY.itemTagOptionalForge(Names.WOOLS_GREEN);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_RED = REGISTRY.itemTagOptionalForge(Names.WOOLS_RED);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_BLACK = REGISTRY.itemTagOptionalForge(Names.WOOLS_BLACK);
		// endregion
		// endregion

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.ITEM_TAGS, provider -> {
				// region: Forge / Vanilla Missing
				provider.tag(PAPER).add(net.minecraft.item.Items.PAPER);
				provider.tag(APPLES).add(net.minecraft.item.Items.APPLE);
				provider.tag(SUGAR).add(net.minecraft.item.Items.SUGAR);
				provider.tag(GLASS_BOTTLES).add(net.minecraft.item.Items.GLASS_BOTTLE);
				provider.tag(SWEET_BERRIES).add(net.minecraft.item.Items.SWEET_BERRIES);
				provider.tag(MILK_BUCKET).add(net.minecraft.item.Items.MILK_BUCKET);
				provider.tag(BREAD).add(net.minecraft.item.Items.BREAD);

				// region: Flowers
				provider.tag(FLOWERS_WHITE).add(net.minecraft.item.Items.LILY_OF_THE_VALLEY);
				provider.tag(FLOWERS_ORANGE).add(net.minecraft.item.Items.ORANGE_TULIP);
				provider.tag(FLOWERS_MAGENTA).add(net.minecraft.item.Items.LILAC, net.minecraft.item.Items.ALLIUM);
				provider.tag(FLOWERS_LIGHT_BLUE).add(net.minecraft.item.Items.BLUE_ORCHID);
				provider.tag(FLOWERS_YELLOW).add(net.minecraft.item.Items.DANDELION, net.minecraft.item.Items.SUNFLOWER);
				provider.tag(FLOWERS_LIME);
				provider.tag(FLOWERS_PINK).add(net.minecraft.item.Items.PINK_TULIP, net.minecraft.item.Items.PEONY);
				provider.tag(FLOWERS_GRAY);
				provider.tag(FLOWERS_LIGHT_GRAY).add(net.minecraft.item.Items.OXEYE_DAISY, net.minecraft.item.Items.AZURE_BLUET, net.minecraft.item.Items.WHITE_TULIP);
				provider.tag(FLOWERS_CYAN);
				provider.tag(FLOWERS_PURPLE);
				provider.tag(FLOWERS_BLUE).add(net.minecraft.item.Items.CORNFLOWER);
				provider.tag(FLOWERS_BROWN);
				provider.tag(FLOWERS_GREEN).add(net.minecraft.item.Items.CACTUS);
				provider.tag(FLOWERS_RED).add(net.minecraft.item.Items.RED_TULIP, net.minecraft.item.Items.POPPY, net.minecraft.item.Items.ROSE_BUSH);
				provider.tag(FLOWERS_BLACK).add(net.minecraft.item.Items.WITHER_ROSE);

				provider.tag(ItemTags.FLOWERS).addTags(
						FLOWERS_WHITE,
						FLOWERS_ORANGE,
						FLOWERS_MAGENTA,
						FLOWERS_LIGHT_BLUE,
						FLOWERS_YELLOW,
						FLOWERS_LIME,
						FLOWERS_PINK,
						FLOWERS_GRAY,
						FLOWERS_LIGHT_GRAY,
						FLOWERS_CYAN,
						FLOWERS_PURPLE,
						FLOWERS_BLUE,
						FLOWERS_BROWN,
						FLOWERS_GREEN,
						FLOWERS_RED,
						FLOWERS_BLACK
				);
				// endregion

				// region: Carpets
				provider.tag(CARPETS_WHITE).add(net.minecraft.item.Items.WHITE_CARPET);
				provider.tag(CARPETS_ORANGE).add(net.minecraft.item.Items.ORANGE_CARPET);
				provider.tag(CARPETS_MAGENTA).add(net.minecraft.item.Items.MAGENTA_CARPET);
				provider.tag(CARPETS_LIGHT_BLUE).add(net.minecraft.item.Items.LIGHT_BLUE_CARPET);
				provider.tag(CARPETS_YELLOW).add(net.minecraft.item.Items.YELLOW_CARPET);
				provider.tag(CARPETS_LIME).add(net.minecraft.item.Items.LIME_CARPET);
				provider.tag(CARPETS_PINK).add(net.minecraft.item.Items.PINK_CARPET);
				provider.tag(CARPETS_GRAY).add(net.minecraft.item.Items.GRAY_CARPET);
				provider.tag(CARPETS_LIGHT_GRAY).add(net.minecraft.item.Items.LIGHT_GRAY_CARPET);
				provider.tag(CARPETS_CYAN).add(net.minecraft.item.Items.CYAN_CARPET);
				provider.tag(CARPETS_PURPLE).add(net.minecraft.item.Items.PURPLE_CARPET);
				provider.tag(CARPETS_BLUE).add(net.minecraft.item.Items.BLUE_CARPET);
				provider.tag(CARPETS_BROWN).add(net.minecraft.item.Items.BROWN_CARPET);
				provider.tag(CARPETS_GREEN).add(net.minecraft.item.Items.GREEN_CARPET);
				provider.tag(CARPETS_RED).add(net.minecraft.item.Items.RED_CARPET);
				provider.tag(CARPETS_BLACK).add(net.minecraft.item.Items.BLACK_CARPET);

				provider.tag(ItemTags.CARPETS).addTags(
						CARPETS_WHITE,
						CARPETS_ORANGE,
						CARPETS_MAGENTA,
						CARPETS_LIGHT_BLUE,
						CARPETS_YELLOW,
						CARPETS_LIME,
						CARPETS_PINK,
						CARPETS_GRAY,
						CARPETS_LIGHT_GRAY,
						CARPETS_CYAN,
						CARPETS_PURPLE,
						CARPETS_BLUE,
						CARPETS_BROWN,
						CARPETS_GREEN,
						CARPETS_RED,
						CARPETS_BLACK
				);
				// endregion

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

				provider.tag(WOOLS).addTags(
						WOOLS_WHITE,
						WOOLS_ORANGE,
						WOOLS_MAGENTA,
						WOOLS_LIGHT_BLUE,
						WOOLS_YELLOW,
						WOOLS_LIME,
						WOOLS_PINK,
						WOOLS_GRAY,
						WOOLS_LIGHT_GRAY,
						WOOLS_CYAN,
						WOOLS_PURPLE,
						WOOLS_BLUE,
						WOOLS_BROWN,
						WOOLS_GREEN,
						WOOLS_RED,
						WOOLS_BLACK
				);

				provider.tag(ItemTags.WOOL).addTags(WOOLS);
				// endregion
				// endregion
			});
		}
	}

	public static final class Blocks
	{
		// region: Forge / Vanilla Missing
		public static final Tags.IOptionalNamedTag<Block> CROPS = REGISTRY.blockTagOptionalForge(Names.CROPS);

		// region: Flowers
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_WHITE = REGISTRY.blockTagOptionalForge(Names.FLOWERS_WHITE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_ORANGE = REGISTRY.blockTagOptionalForge(Names.FLOWERS_ORANGE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_MAGENTA = REGISTRY.blockTagOptionalForge(Names.FLOWERS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_LIGHT_BLUE = REGISTRY.blockTagOptionalForge(Names.FLOWERS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_YELLOW = REGISTRY.blockTagOptionalForge(Names.FLOWERS_YELLOW);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_LIME = REGISTRY.blockTagOptionalForge(Names.FLOWERS_LIME);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_PINK = REGISTRY.blockTagOptionalForge(Names.FLOWERS_PINK);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_GRAY = REGISTRY.blockTagOptionalForge(Names.FLOWERS_GRAY);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_LIGHT_GRAY = REGISTRY.blockTagOptionalForge(Names.FLOWERS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_CYAN = REGISTRY.blockTagOptionalForge(Names.FLOWERS_CYAN);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_PURPLE = REGISTRY.blockTagOptionalForge(Names.FLOWERS_PURPLE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_BLUE = REGISTRY.blockTagOptionalForge(Names.FLOWERS_BLUE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_BROWN = REGISTRY.blockTagOptionalForge(Names.FLOWERS_BROWN);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_GREEN = REGISTRY.blockTagOptionalForge(Names.FLOWERS_GREEN);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_RED = REGISTRY.blockTagOptionalForge(Names.FLOWERS_RED);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_BLACK = REGISTRY.blockTagOptionalForge(Names.FLOWERS_BLACK);
		// endregion

		// region: Carpets
		public static final Tags.IOptionalNamedTag<Block> CARPETS_WHITE = REGISTRY.blockTagOptionalForge(Names.CARPETS_WHITE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_ORANGE = REGISTRY.blockTagOptionalForge(Names.CARPETS_ORANGE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_MAGENTA = REGISTRY.blockTagOptionalForge(Names.CARPETS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_LIGHT_BLUE = REGISTRY.blockTagOptionalForge(Names.CARPETS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_YELLOW = REGISTRY.blockTagOptionalForge(Names.CARPETS_YELLOW);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_LIME = REGISTRY.blockTagOptionalForge(Names.CARPETS_LIME);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_PINK = REGISTRY.blockTagOptionalForge(Names.CARPETS_PINK);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_GRAY = REGISTRY.blockTagOptionalForge(Names.CARPETS_GRAY);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_LIGHT_GRAY = REGISTRY.blockTagOptionalForge(Names.CARPETS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_CYAN = REGISTRY.blockTagOptionalForge(Names.CARPETS_CYAN);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_PURPLE = REGISTRY.blockTagOptionalForge(Names.CARPETS_PURPLE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_BLUE = REGISTRY.blockTagOptionalForge(Names.CARPETS_BLUE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_BROWN = REGISTRY.blockTagOptionalForge(Names.CARPETS_BROWN);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_GREEN = REGISTRY.blockTagOptionalForge(Names.CARPETS_GREEN);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_RED = REGISTRY.blockTagOptionalForge(Names.CARPETS_RED);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_BLACK = REGISTRY.blockTagOptionalForge(Names.CARPETS_BLACK);
		// endregion

		// region: Wools
		public static final Tags.IOptionalNamedTag<Block> WOOLS = REGISTRY.blockTagOptionalForge(Names.WOOLS);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_WHITE = REGISTRY.blockTagOptionalForge(Names.WOOLS_WHITE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_ORANGE = REGISTRY.blockTagOptionalForge(Names.WOOLS_ORANGE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_MAGENTA = REGISTRY.blockTagOptionalForge(Names.WOOLS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_LIGHT_BLUE = REGISTRY.blockTagOptionalForge(Names.WOOLS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_YELLOW = REGISTRY.blockTagOptionalForge(Names.WOOLS_YELLOW);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_LIME = REGISTRY.blockTagOptionalForge(Names.WOOLS_LIME);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_PINK = REGISTRY.blockTagOptionalForge(Names.WOOLS_PINK);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_GRAY = REGISTRY.blockTagOptionalForge(Names.WOOLS_GRAY);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_LIGHT_GRAY = REGISTRY.blockTagOptionalForge(Names.WOOLS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_CYAN = REGISTRY.blockTagOptionalForge(Names.WOOLS_CYAN);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_PURPLE = REGISTRY.blockTagOptionalForge(Names.WOOLS_PURPLE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_BLUE = REGISTRY.blockTagOptionalForge(Names.WOOLS_BLUE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_BROWN = REGISTRY.blockTagOptionalForge(Names.WOOLS_BROWN);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_GREEN = REGISTRY.blockTagOptionalForge(Names.WOOLS_GREEN);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_RED = REGISTRY.blockTagOptionalForge(Names.WOOLS_RED);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_BLACK = REGISTRY.blockTagOptionalForge(Names.WOOLS_BLACK);
		// endregion
		// endregion

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.BLOCK_TAGS, provider -> {
				// region: Forge / Vanilla Missing
				// region: Crops
				provider.tag(CROPS);
				provider.tag(BlockTags.CROPS).addTags(CROPS);
				// endregion

				// region: Flowers
				provider.tag(FLOWERS_WHITE).add(net.minecraft.block.Blocks.LILY_OF_THE_VALLEY);
				provider.tag(FLOWERS_ORANGE).add(net.minecraft.block.Blocks.ORANGE_TULIP);
				provider.tag(FLOWERS_MAGENTA).add(net.minecraft.block.Blocks.LILAC, net.minecraft.block.Blocks.ALLIUM);
				provider.tag(FLOWERS_LIGHT_BLUE).add(net.minecraft.block.Blocks.BLUE_ORCHID);
				provider.tag(FLOWERS_YELLOW).add(net.minecraft.block.Blocks.DANDELION, net.minecraft.block.Blocks.SUNFLOWER);
				provider.tag(FLOWERS_LIME);
				provider.tag(FLOWERS_PINK).add(net.minecraft.block.Blocks.PINK_TULIP, net.minecraft.block.Blocks.PEONY);
				provider.tag(FLOWERS_GRAY);
				provider.tag(FLOWERS_LIGHT_GRAY).add(net.minecraft.block.Blocks.OXEYE_DAISY, net.minecraft.block.Blocks.AZURE_BLUET, net.minecraft.block.Blocks.WHITE_TULIP);
				provider.tag(FLOWERS_CYAN);
				provider.tag(FLOWERS_PURPLE);
				provider.tag(FLOWERS_BLUE).add(net.minecraft.block.Blocks.CORNFLOWER);
				provider.tag(FLOWERS_BROWN);
				provider.tag(FLOWERS_GREEN).add(net.minecraft.block.Blocks.CACTUS);
				provider.tag(FLOWERS_RED).add(net.minecraft.block.Blocks.RED_TULIP, net.minecraft.block.Blocks.POPPY, net.minecraft.block.Blocks.ROSE_BUSH);
				provider.tag(FLOWERS_BLACK).add(net.minecraft.block.Blocks.WITHER_ROSE);

				provider.tag(BlockTags.FLOWERS).addTags(
						FLOWERS_WHITE,
						FLOWERS_ORANGE,
						FLOWERS_MAGENTA,
						FLOWERS_LIGHT_BLUE,
						FLOWERS_YELLOW,
						FLOWERS_LIME,
						FLOWERS_PINK,
						FLOWERS_GRAY,
						FLOWERS_LIGHT_GRAY,
						FLOWERS_CYAN,
						FLOWERS_PURPLE,
						FLOWERS_BLUE,
						FLOWERS_BROWN,
						FLOWERS_GREEN,
						FLOWERS_RED,
						FLOWERS_BLACK
				);
				// endregion

				// region: Carpets
				provider.tag(CARPETS_WHITE).add(net.minecraft.block.Blocks.WHITE_CARPET);
				provider.tag(CARPETS_ORANGE).add(net.minecraft.block.Blocks.ORANGE_CARPET);
				provider.tag(CARPETS_MAGENTA).add(net.minecraft.block.Blocks.MAGENTA_CARPET);
				provider.tag(CARPETS_LIGHT_BLUE).add(net.minecraft.block.Blocks.LIGHT_BLUE_CARPET);
				provider.tag(CARPETS_YELLOW).add(net.minecraft.block.Blocks.YELLOW_CARPET);
				provider.tag(CARPETS_LIME).add(net.minecraft.block.Blocks.LIME_CARPET);
				provider.tag(CARPETS_PINK).add(net.minecraft.block.Blocks.PINK_CARPET);
				provider.tag(CARPETS_GRAY).add(net.minecraft.block.Blocks.GRAY_CARPET);
				provider.tag(CARPETS_LIGHT_GRAY).add(net.minecraft.block.Blocks.LIGHT_GRAY_CARPET);
				provider.tag(CARPETS_CYAN).add(net.minecraft.block.Blocks.CYAN_CARPET);
				provider.tag(CARPETS_PURPLE).add(net.minecraft.block.Blocks.PURPLE_CARPET);
				provider.tag(CARPETS_BLUE).add(net.minecraft.block.Blocks.BLUE_CARPET);
				provider.tag(CARPETS_BROWN).add(net.minecraft.block.Blocks.BROWN_CARPET);
				provider.tag(CARPETS_GREEN).add(net.minecraft.block.Blocks.GREEN_CARPET);
				provider.tag(CARPETS_RED).add(net.minecraft.block.Blocks.RED_CARPET);
				provider.tag(CARPETS_BLACK).add(net.minecraft.block.Blocks.BLACK_CARPET);

				provider.tag(BlockTags.CARPETS).addTags(
						CARPETS_WHITE,
						CARPETS_ORANGE,
						CARPETS_MAGENTA,
						CARPETS_LIGHT_BLUE,
						CARPETS_YELLOW,
						CARPETS_LIME,
						CARPETS_PINK,
						CARPETS_GRAY,
						CARPETS_LIGHT_GRAY,
						CARPETS_CYAN,
						CARPETS_PURPLE,
						CARPETS_BLUE,
						CARPETS_BROWN,
						CARPETS_GREEN,
						CARPETS_RED,
						CARPETS_BLACK
				);
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

				provider.tag(WOOLS).addTags(
						WOOLS_WHITE,
						WOOLS_ORANGE,
						WOOLS_MAGENTA,
						WOOLS_LIGHT_BLUE,
						WOOLS_YELLOW,
						WOOLS_LIME,
						WOOLS_PINK,
						WOOLS_GRAY,
						WOOLS_LIGHT_GRAY,
						WOOLS_CYAN,
						WOOLS_PURPLE,
						WOOLS_BLUE,
						WOOLS_BROWN,
						WOOLS_GREEN,
						WOOLS_RED,
						WOOLS_BLACK
				);

				provider.tag(BlockTags.WOOL).addTags(WOOLS);
				// endregion
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
		public static final Tags.IOptionalNamedTag<EntityType<?>> COWS = REGISTRY.entityTypeTagOptionalForge(Names.COWS);
		public static final Tags.IOptionalNamedTag<EntityType<?>> CHICKENS = REGISTRY.entityTypeTagOptionalForge(Names.CHICKENS);
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

	public static final class Names
	{
		// region: Forge / Vanilla Missing
		// Some of these should maybe be PR'd into Forge itself
		public static final String PAPER = "paper";
		public static final String CROPS = "crops";
		public static final String COWS = "cows";
		public static final String CHICKENS = "chickens";
		public static final String APPLES = "apples";
		public static final String SUGAR = "sugar";
		public static final String GLASS_BOTTLES = "glass_bottles";
		public static final String SWEET_BERRIES = "sweet_berries";
		public static final String MILK_BUCKET = "milk_bucket";
		public static final String BREAD = "bread";

		// region: Flowers
		public static final String FLOWERS_WHITE = "flowers/white";
		public static final String FLOWERS_ORANGE = "flowers/orange";
		public static final String FLOWERS_MAGENTA = "flowers/magenta";
		public static final String FLOWERS_LIGHT_BLUE = "flowers/light_blue";
		public static final String FLOWERS_YELLOW = "flowers/yellow";
		public static final String FLOWERS_LIME = "flowers/lime";
		public static final String FLOWERS_PINK = "flowers/pink";
		public static final String FLOWERS_GRAY = "flowers/gray";
		public static final String FLOWERS_LIGHT_GRAY = "flowers/light_gray";
		public static final String FLOWERS_CYAN = "flowers/cyan";
		public static final String FLOWERS_PURPLE = "flowers/purple";
		public static final String FLOWERS_BLUE = "flowers/blue";
		public static final String FLOWERS_BROWN = "flowers/brown";
		public static final String FLOWERS_GREEN = "flowers/green";
		public static final String FLOWERS_RED = "flowers/red";
		public static final String FLOWERS_BLACK = "flowers/black";
		// endregion

		// region: Carpets
		public static final String CARPETS_WHITE = "carpets/white";
		public static final String CARPETS_ORANGE = "carpets/orange";
		public static final String CARPETS_MAGENTA = "carpets/magenta";
		public static final String CARPETS_LIGHT_BLUE = "carpets/light_blue";
		public static final String CARPETS_YELLOW = "carpets/yellow";
		public static final String CARPETS_LIME = "carpets/lime";
		public static final String CARPETS_PINK = "carpets/pink";
		public static final String CARPETS_GRAY = "carpets/gray";
		public static final String CARPETS_LIGHT_GRAY = "carpets/light_gray";
		public static final String CARPETS_CYAN = "carpets/cyan";
		public static final String CARPETS_PURPLE = "carpets/purple";
		public static final String CARPETS_BLUE = "carpets/blue";
		public static final String CARPETS_BROWN = "carpets/brown";
		public static final String CARPETS_GREEN = "carpets/green";
		public static final String CARPETS_RED = "carpets/red";
		public static final String CARPETS_BLACK = "carpets/black";
		// endregion

		// region: Wools
		public static final String WOOLS = "wools";
		public static final String WOOLS_WHITE = "wools/white";
		public static final String WOOLS_ORANGE = "wools/orange";
		public static final String WOOLS_MAGENTA = "wools/magenta";
		public static final String WOOLS_LIGHT_BLUE = "wools/light_blue";
		public static final String WOOLS_YELLOW = "wools/yellow";
		public static final String WOOLS_LIME = "wools/lime";
		public static final String WOOLS_PINK = "wools/pink";
		public static final String WOOLS_GRAY = "wools/gray";
		public static final String WOOLS_LIGHT_GRAY = "wools/light_gray";
		public static final String WOOLS_CYAN = "wools/cyan";
		public static final String WOOLS_PURPLE = "wools/purple";
		public static final String WOOLS_BLUE = "wools/blue";
		public static final String WOOLS_BROWN = "wools/brown";
		public static final String WOOLS_GREEN = "wools/green";
		public static final String WOOLS_RED = "wools/red";
		public static final String WOOLS_BLACK = "wools/black";
		// endregion
		// endregion
	}
}
