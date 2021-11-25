package xyz.apex.forge.testmod.init;

import xyz.apex.forge.utility.registrator.entry.PaintingEntry;

public final class TPaintings
{
	private static final TRegistry REGISTRY = TRegistry.getRegistry();

	public static final PaintingEntry TEST_PAINTING = REGISTRY.painting("test_painting", 64, 64);
	public static final PaintingEntry TEST_PAINTING_TRANSLUCENT = REGISTRY.painting("test_painting_translucent", 64, 64);

	static void bootstrap() { }
}
