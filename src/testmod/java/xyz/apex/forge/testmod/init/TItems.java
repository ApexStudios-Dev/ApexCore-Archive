package xyz.apex.forge.testmod.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.testmod.TestMod;

public final class TItems
{
	private static final SimpleRegistrate REGISTRATE = TestMod.registrate();

	// @formatter:off
	public static final ItemEntry<Item> TEST_ITEM = REGISTRATE
			.object("test_item")
			.item(Item::new)
			.defaultModel()
			.defaultLang()
			.tag(TTags.Items.TEST_ITEM)
			.register();

	public static final ItemEntry<Item> COPPER_INGOT = REGISTRATE
			.object("copper_ingot")
			.item(Item::new)
			.defaultModel()
			.defaultLang()
			.tag(TTags.Items.COPPER_INGOT)
			.register();

	public static final ItemEntry<SwordItem> COPPER_SWORD = REGISTRATE
			.object("copper_sword")
			.item(properties -> new SwordItem(ItemTier.COPPER, 3, -2.4F, properties))
			.model((ctx, provider) -> provider.handheld(ctx))
			.defaultLang()
			.tag(TTags.Items.COPPER_SWORD)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.define('S', Tags.Items.RODS_WOODEN)
					.pattern("I")
					.pattern("I")
					.pattern("S")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<ShovelItem> COPPER_SHOVEL = REGISTRATE
			.object("copper_shovel")
			.item(properties -> new ShovelItem(ItemTier.COPPER, 1.5F, -3F, properties))
			.model((ctx, provider) -> provider.handheld(ctx))
			.defaultLang()
			.tag(TTags.Items.COPPER_SHOVEL)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.define('S', Tags.Items.RODS_WOODEN)
					.pattern("I")
					.pattern("S")
					.pattern("S")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<PickaxeItem> COPPER_PICKAXE = REGISTRATE
			.object("copper_pickaxe")
			.item(properties -> new PickaxeItem(ItemTier.COPPER, 1, 2.8F, properties))
			.model((ctx, provider) -> provider.handheld(ctx))
			.defaultLang()
			.tag(TTags.Items.COPPER_PICKAXE)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.define('S', Tags.Items.RODS_WOODEN)
					.pattern("III")
					.pattern(" S ")
					.pattern(" S ")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<AxeItem> COPPER_AXE = REGISTRATE
			.object("copper_axe")
			.item(properties -> new AxeItem(ItemTier.COPPER, 6F, 3.1F, properties))
			.model((ctx, provider) -> provider.handheld(ctx))
			.defaultLang()
			.tag(TTags.Items.COPPER_AXE)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.define('S', Tags.Items.RODS_WOODEN)
					.pattern("II")
					.pattern("IS")
					.pattern(" S")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<HoeItem> COPPER_HOE = REGISTRATE
			.object("copper_hoe")
			.item(properties -> new HoeItem(ItemTier.COPPER, -2, 1F, properties))
			.model((ctx, provider) -> provider.handheld(ctx))
			.defaultLang()
			.tag(TTags.Items.COPPER_HOE)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.define('S', Tags.Items.RODS_WOODEN)
					.pattern("II")
					.pattern(" S")
					.pattern(" S")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<ArmorItem> COPPER_HELMET = REGISTRATE
			.object("copper_helmet")
			.item(properties -> new ArmorItem(ArmorMaterial.COPPER, EquipmentSlotType.HEAD, properties))
			.defaultModel()
			.defaultLang()
			.tag(TTags.Items.COPPER_HELMET)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.pattern("III")
					.pattern("I I")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<ArmorItem> COPPER_CHESTPLATE = REGISTRATE
			.object("copper_chestplate")
			.item(properties -> new ArmorItem(ArmorMaterial.COPPER, EquipmentSlotType.CHEST, properties))
			.defaultModel()
			.defaultLang()
			.tag(TTags.Items.COPPER_CHESTPLATE)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.pattern("I I")
					.pattern("III")
					.pattern("III")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<ArmorItem> COPPER_LEGGINGS = REGISTRATE
			.object("copper_leggings")
			.item(properties -> new ArmorItem(ArmorMaterial.COPPER, EquipmentSlotType.LEGS, properties))
			.defaultModel()
			.defaultLang()
			.tag(TTags.Items.COPPER_LEGGINGS)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.pattern("III")
					.pattern("I I")
					.pattern("I I")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<ArmorItem> COPPER_BOOTS = REGISTRATE
			.object("copper_boots")
			.item(properties -> new ArmorItem(ArmorMaterial.COPPER, EquipmentSlotType.FEET, properties))
			.defaultModel()
			.defaultLang()
			.tag(TTags.Items.COPPER_BOOTS)
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx::get, 1)
					.define('I', TTags.Items.INGOTS_COPPER)
					.pattern("I I")
					.pattern("I I")
					.unlockedBy("has_copper_ingot", RegistrateRecipeProvider.hasItem(TTags.Items.INGOTS_COPPER))
					.save(provider)
			)
			.register();

	public static final ItemEntry<HorseArmorItem> COPPER_HORSE_ARMOR = REGISTRATE
			.object("copper_horse_armor")
			.item(properties -> new HorseArmorItem(5, TestMod.getMod().id("textures/entity/horse/armor/horse_armor_copper.png"), properties))
			.properties(properties -> properties.stacksTo(1))
			.defaultModel()
			.defaultLang()
			.tag(TTags.Items.COPPER_HORSE_ARMOR)
			.register();
	// @formatter:on

	public static void register() { }

	public enum ItemTier implements IItemTier
	{
		COPPER(2, 250, 6F, 2F, 14, () -> DataIngredient.tag(TTags.Items.INGOTS_COPPER)),
		;

		public final int uses;
		public final float speed;
		public final float damage;
		public final int level;
		public final int enchantmentValue;
		public final NonNullLazyValue<DataIngredient> repairIngredient;

		ItemTier(int level, int uses, float speed, float damage, int enchantmentValue, NonNullSupplier<DataIngredient> repairIngredient)
		{
			this.uses = uses;
			this.speed = speed;
			this.damage = damage;
			this.level = level;
			this.enchantmentValue = enchantmentValue;
			this.repairIngredient = new NonNullLazyValue<>(repairIngredient);
		}

		@Override
		public int getUses()
		{
			return uses;
		}

		@Override
		public float getSpeed()
		{
			return speed;
		}

		@Override
		public float getAttackDamageBonus()
		{
			return damage;
		}

		@Override
		public int getLevel()
		{
			return level;
		}

		@Override
		public int getEnchantmentValue()
		{
			return enchantmentValue;
		}

		@Override
		public Ingredient getRepairIngredient()
		{
			return repairIngredient.get();
		}
	}

	public enum ArmorMaterial implements IArmorMaterial
	{
		COPPER(TestMod.getMod().prefix("copper"), 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_IRON, 0F, 0F, () -> DataIngredient.tag(TTags.Items.INGOTS_COPPER)),
		;

		// MUST MATCH VANILLA VALUE ArmorMaterial#HEALTH_PER_SLOT
		private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };

		public final String name;
		public final int durabilityMultiplier;
		public final int[] slotProtections;
		public final int enchantmentValue;
		public final SoundEvent sound;
		public final float toughness;
		public final float knockbackResistance;
		public final NonNullLazyValue<DataIngredient> repairIngredient;

		ArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, NonNullSupplier<DataIngredient> repairIngredient)
		{
			this.name = name;
			this.durabilityMultiplier = durabilityMultiplier;
			this.slotProtections = slotProtections;
			this.enchantmentValue = enchantmentValue;
			this.sound = sound;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairIngredient = new NonNullLazyValue<>(repairIngredient);
		}

		@Override
		public int getDurabilityForSlot(EquipmentSlotType slotType)
		{
			return HEALTH_PER_SLOT[slotType.getIndex()] * durabilityMultiplier;
		}

		@Override
		public int getDefenseForSlot(EquipmentSlotType slotType)
		{
			return slotProtections[slotType.getIndex()];
		}

		@Override
		public int getEnchantmentValue()
		{
			return enchantmentValue;
		}

		@Override
		public SoundEvent getEquipSound()
		{
			return sound;
		}

		@Override
		public Ingredient getRepairIngredient()
		{
			return repairIngredient.get();
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public float getToughness()
		{
			return toughness;
		}

		@Override
		public float getKnockbackResistance()
		{
			return knockbackResistance;
		}
	}
}
