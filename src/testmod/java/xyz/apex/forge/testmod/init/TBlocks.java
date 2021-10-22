package xyz.apex.forge.testmod.init;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.testmod.TestMod;

public final class TBlocks
{
	private static final SimpleRegistrate REGISTRATE = TestMod.registrate();

	// @formatter:off
	public static final BlockEntry<Block> TEST_BLOCK = REGISTRATE
			.object("test_block")
			.block(Block::new)
			.initialProperties(Material.STONE, MaterialColor.STONE)
			.properties(properties -> properties.strength(1.5F).sound(SoundType.STONE))
			.defaultLang()
			.defaultBlockstate()
			.defaultLoot()
			.tag(TTags.Blocks.TEST_BLOCK)
			.item(BlockItem::new)
			.model((ctx, provider) -> provider.blockItem(ctx))
			.tag(TTags.Items.TEST_BLOCK)
			.build()
			.register();

	public static final BlockEntry<Block> COPPER_ORE = REGISTRATE
			.object("copper_ore")
			.block(Block::new)
			.initialProperties(Material.STONE)
			.properties(properties -> properties.requiresCorrectToolForDrops().strength(3F, 3F))
			.defaultLang()
			.defaultBlockstate()
			.defaultLoot()
			.tag(TTags.Blocks.COPPER_ORE)
			.recipe((ctx, provider) -> provider.smeltingAndBlasting(DataIngredient.tag(TTags.Items.ORES_COPPER), TItems.COPPER_INGOT, .7F))
			.item(BlockItem::new)
			.model((ctx, provider) -> provider.blockItem(ctx))
			.tag(TTags.Items.COPPER_ORE)
			.build()
			.register();

	public static final BlockEntry<Block> COPPER_BLOCK = REGISTRATE
			.object("copper_block")
			.block(Block::new)
			.initialProperties(Material.METAL, MaterialColor.METAL)
			.properties(properties -> properties.requiresCorrectToolForDrops().strength(5F, 6F).sound(SoundType.METAL))
			.defaultLang()
			.defaultBlockstate()
			.defaultLoot()
			.tag(TTags.Blocks.COPPER_BLOCK)
			.recipe((ctx, provider) -> provider.storage(DataIngredient.tag(TTags.Items.INGOTS_COPPER), TItems.COPPER_INGOT, DataIngredient.tag(TTags.Items.STORAGE_BLOCKS_COPPER), ctx))
			.item(BlockItem::new)
			.model((ctx, provider) -> provider.blockItem(ctx))
			.tag(TTags.Items.COPPER_BLOCK)
			.build()
			.register();
	// @formatter:on

	public static void register() { }
}
