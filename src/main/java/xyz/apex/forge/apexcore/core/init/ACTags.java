package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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
		public static final TagKey<Item> WOOLS_WHITE = REGISTRY.itemTagForge("wools/white");
		public static final TagKey<Item> WOOLS_ORANGE = REGISTRY.itemTagForge("wools/orange");
		public static final TagKey<Item> WOOLS_MAGENTA = REGISTRY.itemTagForge("wools/magenta");
		public static final TagKey<Item> WOOLS_LIGHT_BLUE = REGISTRY.itemTagForge("wools/light_blue");
		public static final TagKey<Item> WOOLS_YELLOW = REGISTRY.itemTagForge("wools/yellow");
		public static final TagKey<Item> WOOLS_LIME = REGISTRY.itemTagForge("wools/lime");
		public static final TagKey<Item> WOOLS_PINK = REGISTRY.itemTagForge("wools/pink");
		public static final TagKey<Item> WOOLS_GRAY = REGISTRY.itemTagForge("wools/gray");
		public static final TagKey<Item> WOOLS_LIGHT_GRAY = REGISTRY.itemTagForge("wools/light_gray");
		public static final TagKey<Item> WOOLS_CYAN = REGISTRY.itemTagForge("wools/cyan");
		public static final TagKey<Item> WOOLS_PURPLE = REGISTRY.itemTagForge("wools/purple");
		public static final TagKey<Item> WOOLS_BLUE = REGISTRY.itemTagForge("wools/blue");
		public static final TagKey<Item> WOOLS_BROWN = REGISTRY.itemTagForge("wools/brown");
		public static final TagKey<Item> WOOLS_GREEN = REGISTRY.itemTagForge("wools/green");
		public static final TagKey<Item> WOOLS_RED = REGISTRY.itemTagForge("wools/red");
		public static final TagKey<Item> WOOLS_BLACK = REGISTRY.itemTagForge("wools/black");
		public static final TagKey<Item> WOOLS = REGISTRY.itemTagForge("wools");
		// endregion

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.ITEM_TAGS, provider -> {
				// region: Wools
				provider.tag(WOOLS_WHITE).add(net.minecraft.world.item.Items.WHITE_WOOL);
				provider.tag(WOOLS_ORANGE).add(net.minecraft.world.item.Items.ORANGE_WOOL);
				provider.tag(WOOLS_MAGENTA).add(net.minecraft.world.item.Items.MAGENTA_WOOL);
				provider.tag(WOOLS_LIGHT_BLUE).add(net.minecraft.world.item.Items.LIGHT_BLUE_WOOL);
				provider.tag(WOOLS_YELLOW).add(net.minecraft.world.item.Items.YELLOW_WOOL);
				provider.tag(WOOLS_LIME).add(net.minecraft.world.item.Items.LIME_WOOL);
				provider.tag(WOOLS_PINK).add(net.minecraft.world.item.Items.PINK_WOOL);
				provider.tag(WOOLS_GRAY).add(net.minecraft.world.item.Items.GRAY_WOOL);
				provider.tag(WOOLS_LIGHT_GRAY).add(net.minecraft.world.item.Items.LIGHT_GRAY_WOOL);
				provider.tag(WOOLS_CYAN).add(net.minecraft.world.item.Items.CYAN_WOOL);
				provider.tag(WOOLS_PURPLE).add(net.minecraft.world.item.Items.PURPLE_WOOL);
				provider.tag(WOOLS_BLUE).add(net.minecraft.world.item.Items.BLUE_WOOL);
				provider.tag(WOOLS_BROWN).add(net.minecraft.world.item.Items.BROWN_WOOL);
				provider.tag(WOOLS_GREEN).add(net.minecraft.world.item.Items.GREEN_WOOL);
				provider.tag(WOOLS_RED).add(net.minecraft.world.item.Items.RED_WOOL);
				provider.tag(WOOLS_BLACK).add(net.minecraft.world.item.Items.BLACK_WOOL);

				provider.tag(WOOLS).addTags(WOOLS_WHITE, WOOLS_ORANGE, WOOLS_MAGENTA, WOOLS_LIGHT_BLUE, WOOLS_YELLOW, WOOLS_LIME, WOOLS_PINK, WOOLS_GRAY, WOOLS_LIGHT_GRAY, WOOLS_CYAN, WOOLS_PURPLE, WOOLS_BLUE, WOOLS_BROWN, WOOLS_GREEN, WOOLS_RED, WOOLS_BLACK);
				provider.tag(ItemTags.WOOL).addTags(WOOLS, WOOLS_WHITE, WOOLS_ORANGE, WOOLS_MAGENTA, WOOLS_LIGHT_BLUE, WOOLS_YELLOW, WOOLS_LIME, WOOLS_PINK, WOOLS_GRAY, WOOLS_LIGHT_GRAY, WOOLS_CYAN, WOOLS_PURPLE, WOOLS_BLUE, WOOLS_BROWN, WOOLS_GREEN, WOOLS_RED, WOOLS_BLACK);
				// endregion
			});
		}
	}

	public static final class Blocks
	{
		// region: Wools
		public static final TagKey<Block> WOOLS_WHITE = REGISTRY.blockTagForge("wools/white");
		public static final TagKey<Block> WOOLS_ORANGE = REGISTRY.blockTagForge("wools/orange");
		public static final TagKey<Block> WOOLS_MAGENTA = REGISTRY.blockTagForge("wools/magenta");
		public static final TagKey<Block> WOOLS_LIGHT_BLUE = REGISTRY.blockTagForge("wools/light_blue");
		public static final TagKey<Block> WOOLS_YELLOW = REGISTRY.blockTagForge("wools/yellow");
		public static final TagKey<Block> WOOLS_LIME = REGISTRY.blockTagForge("wools/lime");
		public static final TagKey<Block> WOOLS_PINK = REGISTRY.blockTagForge("wools/pink");
		public static final TagKey<Block> WOOLS_GRAY = REGISTRY.blockTagForge("wools/gray");
		public static final TagKey<Block> WOOLS_LIGHT_GRAY = REGISTRY.blockTagForge("wools/light_gray");
		public static final TagKey<Block> WOOLS_CYAN = REGISTRY.blockTagForge("wools/cyan");
		public static final TagKey<Block> WOOLS_PURPLE = REGISTRY.blockTagForge("wools/purple");
		public static final TagKey<Block> WOOLS_BLUE = REGISTRY.blockTagForge("wools/blue");
		public static final TagKey<Block> WOOLS_BROWN = REGISTRY.blockTagForge("wools/brown");
		public static final TagKey<Block> WOOLS_GREEN = REGISTRY.blockTagForge("wools/green");
		public static final TagKey<Block> WOOLS_RED = REGISTRY.blockTagForge("wools/red");
		public static final TagKey<Block> WOOLS_BLACK = REGISTRY.blockTagForge("wools/black");
		public static final TagKey<Block> WOOLS = REGISTRY.blockTagForge("wools");
		// endregion

		public static final TagKey<Block> CROPS = REGISTRY.blockTagForge("crops");
		public static final TagKey<Block> MUSHROOMS = REGISTRY.blockTagForge("mushrooms");

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.BLOCK_TAGS, provider -> {
				// region: Crops
				provider.tag(CROPS);
				provider.tag(BlockTags.CROPS).addTags(CROPS);
				// endregion

				// region: Mushrooms
				provider.tag(MUSHROOMS).add(net.minecraft.world.level.block.Blocks.BROWN_MUSHROOM, net.minecraft.world.level.block.Blocks.RED_MUSHROOM);
				// endregion

				// region: Wools
				provider.tag(WOOLS_WHITE).add(net.minecraft.world.level.block.Blocks.WHITE_WOOL);
				provider.tag(WOOLS_ORANGE).add(net.minecraft.world.level.block.Blocks.ORANGE_WOOL);
				provider.tag(WOOLS_MAGENTA).add(net.minecraft.world.level.block.Blocks.MAGENTA_WOOL);
				provider.tag(WOOLS_LIGHT_BLUE).add(net.minecraft.world.level.block.Blocks.LIGHT_BLUE_WOOL);
				provider.tag(WOOLS_YELLOW).add(net.minecraft.world.level.block.Blocks.YELLOW_WOOL);
				provider.tag(WOOLS_LIME).add(net.minecraft.world.level.block.Blocks.LIME_WOOL);
				provider.tag(WOOLS_PINK).add(net.minecraft.world.level.block.Blocks.PINK_WOOL);
				provider.tag(WOOLS_GRAY).add(net.minecraft.world.level.block.Blocks.GRAY_WOOL);
				provider.tag(WOOLS_LIGHT_GRAY).add(net.minecraft.world.level.block.Blocks.LIGHT_GRAY_WOOL);
				provider.tag(WOOLS_CYAN).add(net.minecraft.world.level.block.Blocks.CYAN_WOOL);
				provider.tag(WOOLS_PURPLE).add(net.minecraft.world.level.block.Blocks.PURPLE_WOOL);
				provider.tag(WOOLS_BLUE).add(net.minecraft.world.level.block.Blocks.BLUE_WOOL);
				provider.tag(WOOLS_BROWN).add(net.minecraft.world.level.block.Blocks.BROWN_WOOL);
				provider.tag(WOOLS_GREEN).add(net.minecraft.world.level.block.Blocks.GREEN_WOOL);
				provider.tag(WOOLS_RED).add(net.minecraft.world.level.block.Blocks.RED_WOOL);
				provider.tag(WOOLS_BLACK).add(net.minecraft.world.level.block.Blocks.BLACK_WOOL);

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
		public static final TagKey<EntityType<?>> COWS = REGISTRY.entityTypeTagForge("cows");
		public static final TagKey<EntityType<?>> CHICKENS = REGISTRY.entityTypeTagForge("chickens");
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
