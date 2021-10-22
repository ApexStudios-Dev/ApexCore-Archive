package xyz.apex.forge.apexcore.lib.registrate;

import com.tterrag.registrate.util.NonNullLazyValue;
import xyz.apex.forge.apexcore.lib.util.IMod;

public class SimpleRegistrate extends CustomRegistrate<SimpleRegistrate>
{
	private SimpleRegistrate(String modId)
	{
		super(modId);
	}

	private SimpleRegistrate(IMod mod)
	{
		super(mod);
	}

	public static NonNullLazyValue<SimpleRegistrate> create(String modId)
	{
		return create(modId, SimpleRegistrate::new);
	}

	public static NonNullLazyValue<SimpleRegistrate> create(IMod mod)
	{
		return create(mod, SimpleRegistrate::new);
	}
}
