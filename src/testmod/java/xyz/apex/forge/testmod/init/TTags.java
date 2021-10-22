package xyz.apex.forge.testmod.init;

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
import xyz.apex.forge.testmod.TestMod;

public final class TTags
{
	public static final class Items
	{
		// region: Mod Wrapper
		public static final Tags.IOptionalNamedTag<Item> TEST_ITEM = tag(Names.TEST_ITEM);
		public static final Tags.IOptionalNamedTag<Item> COPPER_INGOT = tag(Names.COPPER_INGOT);
		public static final Tags.IOptionalNamedTag<Item> COPPER_SWORD = tag(Names.COPPER_SWORD);
		public static final Tags.IOptionalNamedTag<Item> COPPER_SHOVEL = tag(Names.COPPER_SHOVEL);
		public static final Tags.IOptionalNamedTag<Item> COPPER_PICKAXE = tag(Names.COPPER_PICKAXE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_AXE = tag(Names.COPPER_AXE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_HOE = tag(Names.COPPER_HOE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_HELMET = tag(Names.COPPER_HELMET);
		public static final Tags.IOptionalNamedTag<Item> COPPER_CHESTPLATE = tag(Names.COPPER_CHESTPLATE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_LEGGINGS = tag(Names.COPPER_LEGGINGS);
		public static final Tags.IOptionalNamedTag<Item> COPPER_BOOTS = tag(Names.COPPER_BOOTS);
		public static final Tags.IOptionalNamedTag<Item> COPPER_HORSE_ARMOR = tag(Names.COPPER_HORSE_ARMOR);

		public static final Tags.IOptionalNamedTag<Item> TEST_BLOCK = tag(Names.TEST_BLOCK);
		public static final Tags.IOptionalNamedTag<Item> COPPER_ORE = tag(Names.COPPER_ORE);
		public static final Tags.IOptionalNamedTag<Item> COPPER_BLOCK = tag(Names.COPPER_BLOCK);
		// endregion

		// region: Mod Tags
		public static final Tags.IOptionalNamedTag<Item> ORES_COPPER = tag(Names.ORES_COPPER);
		public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_COPPER = tag(Names.STORAGE_BLOCKS_COPPER);
		public static final Tags.IOptionalNamedTag<Item> INGOTS_COPPER = tag(Names.INGOTS_COPPER);
		// endregion

		public static void generate(RegistrateItemTagsProvider provider)
		{
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
		}

		private static Tags.IOptionalNamedTag<Item> tag(ResourceLocation tagName)
		{
			return ItemTags.createOptional(tagName);
		}
	}

	public static final class Blocks
	{
		// region: Mod Wrapper
		public static final Tags.IOptionalNamedTag<Block> TEST_BLOCK = tag(Names.TEST_BLOCK);
		public static final Tags.IOptionalNamedTag<Block> COPPER_ORE = tag(Names.COPPER_ORE);
		public static final Tags.IOptionalNamedTag<Block> COPPER_BLOCK = tag(Names.COPPER_BLOCK);
		// endregion

		// region: Mod Tags
		public static final Tags.IOptionalNamedTag<Block> ORES_COPPER = tag(Names.ORES_COPPER);
		public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_COPPER = tag(Names.STORAGE_BLOCKS_COPPER);
		// endregion

		public static void generate(RegistrateTagsProvider<Block> provider)
		{
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
		}

		private static Tags.IOptionalNamedTag<Block> tag(ResourceLocation tagName)
		{
			return BlockTags.createOptional(tagName);
		}
	}

	public static final class Fluids
	{
		// region: Mod Wrapper
		// endregion

		// region: Mod Tags
		// endregion

		public static void generate(RegistrateTagsProvider<Fluid> provider)
		{
			// region: Mod Wrapper
			// endregion

			// region: Mod Tags
			// endregion
		}

		private static Tags.IOptionalNamedTag<Fluid> tag(ResourceLocation tagName)
		{
			return FluidTags.createOptional(tagName);
		}
	}

	public static final class EntityTypes
	{
		// region: Mod Wrapper
		// endregion

		// region: Mod Tags
		// endregion

		public static void generate(RegistrateTagsProvider<EntityType<?>> provider)
		{
			// region: Mod Wrapper
			// endregion

			// region: Mod Tags
			// endregion
		}

		private static Tags.IOptionalNamedTag<EntityType<?>> tag(ResourceLocation tagName)
		{
			return EntityTypeTags.createOptional(tagName);
		}
	}

	public static final class Names
	{
		// region: Mod Wrapper
		public static final ResourceLocation TEST_ITEM = TestMod.getMod().id("test_item");
		public static final ResourceLocation COPPER_INGOT = TestMod.getMod().id("copper_ingot");
		public static final ResourceLocation COPPER_SWORD = TestMod.getMod().id("copper_sword");
		public static final ResourceLocation COPPER_SHOVEL = TestMod.getMod().id("copper_shovel");
		public static final ResourceLocation COPPER_PICKAXE = TestMod.getMod().id("copper_pickaxe");
		public static final ResourceLocation COPPER_AXE = TestMod.getMod().id("copper_axe");
		public static final ResourceLocation COPPER_HOE = TestMod.getMod().id("copper_hoe");
		public static final ResourceLocation COPPER_HELMET = TestMod.getMod().id("copper_helmet");
		public static final ResourceLocation COPPER_CHESTPLATE = TestMod.getMod().id("copper_chestplate");
		public static final ResourceLocation COPPER_LEGGINGS = TestMod.getMod().id("copper_leggings");
		public static final ResourceLocation COPPER_BOOTS = TestMod.getMod().id("copper_boots");
		public static final ResourceLocation COPPER_HORSE_ARMOR = TestMod.getMod().id("copper_horse_armor");

		public static final ResourceLocation TEST_BLOCK = TestMod.getMod().id("test_block");
		public static final ResourceLocation COPPER_ORE = TestMod.getMod().id("copper_ore");
		public static final ResourceLocation COPPER_BLOCK = TestMod.getMod().id("copper_block");
		// endregion

		// region: Mod Tags
		public static final ResourceLocation ORES_COPPER = IMod.FORGE.id("ores/copper");
		public static final ResourceLocation STORAGE_BLOCKS_COPPER = IMod.FORGE.id("storage_blocks/copper");
		public static final ResourceLocation INGOTS_COPPER = IMod.FORGE.id("ingots/copper");
		// endregion
	}
}
