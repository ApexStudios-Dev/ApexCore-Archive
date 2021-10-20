package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import xyz.apex.forge.apexcore.lib.util.IMod;

public final class ATags
{
	public static final class Items
	{
		// region: Forge / Vanilla Missing
		public static final Tags.IOptionalNamedTag<Item> PAPER = tag(Names.PAPER);
		public static final Tags.IOptionalNamedTag<Item> APPLES = tag(Names.APPLES);
		public static final Tags.IOptionalNamedTag<Item> SUGAR = tag(Names.SUGAR);
		public static final Tags.IOptionalNamedTag<Item> GLASS_BOTTLES = tag(Names.GLASS_BOTTLES);
		public static final Tags.IOptionalNamedTag<Item> SWEET_BERRIES = tag(Names.SWEET_BERRIES);
		public static final Tags.IOptionalNamedTag<Item> MILK_BUCKET = tag(Names.MILK_BUCKET);
		public static final Tags.IOptionalNamedTag<Item> BREAD = tag(Names.BREAD);

		// region: Flowers
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_WHITE = tag(Names.FLOWERS_WHITE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_ORANGE = tag(Names.FLOWERS_ORANGE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_MAGENTA = tag(Names.FLOWERS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_LIGHT_BLUE = tag(Names.FLOWERS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_YELLOW = tag(Names.FLOWERS_YELLOW);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_LIME = tag(Names.FLOWERS_LIME);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_PINK = tag(Names.FLOWERS_PINK);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_GRAY = tag(Names.FLOWERS_GRAY);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_LIGHT_GRAY = tag(Names.FLOWERS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_CYAN = tag(Names.FLOWERS_CYAN);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_PURPLE = tag(Names.FLOWERS_PURPLE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_BLUE = tag(Names.FLOWERS_BLUE);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_BROWN = tag(Names.FLOWERS_BROWN);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_GREEN = tag(Names.FLOWERS_GREEN);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_RED = tag(Names.FLOWERS_RED);
		public static final Tags.IOptionalNamedTag<Item> FLOWERS_BLACK = tag(Names.FLOWERS_BLACK);
		// endregion

		// region: Carpets
		public static final Tags.IOptionalNamedTag<Item> CARPETS_WHITE = tag(Names.CARPETS_WHITE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_ORANGE = tag(Names.CARPETS_ORANGE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_MAGENTA = tag(Names.CARPETS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_LIGHT_BLUE = tag(Names.CARPETS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_YELLOW = tag(Names.CARPETS_YELLOW);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_LIME = tag(Names.CARPETS_LIME);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_PINK = tag(Names.CARPETS_PINK);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_GRAY = tag(Names.CARPETS_GRAY);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_LIGHT_GRAY = tag(Names.CARPETS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_CYAN = tag(Names.CARPETS_CYAN);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_PURPLE = tag(Names.CARPETS_PURPLE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_BLUE = tag(Names.CARPETS_BLUE);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_BROWN = tag(Names.CARPETS_BROWN);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_GREEN = tag(Names.CARPETS_GREEN);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_RED = tag(Names.CARPETS_RED);
		public static final Tags.IOptionalNamedTag<Item> CARPETS_BLACK = tag(Names.CARPETS_BLACK);
		// endregion

		// region: Wools
		public static final Tags.IOptionalNamedTag<Item> WOOLS = tag(Names.WOOLS);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_WHITE = tag(Names.WOOLS_WHITE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_ORANGE = tag(Names.WOOLS_ORANGE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_MAGENTA = tag(Names.WOOLS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_LIGHT_BLUE = tag(Names.WOOLS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_YELLOW = tag(Names.WOOLS_YELLOW);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_LIME = tag(Names.WOOLS_LIME);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_PINK = tag(Names.WOOLS_PINK);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_GRAY = tag(Names.WOOLS_GRAY);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_LIGHT_GRAY = tag(Names.WOOLS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_CYAN = tag(Names.WOOLS_CYAN);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_PURPLE = tag(Names.WOOLS_PURPLE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_BLUE = tag(Names.WOOLS_BLUE);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_BROWN = tag(Names.WOOLS_BROWN);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_GREEN = tag(Names.WOOLS_GREEN);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_RED = tag(Names.WOOLS_RED);
		public static final Tags.IOptionalNamedTag<Item> WOOLS_BLACK = tag(Names.WOOLS_BLACK);
		// endregion
		// endregion

		public static void generate(RegistrateItemTagsProvider provider)
		{
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
		}

		private static Tags.IOptionalNamedTag<Item> tag(ResourceLocation tagName)
		{
			return ItemTags.createOptional(tagName);
		}
	}

	public static final class Blocks
	{
		// region: Forge / Vanilla Missing
		public static final Tags.IOptionalNamedTag<Block> CROPS = tag(Names.CROPS);

		// region: Flowers
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_WHITE = tag(Names.FLOWERS_WHITE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_ORANGE = tag(Names.FLOWERS_ORANGE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_MAGENTA = tag(Names.FLOWERS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_LIGHT_BLUE = tag(Names.FLOWERS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_YELLOW = tag(Names.FLOWERS_YELLOW);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_LIME = tag(Names.FLOWERS_LIME);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_PINK = tag(Names.FLOWERS_PINK);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_GRAY = tag(Names.FLOWERS_GRAY);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_LIGHT_GRAY = tag(Names.FLOWERS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_CYAN = tag(Names.FLOWERS_CYAN);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_PURPLE = tag(Names.FLOWERS_PURPLE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_BLUE = tag(Names.FLOWERS_BLUE);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_BROWN = tag(Names.FLOWERS_BROWN);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_GREEN = tag(Names.FLOWERS_GREEN);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_RED = tag(Names.FLOWERS_RED);
		public static final Tags.IOptionalNamedTag<Block> FLOWERS_BLACK = tag(Names.FLOWERS_BLACK);
		// endregion

		// region: Carpets
		public static final Tags.IOptionalNamedTag<Block> CARPETS_WHITE = tag(Names.CARPETS_WHITE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_ORANGE = tag(Names.CARPETS_ORANGE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_MAGENTA = tag(Names.CARPETS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_LIGHT_BLUE = tag(Names.CARPETS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_YELLOW = tag(Names.CARPETS_YELLOW);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_LIME = tag(Names.CARPETS_LIME);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_PINK = tag(Names.CARPETS_PINK);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_GRAY = tag(Names.CARPETS_GRAY);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_LIGHT_GRAY = tag(Names.CARPETS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_CYAN = tag(Names.CARPETS_CYAN);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_PURPLE = tag(Names.CARPETS_PURPLE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_BLUE = tag(Names.CARPETS_BLUE);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_BROWN = tag(Names.CARPETS_BROWN);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_GREEN = tag(Names.CARPETS_GREEN);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_RED = tag(Names.CARPETS_RED);
		public static final Tags.IOptionalNamedTag<Block> CARPETS_BLACK = tag(Names.CARPETS_BLACK);
		// endregion

		// region: Wools
		public static final Tags.IOptionalNamedTag<Block> WOOLS = tag(Names.WOOLS);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_WHITE = tag(Names.WOOLS_WHITE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_ORANGE = tag(Names.WOOLS_ORANGE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_MAGENTA = tag(Names.WOOLS_MAGENTA);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_LIGHT_BLUE = tag(Names.WOOLS_LIGHT_BLUE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_YELLOW = tag(Names.WOOLS_YELLOW);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_LIME = tag(Names.WOOLS_LIME);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_PINK = tag(Names.WOOLS_PINK);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_GRAY = tag(Names.WOOLS_GRAY);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_LIGHT_GRAY = tag(Names.WOOLS_LIGHT_GRAY);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_CYAN = tag(Names.WOOLS_CYAN);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_PURPLE = tag(Names.WOOLS_PURPLE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_BLUE = tag(Names.WOOLS_BLUE);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_BROWN = tag(Names.WOOLS_BROWN);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_GREEN = tag(Names.WOOLS_GREEN);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_RED = tag(Names.WOOLS_RED);
		public static final Tags.IOptionalNamedTag<Block> WOOLS_BLACK = tag(Names.WOOLS_BLACK);
		// endregion
		// endregion

		public static void generate(RegistrateTagsProvider<Block> provider)
		{
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
		}

		private static Tags.IOptionalNamedTag<Block> tag(ResourceLocation tagName)
		{
			return BlockTags.createOptional(tagName);
		}
	}

	public static final class Fluids
	{
		public static void generate(RegistrateTagsProvider<Fluid> provider) { }

		private static Tags.IOptionalNamedTag<Fluid> tag(ResourceLocation tagName)
		{
			return FluidTags.createOptional(tagName);
		}
	}

	public static final class EntityTypes
	{
		// region: Forge / Vanilla Missing
		public static final Tags.IOptionalNamedTag<EntityType<?>> COWS = tag(Names.COWS);
		public static final Tags.IOptionalNamedTag<EntityType<?>> CHICKENS = tag(Names.CHICKENS);
		// endregion

		public static void generate(RegistrateTagsProvider<EntityType<?>> provider)
		{
			// region: Forge / Vanilla Missing
			provider.tag(COWS).add(EntityType.COW);
			provider.tag(CHICKENS).add(EntityType.CHICKEN);
			// endregion
		}

		private static Tags.IOptionalNamedTag<EntityType<?>> tag(ResourceLocation tagName)
		{
			return EntityTypeTags.createOptional(tagName);
		}
	}

	public static final class Names
	{
		// region: Forge / Vanilla Missing
		// Some of these should maybe be PR'd into Forge itself
		public static final ResourceLocation PAPER = IMod.FORGE.id("paper");
		public static final ResourceLocation CROPS = IMod.FORGE.id("crops");
		public static final ResourceLocation COWS = IMod.FORGE.id("cows");
		public static final ResourceLocation CHICKENS = IMod.FORGE.id("chickens");
		public static final ResourceLocation APPLES = IMod.FORGE.id("apples");
		public static final ResourceLocation SUGAR = IMod.FORGE.id("sugar");
		public static final ResourceLocation GLASS_BOTTLES = IMod.FORGE.id("glass_bottles");
		public static final ResourceLocation SWEET_BERRIES = IMod.FORGE.id("sweet_berries");
		public static final ResourceLocation MILK_BUCKET = IMod.FORGE.id("milk_bucket");
		public static final ResourceLocation BREAD = IMod.FORGE.id("bread");

		// region: Flowers
		public static final ResourceLocation FLOWERS_WHITE = IMod.FORGE.id("flowers/white");
		public static final ResourceLocation FLOWERS_ORANGE = IMod.FORGE.id("flowers/orange");
		public static final ResourceLocation FLOWERS_MAGENTA = IMod.FORGE.id("flowers/magenta");
		public static final ResourceLocation FLOWERS_LIGHT_BLUE = IMod.FORGE.id("flowers/light_blue");
		public static final ResourceLocation FLOWERS_YELLOW = IMod.FORGE.id("flowers/yellow");
		public static final ResourceLocation FLOWERS_LIME = IMod.FORGE.id("flowers/lime");
		public static final ResourceLocation FLOWERS_PINK = IMod.FORGE.id("flowers/pink");
		public static final ResourceLocation FLOWERS_GRAY = IMod.FORGE.id("flowers/gray");
		public static final ResourceLocation FLOWERS_LIGHT_GRAY = IMod.FORGE.id("flowers/light_gray");
		public static final ResourceLocation FLOWERS_CYAN = IMod.FORGE.id("flowers/cyan");
		public static final ResourceLocation FLOWERS_PURPLE = IMod.FORGE.id("flowers/purple");
		public static final ResourceLocation FLOWERS_BLUE = IMod.FORGE.id("flowers/blue");
		public static final ResourceLocation FLOWERS_BROWN = IMod.FORGE.id("flowers/brown");
		public static final ResourceLocation FLOWERS_GREEN = IMod.FORGE.id("flowers/green");
		public static final ResourceLocation FLOWERS_RED = IMod.FORGE.id("flowers/red");
		public static final ResourceLocation FLOWERS_BLACK = IMod.FORGE.id("flowers/black");
		// endregion

		// region: Carpets
		public static final ResourceLocation CARPETS_WHITE = IMod.FORGE.id("carpets/white");
		public static final ResourceLocation CARPETS_ORANGE = IMod.FORGE.id("carpets/orange");
		public static final ResourceLocation CARPETS_MAGENTA = IMod.FORGE.id("carpets/magenta");
		public static final ResourceLocation CARPETS_LIGHT_BLUE = IMod.FORGE.id("carpets/light_blue");
		public static final ResourceLocation CARPETS_YELLOW = IMod.FORGE.id("carpets/yellow");
		public static final ResourceLocation CARPETS_LIME = IMod.FORGE.id("carpets/lime");
		public static final ResourceLocation CARPETS_PINK = IMod.FORGE.id("carpets/pink");
		public static final ResourceLocation CARPETS_GRAY = IMod.FORGE.id("carpets/gray");
		public static final ResourceLocation CARPETS_LIGHT_GRAY = IMod.FORGE.id("carpets/light_gray");
		public static final ResourceLocation CARPETS_CYAN = IMod.FORGE.id("carpets/cyan");
		public static final ResourceLocation CARPETS_PURPLE = IMod.FORGE.id("carpets/purple");
		public static final ResourceLocation CARPETS_BLUE = IMod.FORGE.id("carpets/blue");
		public static final ResourceLocation CARPETS_BROWN = IMod.FORGE.id("carpets/brown");
		public static final ResourceLocation CARPETS_GREEN = IMod.FORGE.id("carpets/green");
		public static final ResourceLocation CARPETS_RED = IMod.FORGE.id("carpets/red");
		public static final ResourceLocation CARPETS_BLACK = IMod.FORGE.id("carpets/black");
		// endregion

		// region: Wools
		public static final ResourceLocation WOOLS = IMod.FORGE.id("wools");
		public static final ResourceLocation WOOLS_WHITE = IMod.FORGE.id("wools/white");
		public static final ResourceLocation WOOLS_ORANGE = IMod.FORGE.id("wools/orange");
		public static final ResourceLocation WOOLS_MAGENTA = IMod.FORGE.id("wools/magenta");
		public static final ResourceLocation WOOLS_LIGHT_BLUE = IMod.FORGE.id("wools/light_blue");
		public static final ResourceLocation WOOLS_YELLOW = IMod.FORGE.id("wools/yellow");
		public static final ResourceLocation WOOLS_LIME = IMod.FORGE.id("wools/lime");
		public static final ResourceLocation WOOLS_PINK = IMod.FORGE.id("wools/pink");
		public static final ResourceLocation WOOLS_GRAY = IMod.FORGE.id("wools/gray");
		public static final ResourceLocation WOOLS_LIGHT_GRAY = IMod.FORGE.id("wools/light_gray");
		public static final ResourceLocation WOOLS_CYAN = IMod.FORGE.id("wools/cyan");
		public static final ResourceLocation WOOLS_PURPLE = IMod.FORGE.id("wools/purple");
		public static final ResourceLocation WOOLS_BLUE = IMod.FORGE.id("wools/blue");
		public static final ResourceLocation WOOLS_BROWN = IMod.FORGE.id("wools/brown");
		public static final ResourceLocation WOOLS_GREEN = IMod.FORGE.id("wools/green");
		public static final ResourceLocation WOOLS_RED = IMod.FORGE.id("wools/red");
		public static final ResourceLocation WOOLS_BLACK = IMod.FORGE.id("wools/black");
		// endregion
		// endregion
	}
}
