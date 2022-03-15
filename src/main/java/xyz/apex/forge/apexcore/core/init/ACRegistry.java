package xyz.apex.forge.apexcore.core.init;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.util.ModEventBusHelper;
import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.Lazy;

import static xyz.apex.forge.utility.registrator.provider.RegistrateLangExtProvider.EN_GB;
import static com.tterrag.registrate.providers.ProviderType.LANG;

public final class ACRegistry extends AbstractRegistrator<ACRegistry>
{
	private static final Lazy<ACRegistry> REGISTRY = create(ACRegistry::new);
	private static boolean bootstrap = false;

	private ACRegistry()
	{
		super(ApexCore.ID);

		skipErrors();

		addDataGenerator(LANG, provider -> {
			provider.add(ACCreativeModeTabCategories.ENCHANTED_BOOKS.getCategoryNameKey(), "Enchanted Books");
			provider.add(ACCreativeModeTabCategories.TOOLS.getCategoryNameKey(), "Tools");
			provider.add(ACCreativeModeTabCategories.WEAPONS.getCategoryNameKey(), "Weapons");
			provider.add(ACCreativeModeTabCategories.ARMOR.getCategoryNameKey(), "Armor");
			provider.add(ACCreativeModeTabCategories.STAIRS.getCategoryNameKey(), "Stairs");
			provider.add(ACCreativeModeTabCategories.SLABS.getCategoryNameKey(), "Slabs");
			provider.add(ACCreativeModeTabCategories.ORES.getCategoryNameKey(), "Ores");
			provider.add(ACCreativeModeTabCategories.STORAGE_BLOCKS.getCategoryNameKey(), "Storage Blocks");
			provider.add(ACCreativeModeTabCategories.WOOLS.getCategoryNameKey(), "Wools");
			provider.add(ACCreativeModeTabCategories.LOGS.getCategoryNameKey(), "Logs");
		});

		addDataGenerator(LANG_EXT_PROVIDER, provider -> {
			provider.add(EN_GB, ACCreativeModeTabCategories.ENCHANTED_BOOKS.getCategoryNameKey(), "Enchanted Books");
			provider.add(EN_GB, ACCreativeModeTabCategories.TOOLS.getCategoryNameKey(), "Tools");
			provider.add(EN_GB, ACCreativeModeTabCategories.WEAPONS.getCategoryNameKey(), "Weapons");
			provider.add(EN_GB, ACCreativeModeTabCategories.ARMOR.getCategoryNameKey(), "Armor");
			provider.add(EN_GB, ACCreativeModeTabCategories.STAIRS.getCategoryNameKey(), "Stairs");
			provider.add(EN_GB, ACCreativeModeTabCategories.SLABS.getCategoryNameKey(), "Slabs");
			provider.add(EN_GB, ACCreativeModeTabCategories.ORES.getCategoryNameKey(), "Ores");
			provider.add(EN_GB, ACCreativeModeTabCategories.STORAGE_BLOCKS.getCategoryNameKey(), "Storage Blocks");
			provider.add(EN_GB, ACCreativeModeTabCategories.WOOLS.getCategoryNameKey(), "Wools");
			provider.add(EN_GB, ACCreativeModeTabCategories.LOGS.getCategoryNameKey(), "Logs");
		});
	}

	public static void bootstrap()
	{
		if(!bootstrap)
		{
			ACTags.bootstrap();

			ModEventBusHelper.addEnqueuedListener(FMLCommonSetupEvent.class, event -> ACLootFunctionTypes.bootstrap());
			bootstrap = true;
		}
	}

	public static ACRegistry getRegistry()
	{
		return REGISTRY.get();
	}
}
