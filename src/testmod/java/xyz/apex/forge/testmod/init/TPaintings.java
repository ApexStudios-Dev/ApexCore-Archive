package xyz.apex.forge.testmod.init;

import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.apex.forge.apexcore.lib.util.ModEventBusHelper;
import xyz.apex.forge.testmod.TestMod;

public final class TPaintings
{
	public static final DeferredRegister<PaintingType> PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, TestMod.ID);

	// @formatter:off
	public static final RegistryObject<PaintingType> TEST_PAINTING = PAINTING_TYPES.register("test_painting", () -> new PaintingType(64, 64));
	public static final RegistryObject<PaintingType> TEST_PAINTING_TRANSLUCENT = PAINTING_TYPES.register("test_painting_translucent", () -> new PaintingType(64, 64));
	// @formatter:on

	public static void register()
	{
		PAINTING_TYPES.register(ModEventBusHelper.getModEventBus());
	}
}
