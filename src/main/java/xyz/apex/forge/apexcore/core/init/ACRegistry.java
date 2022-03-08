package xyz.apex.forge.apexcore.core.init;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.util.EventBusHelper;
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
			provider.add(ACItemGroupCategories.ENCHANTED_BOOKS.getCategoryNameKey(), "Enchanted Books");
			provider.add(ACItemGroupCategories.TOOLS.getCategoryNameKey(), "Tools");
			provider.add(ACItemGroupCategories.WEAPONS.getCategoryNameKey(), "Weapons");
			provider.add(ACItemGroupCategories.ARMOR.getCategoryNameKey(), "Armor");
			provider.add(ACItemGroupCategories.STAIRS.getCategoryNameKey(), "Stairs");
			provider.add(ACItemGroupCategories.SLABS.getCategoryNameKey(), "Slabs");
			provider.add(ACItemGroupCategories.ORES.getCategoryNameKey(), "Ores");
			provider.add(ACItemGroupCategories.STORAGE_BLOCKS.getCategoryNameKey(), "Storage Blocks");
			provider.add(ACItemGroupCategories.WOOLS.getCategoryNameKey(), "Wools");
			provider.add(ACItemGroupCategories.LOGS.getCategoryNameKey(), "Logs");
		});

		addDataGenerator(LANG_EXT_PROVIDER, provider -> {
			provider.add(EN_GB, ACItemGroupCategories.ENCHANTED_BOOKS.getCategoryNameKey(), "Enchanted Books");
			provider.add(EN_GB, ACItemGroupCategories.TOOLS.getCategoryNameKey(), "Tools");
			provider.add(EN_GB, ACItemGroupCategories.WEAPONS.getCategoryNameKey(), "Weapons");
			provider.add(EN_GB, ACItemGroupCategories.ARMOR.getCategoryNameKey(), "Armor");
			provider.add(EN_GB, ACItemGroupCategories.STAIRS.getCategoryNameKey(), "Stairs");
			provider.add(EN_GB, ACItemGroupCategories.SLABS.getCategoryNameKey(), "Slabs");
			provider.add(EN_GB, ACItemGroupCategories.ORES.getCategoryNameKey(), "Ores");
			provider.add(EN_GB, ACItemGroupCategories.STORAGE_BLOCKS.getCategoryNameKey(), "Storage Blocks");
			provider.add(EN_GB, ACItemGroupCategories.WOOLS.getCategoryNameKey(), "Wools");
			provider.add(EN_GB, ACItemGroupCategories.LOGS.getCategoryNameKey(), "Logs");
		});
	}

	public static void bootstrap()
	{
		if(!bootstrap)
		{
			ACTags.bootstrap();

			EventBusHelper.addEnqueuedListener(FMLCommonSetupEvent.class, event -> ACLootFunctionTypes.bootstrap());
			bootstrap = true;
		}
	}

	public static ACRegistry getRegistry()
	{
		return REGISTRY.get();
	}
}
