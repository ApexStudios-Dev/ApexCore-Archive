package xyz.apex.forge.testmod.init;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import xyz.apex.forge.apexcore.lib.item.ArmorMaterial;
import xyz.apex.forge.apexcore.lib.item.ItemTier;
import xyz.apex.forge.apexcore.lib.registrate.RegistrateHelper;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.testmod.TestMod;

public final class TItems
{
	public static final NonNullLazyValue<IItemTier> COPPER_ITEM_TIER = new NonNullLazyValue<>(() -> new ItemTier(2, 250, 6F, 2F, 14, () -> DataIngredient.tag(TTags.Items.INGOTS_COPPER)));
	public static final NonNullLazyValue<IArmorMaterial> COPPER_ARMOR_MATERIAL = new NonNullLazyValue<>(() -> new ArmorMaterial(TestMod.getMod().prefix("copper"), 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_IRON, 0F, 0F, () -> DataIngredient.tag(TTags.Items.INGOTS_COPPER)));

	private static final SimpleRegistrate REGISTRATE = TestMod.registrate();

	// @formatter:off
	public static final ItemEntry<Item> TEST_ITEM = REGISTRATE
			.object("test_item")
			.item(Item::new)
			.tag(TTags.Items.TEST_ITEM)
			.register();

	public static final ItemEntry<Item> COPPER_INGOT = REGISTRATE
			.object("copper_ingot")
			.item(Item::new)
			.tag(TTags.Items.COPPER_INGOT)
			.register();

	public static final ItemEntry<SwordItem> COPPER_SWORD = REGISTRATE
			.object("copper_sword")
			.helper().sword(COPPER_ITEM_TIER, 3, -2.4F)
			.tag(TTags.Items.COPPER_SWORD)
			.recipe((ctx, provider) -> RegistrateHelper.swordRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ShovelItem> COPPER_SHOVEL = REGISTRATE
			.object("copper_shovel")
			.helper().shovel(COPPER_ITEM_TIER, 1.5F, -3F)
			.tag(TTags.Items.COPPER_SHOVEL)
			.recipe((ctx, provider) -> RegistrateHelper.shovelRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<PickaxeItem> COPPER_PICKAXE = REGISTRATE
			.object("copper_pickaxe")
			.helper().pickaxe(COPPER_ITEM_TIER, 1, 2.8F)
			.tag(TTags.Items.COPPER_PICKAXE)
			.recipe((ctx, provider) -> RegistrateHelper.pickaxeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<AxeItem> COPPER_AXE = REGISTRATE
			.object("copper_axe")
			.helper().axe(COPPER_ITEM_TIER, 6F, 3.1F)
			.tag(TTags.Items.COPPER_AXE)
			.recipe((ctx, provider) -> RegistrateHelper.axeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<HoeItem> COPPER_HOE = REGISTRATE
			.object("copper_hoe")
			.helper().hoe(COPPER_ITEM_TIER, -2, 1F)
			.tag(TTags.Items.COPPER_HOE)
			.recipe((ctx, provider) -> RegistrateHelper.hoeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_HELMET = REGISTRATE
			.object("copper_helmet")
			.helper().helmet(COPPER_ARMOR_MATERIAL)
			.tag(TTags.Items.COPPER_HELMET)
			.recipe((ctx, provider) -> RegistrateHelper.helmetRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_CHESTPLATE = REGISTRATE
			.object("copper_chestplate")
			.helper().chestplate(COPPER_ARMOR_MATERIAL)
			.tag(TTags.Items.COPPER_CHESTPLATE)
			.recipe((ctx, provider) -> RegistrateHelper.chestplateRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_LEGGINGS = REGISTRATE
			.object("copper_leggings")
			.helper().leggings(COPPER_ARMOR_MATERIAL)
			.tag(TTags.Items.COPPER_LEGGINGS)
			.recipe((ctx, provider) -> RegistrateHelper.leggingsRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_BOOTS = REGISTRATE
			.object("copper_boots")
			.helper().boots(COPPER_ARMOR_MATERIAL)
			.tag(TTags.Items.COPPER_BOOTS)
			.recipe((ctx, provider) -> RegistrateHelper.bootsRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<HorseArmorItem> COPPER_HORSE_ARMOR = REGISTRATE
			.object("copper_horse_armor")
			.helper().horseArmor(5)
			.tag(TTags.Items.COPPER_HORSE_ARMOR)
			.register();
	// @formatter:on

	public static void register() { }
}
