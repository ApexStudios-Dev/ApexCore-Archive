package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.AbstractRegistrate;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xyz.apex.forge.apexcore.lib.util.EventBusHelper;
import xyz.apex.forge.commonality.Mods;

import static com.tterrag.registrate.providers.ProviderType.LANG;

public final class ACRegistry extends AbstractRegistrate<ACRegistry>
{
	public static final ACRegistry INSTANCE = new ACRegistry();
	private static boolean bootstrap = false;

	private ACRegistry()
	{
		super(Mods.APEX_CORE);

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
	}

	public static void bootstrap()
	{
		if(!bootstrap)
		{
			INSTANCE.registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
			EventBusHelper.addEnqueuedListener(FMLCommonSetupEvent.class, event -> ACLootFunctionTypes.bootstrap());
			PlayerPlushie.bootstrap();
			ACEntities.bootstrap();
			bootstrap = true;
		}
	}
}
