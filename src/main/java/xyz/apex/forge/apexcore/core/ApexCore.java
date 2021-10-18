package xyz.apex.forge.apexcore.core;

import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.apex.forge.apexcore.lib.registrate.CustomRegistrate;
import xyz.apex.forge.apexcore.lib.util.ModEventBusHelper;

@Mod(ApexCore.ID)
public final class ApexCore
{
	public static final String ID = "apexcore";
	public static final Logger LOGGER = LogManager.getLogger();

	private static final NonNullLazyValue<ARegistrate> REGISTRATE = CustomRegistrate.create(ID, ARegistrate::new);

	public ApexCore()
	{
		REGISTRATE.get();

		ModEventBusHelper.addListener(this::onCommonSetup);
	}

	private void onCommonSetup(FMLCommonSetupEvent event)
	{
	}

	public static final class ARegistrate extends CustomRegistrate<ARegistrate>
	{
		private ARegistrate(String modId)
		{
			super(modId);
		}
	}
}
