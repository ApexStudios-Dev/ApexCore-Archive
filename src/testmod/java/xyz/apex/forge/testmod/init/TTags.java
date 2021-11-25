package xyz.apex.forge.testmod.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;

import xyz.apex.repack.com.tterrag.registrate.providers.ProviderType;

public final class TTags
{
	private static final TRegistry REGISTRY = TRegistry.getRegistry();

	static void bootstrap()
	{
		Items.bootstrap();
		Blocks.bootstrap();
	}

	public static final class Items
	{
		// region: Mod Wrapper
		public static final Tags.IOptionalNamedTag<Item> TEST_ITEM = REGISTRY.itemTagOptionalModded(Names.TEST_ITEM);
		public static final Tags.IOptionalNamedTag<Item> COPPER_INGOT = REGISTRY.itemTagOptionalModded(Names.COPPER_INGOT);
		public static final Tags.IOptionalNamedTag<Item> COPPER_SWORD = REGISTRY.itemTagOptionalModded(Names.COPPER_SWORD);
		public static final Tags.IOptionalNamedTag<Item> COPPER_SHOVEL = REGISTRY.itemTagOptionalModded(Names.COPPER_SHOVEL);
		public static final Tags.IOptionalNamedTag<Item> COPPER_PICKAXE = REGISTRY.itemTagOptionalModded(Names.COPPER_PICKAXE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_AXE = REGISTRY.itemTagOptionalModded(Names.COPPER_AXE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_HOE = REGISTRY.itemTagOptionalModded(Names.COPPER_HOE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_HELMET = REGISTRY.itemTagOptionalModded(Names.COPPER_HELMET);
		public static final Tags.IOptionalNamedTag<Item> COPPER_CHESTPLATE = REGISTRY.itemTagOptionalModded(Names.COPPER_CHESTPLATE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_LEGGINGS = REGISTRY.itemTagOptionalModded(Names.COPPER_LEGGINGS);
		public static final Tags.IOptionalNamedTag<Item> COPPER_BOOTS = REGISTRY.itemTagOptionalModded(Names.COPPER_BOOTS);
		public static final Tags.IOptionalNamedTag<Item> COPPER_HORSE_ARMOR = REGISTRY.itemTagOptionalModded(Names.COPPER_HORSE_ARMOR);

		public static final Tags.IOptionalNamedTag<Item> TEST_BLOCK = REGISTRY.itemTagOptionalModded(Names.TEST_BLOCK);
		public static final Tags.IOptionalNamedTag<Item> COPPER_ORE = REGISTRY.itemTagOptionalModded(Names.COPPER_ORE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_BLOCK = REGISTRY.itemTagOptionalModded(Names.COPPER_BLOCK);
		// endregion

		// region: Mod Tags
		public static final Tags.IOptionalNamedTag<Item> ORES_COPPER = REGISTRY.itemTagOptionalForge(Names.ORES_COPPER);
		public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_COPPER = REGISTRY.itemTagOptionalForge(Names.STORAGE_BLOCKS_COPPER);
		public static final Tags.IOptionalNamedTag<Item> INGOTS_COPPER = REGISTRY.itemTagOptionalForge(Names.INGOTS_COPPER);
		// endregion

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.ITEM_TAGS, provider -> {
				// region: Mod Tags
				// region: Ores
				provider.tag(ORES_COPPER).addTags(COPPER_ORE);
				provider.tag(Tags.Items.ORES).addTags(ORES_COPPER);
				// endregion

				// region: Storage Blocks
				provider.tag(STORAGE_BLOCKS_COPPER).addTags(COPPER_BLOCK);
				provider.tag(Tags.Items.STORAGE_BLOCKS).addTags(STORAGE_BLOCKS_COPPER);
				// endregion

				// region: Ingots
				provider.tag(INGOTS_COPPER).addTags(COPPER_INGOT);
				provider.tag(Tags.Items.INGOTS).addTags(INGOTS_COPPER);
				// endregion
				// endregion
			});
		}
	}

	public static final class Blocks
	{
		// region: Mod Wrapper
		public static final Tags.IOptionalNamedTag<Block> TEST_BLOCK = REGISTRY.blockTagOptionalModded(Names.TEST_BLOCK);
		public static final Tags.IOptionalNamedTag<Block> COPPER_ORE = REGISTRY.blockTagOptionalModded(Names.COPPER_ORE);
		public static final Tags.IOptionalNamedTag<Block> COPPER_BLOCK = REGISTRY.blockTagOptionalModded(Names.COPPER_BLOCK);
		// endregion

		// region: Mod Tags
		public static final Tags.IOptionalNamedTag<Block> ORES_COPPER = REGISTRY.blockTagOptionalForge(Names.ORES_COPPER);
		public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_COPPER = REGISTRY.blockTagOptionalForge(Names.STORAGE_BLOCKS_COPPER);
		// endregion

		private static void bootstrap()
		{
			REGISTRY.addDataGenerator(ProviderType.BLOCK_TAGS, provider -> {
				// region: Mod Tags
				// region: Ores
				provider.tag(ORES_COPPER).addTags(COPPER_ORE);
				provider.tag(Tags.Blocks.ORES).addTags(ORES_COPPER);
				// endregion

				// region: Storage Blocks
				provider.tag(STORAGE_BLOCKS_COPPER).addTags(COPPER_BLOCK);
				provider.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(STORAGE_BLOCKS_COPPER);
				// endregion
				// endregion
			});
		}
	}

	public static final class Names
	{
		// region: Mod Wrapper
		public static final String TEST_ITEM = "test_item";
		public static final String COPPER_INGOT = "copper_ingot";
		public static final String COPPER_SWORD = "copper_sword";
		public static final String COPPER_SHOVEL = "copper_shovel";
		public static final String COPPER_PICKAXE = "copper_pickaxe";
		public static final String COPPER_AXE = "copper_axe";
		public static final String COPPER_HOE = "copper_hoe";
		public static final String COPPER_HELMET = "copper_helmet";
		public static final String COPPER_CHESTPLATE = "copper_chestplate";
		public static final String COPPER_LEGGINGS = "copper_leggings";
		public static final String COPPER_BOOTS = "copper_boots";
		public static final String COPPER_HORSE_ARMOR = "copper_horse_armor";

		public static final String TEST_BLOCK = "test_block";
		public static final String COPPER_ORE = "copper_ore";
		public static final String COPPER_BLOCK = "copper_block";
		// endregion

		// region: Mod Tags
		public static final String ORES_COPPER = "ores/copper";
		public static final String STORAGE_BLOCKS_COPPER = "storage_blocks/copper";
		public static final String INGOTS_COPPER = "ingots/copper";
		// endregion
	}
}
