package xyz.apex.forge.apexcore.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.lib.net.NetworkManager;
import xyz.apex.forge.commonality.Mods;
import xyz.apex.forge.commonality.trust.TrustManager;

@Mod(Mods.APEX_CORE)
public final class ApexCore
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final NetworkManager NETWORK = new NetworkManager(Mods.APEX_CORE, "main", "1");

	public ApexCore()
	{
		TrustManager.throwIfUntrusted(Mods.APEX_CORE);
		ACRegistry.bootstrap();
	}
}