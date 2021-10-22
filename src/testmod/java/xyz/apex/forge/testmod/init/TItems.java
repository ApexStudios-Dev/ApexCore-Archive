package xyz.apex.forge.testmod.init;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import xyz.apex.forge.apexcore.lib.registrate.RegistrateHelper;
import xyz.apex.forge.apexcore.lib.registrate.SimpleRegistrate;
import xyz.apex.forge.testmod.TestMod;

public final class TItems
{
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
			.helper().sword(() -> ItemTier.COPPER, 3, -2.4F)
			.tag(TTags.Items.COPPER_SWORD)
			.recipe((ctx, provider) -> RegistrateHelper.swordRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ShovelItem> COPPER_SHOVEL = REGISTRATE
			.object("copper_shovel")
			.helper().shovel(() -> ItemTier.COPPER, 1.5F, -3F)
			.tag(TTags.Items.COPPER_SHOVEL)
			.recipe((ctx, provider) -> RegistrateHelper.shovelRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<PickaxeItem> COPPER_PICKAXE = REGISTRATE
			.object("copper_pickaxe")
			.helper().pickaxe(() -> ItemTier.COPPER, 1, 2.8F)
			.tag(TTags.Items.COPPER_PICKAXE)
			.recipe((ctx, provider) -> RegistrateHelper.pickaxeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<AxeItem> COPPER_AXE = REGISTRATE
			.object("copper_axe")
			.helper().axe(() -> ItemTier.COPPER, 6F, 3.1F)
			.tag(TTags.Items.COPPER_AXE)
			.recipe((ctx, provider) -> RegistrateHelper.axeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<HoeItem> COPPER_HOE = REGISTRATE
			.object("copper_hoe")
			.helper().hoe(() -> ItemTier.COPPER, -2, 1F)
			.tag(TTags.Items.COPPER_HOE)
			.recipe((ctx, provider) -> RegistrateHelper.hoeRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_HELMET = REGISTRATE
			.object("copper_helmet")
			.helper().helmet(() -> ArmorMaterial.COPPER)
			.tag(TTags.Items.COPPER_HELMET)
			.recipe((ctx, provider) -> RegistrateHelper.helmetRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_CHESTPLATE = REGISTRATE
			.object("copper_chestplate")
			.helper().chestplate(() -> ArmorMaterial.COPPER)
			.tag(TTags.Items.COPPER_CHESTPLATE)
			.recipe((ctx, provider) -> RegistrateHelper.chestplateRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_LEGGINGS = REGISTRATE
			.object("copper_leggings")
			.helper().leggings(() -> ArmorMaterial.COPPER)
			.tag(TTags.Items.COPPER_LEGGINGS)
			.recipe((ctx, provider) -> RegistrateHelper.leggingsRecipe(ctx, provider, TTags.Items.INGOTS_COPPER))
			.register();

	public static final ItemEntry<ArmorItem> COPPER_BOOTS = REGISTRATE
			.object("copper_boots")
			.helper().boots(() -> ArmorMaterial.COPPER)
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
