package xyz.apex.forge.apexcore.core;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.apex.forge.apexcore.core.init.ALootFunctionTypes;
import xyz.apex.forge.apexcore.core.init.ATags;
import xyz.apex.forge.apexcore.lib.constants.Mods;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.apexcore.lib.util.ModEventBusHelper;

@Mod(ApexCore.ID)
public final class ApexCore
{
	public static final String ID = "apexcore";
	public static final Logger LOGGER = LogManager.getLogger();

	private static final NonNullLazyValue<SimpleRegistrate> REGISTRATE = SimpleRegistrate.create(ID);

	public ApexCore()
	{
		setupRegistrate();
		ModEventBusHelper.addEnqueuedListener(this::finalizeRegistration);
	}

	private void setupRegistrate()
	{
		// @formatter:off
		registrate()
				.addDataGenerator(ProviderType.ITEM_TAGS, ATags.Items::generate)
				.addDataGenerator(ProviderType.BLOCK_TAGS, ATags.Blocks::generate)
				.addDataGenerator(ProviderType.FLUID_TAGS, ATags.Fluids::generate)
				.addDataGenerator(ProviderType.ENTITY_TAGS, ATags.EntityTypes::generate)
		;
		// @formatter:on
	}

	private void finalizeRegistration(FMLCommonSetupEvent event)
	{
		ALootFunctionTypes.register();
	}

	public static SimpleRegistrate registrate()
	{
		return REGISTRATE.get();
	}

	public static Mods mod()
	{
		return Mods.APEX_CORE;
	}

	public static ResourceLocation id(String name)
	{
		return mod().id(name);
	}
}
