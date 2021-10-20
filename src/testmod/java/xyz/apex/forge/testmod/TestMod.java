package xyz.apex.forge.testmod;

import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.apexcore.lib.util.IMod;
import xyz.apex.forge.testmod.init.TBlocks;
import xyz.apex.forge.testmod.init.TItems;
import xyz.apex.forge.testmod.init.TPaintings;

import javax.annotation.Nullable;

@Mod(TestMod.ID)
public final class TestMod implements IMod
{
	public static final String ID = "testmod";
	public static final Logger LOGGER = LogManager.getLogger();

	@Nullable private static IMod mod;
	private static final NonNullLazyValue<SimpleRegistrate> REGISTRATE = SimpleRegistrate.create(ID);

	public TestMod()
	{
		mod = this;

		setupRegistrate();

		TItems.register();
		TBlocks.register();
		TPaintings.register();
	}

	private void setupRegistrate()
	{
		// @formatter:off
		registrate()
				.itemGroup(ModItemGroup::new);
		;
		// @formatter:on
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

	public static SimpleRegistrate registrate()
	{
		return REGISTRATE.get();
	}

	private static class ModItemGroup extends ItemGroup
	{
		private ModItemGroup()
		{
			super(ID);
		}

		@Override
		public ItemStack makeIcon()
		{
			return TItems.TEST_ITEM.asStack();
		}
	}
}
