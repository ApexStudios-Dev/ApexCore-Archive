package xyz.apex.forge.testmod.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;

import xyz.apex.forge.apexcore.lib.util.RegistratorHelper;
import xyz.apex.forge.utility.registrator.entry.BlockEntry;
import xyz.apex.repack.com.tterrag.registrate.util.DataIngredient;

public final class TBlocks
{
	private static final TRegistry REGISTRY = TRegistry.getRegistry();

	public static final BlockEntry<Block> TEST_BLOCK = REGISTRY
			.block("test_block")
				.initialProperties(Material.STONE, MaterialColor.STONE)
				.properties(properties -> properties.strength(1.5F).sound(SoundType.STONE))
				.tag(TTags.Blocks.TEST_BLOCK)

				.item(BlockItem::new)
					.model(RegistratorHelper::blockItemModel)
					.tag(TTags.Items.TEST_BLOCK)
				.build()
			.register();

	public static final BlockEntry<Block> COPPER_ORE = REGISTRY
			.block("copper_ore")
				.initialProperties(Material.STONE)
				.properties(properties -> properties.requiresCorrectToolForDrops().strength(3F, 3F))
				.tag(TTags.Blocks.COPPER_ORE)
				.recipe((ctx, provider) -> provider.smeltingAndBlasting(DataIngredient.tag(TTags.Items.ORES_COPPER), TItems.COPPER_INGOT, .7F))

				.item(BlockItem::new)
					.model(RegistratorHelper::blockItemModel)
					.tag(TTags.Items.COPPER_ORE)
				.build()
			.register();

	public static final BlockEntry<Block> COPPER_BLOCK = REGISTRY
			.block("copper_block")
				.initialProperties(Material.METAL, MaterialColor.METAL)
				.properties(properties -> properties.requiresCorrectToolForDrops().strength(5F, 6F).sound(SoundType.METAL))
				.tag(TTags.Blocks.COPPER_BLOCK)
				.recipe((ctx, provider) -> provider.storage(DataIngredient.tag(TTags.Items.INGOTS_COPPER), TItems.COPPER_INGOT, DataIngredient.tag(TTags.Items.STORAGE_BLOCKS_COPPER), ctx))

				.item(BlockItem::new)
					.model(RegistratorHelper::blockItemModel)
					.tag(TTags.Items.COPPER_BLOCK)
				.build()
			.register();

	static void bootstrap() { }
}
