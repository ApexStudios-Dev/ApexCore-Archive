package xyz.apex.forge.testmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

import xyz.apex.forge.testmod.init.TRegistry;

@Mod(TestMod.ID)
public final class TestMod
{
	public static final String ID = "testmod";
	public static final Logger LOGGER = LogManager.getLogger();

	public TestMod()
	{
		TRegistry.bootstrap();
	}
}
