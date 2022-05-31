package xyz.apex.forge.utility.registrator;

import xyz.apex.java.utility.Lazy;

public final class Registrator extends AbstractRegistrator<Registrator>
{
	private Registrator(String modId)
	{
		super(modId);
	}

	public static Lazy<Registrator> create(String modId)
	{
		return create(modId, Registrator::new);
	}
}
