package xyz.apex.forge.apexcore.core.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.BlockTags;
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
		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.ITEM_TAGS, provider -> {
			});
		}
	}

	public static final class Blocks
	{
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
