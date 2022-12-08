package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.AbstractRegistrate;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xyz.apex.forge.commonality.Mods;
import xyz.apex.forge.commonality.tags.BlockTags;

import static com.tterrag.registrate.providers.ProviderType.BLOCK_TAGS;

public final class ACRegistry extends AbstractRegistrate<ACRegistry>
{
	public static final ACRegistry INSTANCE = new ACRegistry();
	private static boolean bootstrap = false;

	public static final EnchantmentCategory ENCHANTMENT_CATEGORY_NONE = EnchantmentCategory.create("%s:none".formatted(Mods.APEX_CORE), item -> false);
	public static final TagKey<Block> TAG_VISUALIZER = BlockTags.tag(Mods.APEX_CORE, "visualizer");

	private ACRegistry()
	{
		super(Mods.APEX_CORE);

		// TODO: Revamp for new creative mode tab system
		/*addDataGenerator(LANG, provider -> {
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
		});*/

		addDataGenerator(BLOCK_TAGS, provider -> provider.tag(TAG_VISUALIZER));
	}

	public static void bootstrap()
	{
		if(!bootstrap)
		{
			INSTANCE.registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
			ACEntities.bootstrap();
			bootstrap = true;
		}
	}
}
