package xyz.apex.forge.testmod.init;

import net.minecraft.item.*;

import xyz.apex.forge.apexcore.lib.util.RegistratorHelper;
import xyz.apex.forge.utility.registrator.entry.ItemEntry;

public final class TItems
{
	private static final TRegistry REGISTRY = TRegistry.getRegistry();

	public static final ItemEntry<Item> TEST_ITEM = REGISTRY
			.item("test_item")
				.tag(TTags.Items.TEST_ITEM)
			.register();

	public static final ItemEntry<Item> COPPER_INGOT = REGISTRY
			.item("copper_ingot")
				.tag(TTags.Items.COPPER_INGOT)
			.register();

	public static final ItemEntry<SwordItem> COPPER_SWORD = REGISTRY
			.swordItem("copper_sword", TElements.COPPER_ITEM_TIER, 3, -2.4F)
				.tag(TTags.Items.COPPER_SWORD)
				.recipe((ctx, provider) -> RegistratorHelper.swordRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ShovelItem> COPPER_SHOVEL = REGISTRY
			.shovelItem("copper_shovel", TElements.COPPER_ITEM_TIER, 1.5F, -3F)
				.tag(TTags.Items.COPPER_SHOVEL)
				.recipe((ctx, provider) -> RegistratorHelper.shovelRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<PickaxeItem> COPPER_PICKAXE = REGISTRY
			.pickaxeItem("copper_pickaxe", TElements.COPPER_ITEM_TIER, 1, 2.8F)
				.tag(TTags.Items.COPPER_PICKAXE)
				.recipe((ctx, provider) -> RegistratorHelper.pickaxeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<AxeItem> COPPER_AXE = REGISTRY
			.axeItem("copper_axe", TElements.COPPER_ITEM_TIER, 6F, 3.1F)
				.tag(TTags.Items.COPPER_AXE)
				.recipe((ctx, provider) -> RegistratorHelper.axeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<HoeItem> COPPER_HOE = REGISTRY
			.hoeItem("copper_hoe", TElements.COPPER_ITEM_TIER, -2, 1F)
				.tag(TTags.Items.COPPER_HOE)
				.recipe((ctx, provider) -> RegistratorHelper.hoeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_HELMET = REGISTRY
			.helmetArmorItem("copper_helmet", TElements.COPPER_ARMOR_MATERIAL)
				.tag(TTags.Items.COPPER_HELMET)
				.recipe((ctx, provider) -> RegistratorHelper.helmetRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_CHESTPLATE = REGISTRY
			.chestplateArmorItem("copper_chestplate", TElements.COPPER_ARMOR_MATERIAL)
				.tag(TTags.Items.COPPER_CHESTPLATE)
				.recipe((ctx, provider) -> RegistratorHelper.chestplateRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_LEGGINGS = REGISTRY
			.leggingsArmorItem("copper_leggings", TElements.COPPER_ARMOR_MATERIAL)
				.tag(TTags.Items.COPPER_LEGGINGS)
				.recipe((ctx, provider) -> RegistratorHelper.leggingsRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_BOOTS = REGISTRY
			.bootsArmorItem("copper_boots", TElements.COPPER_ARMOR_MATERIAL)
				.tag(TTags.Items.COPPER_BOOTS)
				.recipe((ctx, provider) -> RegistratorHelper.bootsRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<HorseArmorItem> COPPER_HORSE_ARMOR = REGISTRY
			.horseArmorItem("copper_horse_armor", 5, "copper")
				.tag(TTags.Items.COPPER_HORSE_ARMOR)
			.register();

	static void bootstrap() { }
}
