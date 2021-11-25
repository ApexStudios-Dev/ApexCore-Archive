package xyz.apex.forge.testmod.init;

import xyz.apex.forge.testmod.TestMod;
import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.helper.RegistratorItemGroup;
import xyz.apex.java.utility.Lazy;

public final class TRegistry extends AbstractRegistrator<TRegistry>
{
	private static final Lazy<TRegistry> REGISTRY = create(TRegistry::new);
	private static boolean bootstrap = false;

	private TRegistry()
	{
		super(TestMod.ID);

		skipErrors().itemGroup(() -> RegistratorItemGroup.create(this), "Test Mod");
	}

	public static void bootstrap()
	{
		if(!bootstrap)
		{
			TTags.bootstrap();
			TElements.bootstrap();

			TBlocks.bootstrap();
			TItems.bootstrap();

			TPaintings.bootstrap();

			bootstrap = true;
		}
	}

	public static TRegistry getRegistry()
	{
		return REGISTRY.get();
	}
}
