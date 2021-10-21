package xyz.apex.forge.testmod.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.item.PaintingType;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.testmod.TestMod;

public final class TPaintings
{
	private static final SimpleRegistrate REGISTRATE = TestMod.registrate();

	// @formatter:off
	public static final RegistryEntry<PaintingType> TEST_PAINTING = REGISTRATE
			.object("test_painting")
			.painting(64, 64)
			.register();

	public static final RegistryEntry<PaintingType> TEST_PAINTING_TRANSLUCENT = REGISTRATE
			.object("test_painting_translucent")
			.painting(64, 64)
			.register();
	// @formatter:on

	public static void register() { }
}
