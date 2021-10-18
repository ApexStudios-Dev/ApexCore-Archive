package xyz.apex.forge.apexcore.lib.registrate;

import com.tterrag.registrate.util.NonNullLazyValue;

public class SimpleRegistrate extends CustomRegistrate<SimpleRegistrate>
{
	private SimpleRegistrate(String modId)
	{
		super(modId);
	}

	public static NonNullLazyValue<SimpleRegistrate> create(String modId)
	{
		return create(modId, SimpleRegistrate::new);
	}
}
