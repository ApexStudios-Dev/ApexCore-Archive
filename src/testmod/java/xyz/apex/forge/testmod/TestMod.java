package xyz.apex.forge.testmod;

import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.Validate;
import xyz.apex.forge.apexcore.lib.util.IMod;

import javax.annotation.Nullable;

@Mod(TestMod.ID)
public class TestMod implements IMod
{
	public static final String ID = "testmod";
	@Nullable private static IMod mod;

	public TestMod()
	{
		mod = this;
	}

	@Override
	public String getModId()
	{
		return ID;
	}

	public static IMod getMod()
	{
		return Validate.notNull(mod);
	}
}
