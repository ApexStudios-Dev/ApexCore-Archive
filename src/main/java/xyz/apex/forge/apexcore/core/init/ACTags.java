package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
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
		public static final Tag.Named<Item> WOOLS_WHITE = REGISTRY.forgeItemTag("wools/white");
		public static final Tag.Named<Item> WOOLS_ORANGE = REGISTRY.forgeItemTag("wools/orange");
		public static final Tag.Named<Item> WOOLS_MAGENTA = REGISTRY.forgeItemTag("wools/magenta");
		public static final Tag.Named<Item> WOOLS_LIGHT_BLUE = REGISTRY.forgeItemTag("wools/light_blue");
		public static final Tag.Named<Item> WOOLS_YELLOW = REGISTRY.forgeItemTag("wools/yellow");
		public static final Tag.Named<Item> WOOLS_LIME = REGISTRY.forgeItemTag("wools/lime");
		public static final Tag.Named<Item> WOOLS_PINK = REGISTRY.forgeItemTag("wools/pink");
		public static final Tag.Named<Item> WOOLS_GRAY = REGISTRY.forgeItemTag("wools/gray");
		public static final Tag.Named<Item> WOOLS_LIGHT_GRAY = REGISTRY.forgeItemTag("wools/light_gray");
		public static final Tag.Named<Item> WOOLS_CYAN = REGISTRY.forgeItemTag("wools/cyan");
		public static final Tag.Named<Item> WOOLS_PURPLE = REGISTRY.forgeItemTag("wools/purple");
		public static final Tag.Named<Item> WOOLS_BLUE = REGISTRY.forgeItemTag("wools/blue");
		public static final Tag.Named<Item> WOOLS_BROWN = REGISTRY.forgeItemTag("wools/brown");
		public static final Tag.Named<Item> WOOLS_GREEN = REGISTRY.forgeItemTag("wools/green");
		public static final Tag.Named<Item> WOOLS_RED = REGISTRY.forgeItemTag("wools/red");
		public static final Tag.Named<Item> WOOLS_BLACK = REGISTRY.forgeItemTag("wools/black");
		public static final Tag.Named<Item> WOOLS = REGISTRY.forgeItemTag("wools");
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
		public static final Tag.Named<Block> WOOLS_WHITE = REGISTRY.forgeBlockTag("wools/white");
		public static final Tag.Named<Block> WOOLS_ORANGE = REGISTRY.forgeBlockTag("wools/orange");
		public static final Tag.Named<Block> WOOLS_MAGENTA = REGISTRY.forgeBlockTag("wools/magenta");
		public static final Tag.Named<Block> WOOLS_LIGHT_BLUE = REGISTRY.forgeBlockTag("wools/light_blue");
		public static final Tag.Named<Block> WOOLS_YELLOW = REGISTRY.forgeBlockTag("wools/yellow");
		public static final Tag.Named<Block> WOOLS_LIME = REGISTRY.forgeBlockTag("wools/lime");
		public static final Tag.Named<Block> WOOLS_PINK = REGISTRY.forgeBlockTag("wools/pink");
		public static final Tag.Named<Block> WOOLS_GRAY = REGISTRY.forgeBlockTag("wools/gray");
		public static final Tag.Named<Block> WOOLS_LIGHT_GRAY = REGISTRY.forgeBlockTag("wools/light_gray");
		public static final Tag.Named<Block> WOOLS_CYAN = REGISTRY.forgeBlockTag("wools/cyan");
		public static final Tag.Named<Block> WOOLS_PURPLE = REGISTRY.forgeBlockTag("wools/purple");
		public static final Tag.Named<Block> WOOLS_BLUE = REGISTRY.forgeBlockTag("wools/blue");
		public static final Tag.Named<Block> WOOLS_BROWN = REGISTRY.forgeBlockTag("wools/brown");
		public static final Tag.Named<Block> WOOLS_GREEN = REGISTRY.forgeBlockTag("wools/green");
		public static final Tag.Named<Block> WOOLS_RED = REGISTRY.forgeBlockTag("wools/red");
		public static final Tag.Named<Block> WOOLS_BLACK = REGISTRY.forgeBlockTag("wools/black");
		public static final Tag.Named<Block> WOOLS = REGISTRY.forgeBlockTag("wools");
		// endregion

		public static final Tag.Named<Block> CROPS = REGISTRY.forgeBlockTag("crops");
		public static final Tag.Named<Block> MUSHROOMS = REGISTRY.forgeBlockTag("mushrooms");

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
		public static final Tag.Named<EntityType<?>> COWS = REGISTRY.forgeEntityTypeTag("cows");
		public static final Tag.Named<EntityType<?>> CHICKENS = REGISTRY.forgeEntityTypeTag("chickens");
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
