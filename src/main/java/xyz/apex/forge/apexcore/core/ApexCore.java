package xyz.apex.forge.apexcore.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.lib.net.NetworkManager;
import xyz.apex.forge.apexcore.lib.util.EventBusHelper;
import xyz.apex.forge.apexcore.revamp.net.packet.SyncContainerPacket;
import xyz.apex.forge.commonality.Mods;

@Mod(Mods.APEX_CORE)
public final class ApexCore
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final NetworkManager NETWORK = new NetworkManager(Mods.APEX_CORE, "main", "1");

	public ApexCore()
	{
		ACRegistry.bootstrap();
		EventBusHelper.addEnqueuedListener(FMLCommonSetupEvent.class, event -> {
			NETWORK.registerPackets(SyncContainerPacket.class);
		});
	}
}