package xyz.apex.forge.apexcore.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

import xyz.apex.forge.apexcore.core.init.ACRegistry;

@Mod(ApexCore.ID)
public final class ApexCore
{
	public static final String ID = "apexcore";
	public static final Logger LOGGER = LogManager.getLogger();

	public ApexCore()
	{
		ACRegistry.bootstrap();
	}
}
