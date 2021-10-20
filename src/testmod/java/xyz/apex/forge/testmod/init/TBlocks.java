package xyz.apex.forge.testmod.init;

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
			.item(BlockItem::new)
			.model((ctx, provider) -> provider.blockItem(ctx))
			.build()
			.register();
	// @formatter:on

	public static void register() { }
}
