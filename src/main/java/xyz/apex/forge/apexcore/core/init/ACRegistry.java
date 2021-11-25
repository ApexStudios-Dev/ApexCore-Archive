package xyz.apex.forge.apexcore.core.init;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.util.ModEventBusHelper;
import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.Lazy;

public final class ACRegistry extends AbstractRegistrator<ACRegistry>
{
	private static final Lazy<ACRegistry> REGISTRY = create(ACRegistry::new);
	private static boolean bootstrap = false;

	private ACRegistry()
	{
		super(ApexCore.ID);

		skipErrors();
	}

	public static void bootstrap()
	{
		if(!bootstrap)
		{
			ACTags.bootstrap();

			ModEventBusHelper.addEnqueuedListener(FMLCommonSetupEvent.class, event -> ACLootFunctionTypes.bootstrap());
			bootstrap = true;
		}
	}

	public static ACRegistry getRegistry()
	{
		return REGISTRY.get();
	}
}
