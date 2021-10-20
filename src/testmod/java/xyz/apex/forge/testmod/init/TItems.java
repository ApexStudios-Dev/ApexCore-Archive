package xyz.apex.forge.testmod.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.testmod.TestMod;

public final class TItems
{
	private static final SimpleRegistrate REGISTRATE = TestMod.registrate();

	// @formatter:off
	public static final ItemEntry<Item> TEST_ITEM = REGISTRATE
			.object("test_item")
			.item(Item::new)
			.defaultModel()
			.defaultLang()
			.register();
	// @formatter:on

	public static void register() { }
}
