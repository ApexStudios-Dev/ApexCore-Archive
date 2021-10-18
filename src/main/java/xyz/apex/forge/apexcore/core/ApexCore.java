package xyz.apex.forge.apexcore.core;

import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.apex.forge.apexcore.core.init.ALootFunctionTypes;
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
		REGISTRATE.get();

		ModEventBusHelper.addEnqueuedListener(this::finalizeRegistration);
	}

	private void finalizeRegistration(FMLCommonSetupEvent event)
	{
		ALootFunctionTypes.register();
	}

	public static SimpleRegistrate registrate()
	{
		return REGISTRATE.get();
	}
}
