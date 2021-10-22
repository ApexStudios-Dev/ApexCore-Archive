package xyz.apex.forge.apexcore.lib.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import xyz.apex.forge.apexcore.lib.util.reflection.Methods;

public class RegistrateHelper<R extends CustomRegistrate<R>>
{
	private final R registrate;

	RegistrateHelper(R registrate)
	{
		this.registrate = registrate;
	}

	public R getRegistrate()
	{
		return registrate;
	}

	protected String currentName()
	{
		return Methods.REGISTRATE_CURRENT_NAME.invokeMethod(registrate);
	}

	// region: Item
	// region: Weapons
	// region: Sword
	public ItemBuilder<SwordItem, R> sword(NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return sword(registrate, currentName(), SwordItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<SwordItem, P> sword(P parent, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return sword(parent, currentName(), SwordItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public ItemBuilder<SwordItem, R> sword(String name, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return sword(registrate, name, SwordItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<SwordItem, P> sword(P parent, String name, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return sword(parent, name, SwordItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends SwordItem> ItemBuilder<T, R> sword(SwordItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return sword(registrate, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends SwordItem, P> ItemBuilder<T, P> sword(P parent, SwordItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return sword(parent, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends SwordItem> ItemBuilder<T, R> sword(String name, SwordItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return sword(registrate, name, factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends SwordItem, P> ItemBuilder<T, P> sword(P parent, String name, SwordItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		// @formatter:off
		return registrate
				.item(parent, name, properties -> factory.create(itemTierSupplier.get(), attackDamage, attackSpeed, properties))
				.model(RegistrateHelper::handheldModel)
		;
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Tools
	// region: Pickaxe
	public ItemBuilder<PickaxeItem, R> pickaxe(NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return pickaxe(registrate, currentName(), PickaxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<PickaxeItem, P> pickaxe(P parent, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return pickaxe(parent, currentName(), PickaxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public ItemBuilder<PickaxeItem, R> pickaxe(String name, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return pickaxe(registrate, name, PickaxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<PickaxeItem, P> pickaxe(P parent, String name, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return pickaxe(parent, name, PickaxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends PickaxeItem> ItemBuilder<T, R> pickaxe(PickaxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return pickaxe(registrate, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends PickaxeItem, P> ItemBuilder<T, P> pickaxe(P parent, PickaxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return pickaxe(parent, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends PickaxeItem> ItemBuilder<T, R> pickaxe(String name, PickaxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return pickaxe(registrate, name, factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends PickaxeItem, P> ItemBuilder<T, P> pickaxe(P parent, String name, PickaxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		// @formatter:off
		return registrate
				.item(parent, name, properties -> factory.create(itemTierSupplier.get(), attackDamage, attackSpeed, properties))
				.model(RegistrateHelper::handheldModel)
		;
		// @formatter:on
	}
	// endregion

	// region: Axe
	public ItemBuilder<AxeItem, R> axe(NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return axe(registrate, currentName(), AxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<AxeItem, P> axe(P parent, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return axe(parent, currentName(), AxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public ItemBuilder<AxeItem, R> axe(String name, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return axe(registrate, name, AxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<AxeItem, P> axe(P parent, String name, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return axe(parent, name, AxeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends AxeItem> ItemBuilder<T, R> axe(AxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return axe(registrate, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends AxeItem, P> ItemBuilder<T, P> axe(P parent, AxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return axe(parent, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends AxeItem> ItemBuilder<T, R> axe(String name, AxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return axe(registrate, name, factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends AxeItem, P> ItemBuilder<T, P> axe(P parent, String name, AxeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		// @formatter:off
		return registrate
				.item(parent, name, properties -> factory.create(itemTierSupplier.get(), attackDamage, attackSpeed, properties))
				.model(RegistrateHelper::handheldModel)
		;
		// @formatter:on
	}
	// endregion

	// region: Shovel
	public ItemBuilder<ShovelItem, R> shovel(NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return shovel(registrate, currentName(), ShovelItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<ShovelItem, P> shovel(P parent, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return shovel(parent, currentName(), ShovelItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public ItemBuilder<ShovelItem, R> shovel(String name, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return shovel(registrate, name, ShovelItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<ShovelItem, P> shovel(P parent, String name, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return shovel(parent, name, ShovelItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends ShovelItem> ItemBuilder<T, R> shovel(ShovelItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return shovel(registrate, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends ShovelItem, P> ItemBuilder<T, P> shovel(P parent, ShovelItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return shovel(parent, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends ShovelItem> ItemBuilder<T, R> shovel(String name, ShovelItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		return shovel(registrate, name, factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends ShovelItem, P> ItemBuilder<T, P> shovel(P parent, String name, ShovelItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, float attackDamage, float attackSpeed)
	{
		// @formatter:off
		return registrate
				.item(parent, name, properties -> factory.create(itemTierSupplier.get(), attackDamage, attackSpeed, properties))
				.model(RegistrateHelper::handheldModel)
		;
		// @formatter:on
	}
	// endregion

	// region: Hoe
	public ItemBuilder<HoeItem, R> hoe(NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return hoe(registrate, currentName(), HoeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<HoeItem, P> hoe(P parent, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return hoe(parent, currentName(), HoeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public ItemBuilder<HoeItem, R> hoe(String name, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return hoe(registrate, name, HoeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <P> ItemBuilder<HoeItem, P> hoe(P parent, String name, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return hoe(parent, name, HoeItem::new, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends HoeItem> ItemBuilder<T, R> hoe(HoeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return hoe(registrate, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends HoeItem, P> ItemBuilder<T, P> hoe(P parent, HoeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return hoe(parent, currentName(), factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends HoeItem> ItemBuilder<T, R> hoe(String name, HoeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		return hoe(registrate, name, factory, itemTierSupplier, attackDamage, attackSpeed);
	}

	public <T extends HoeItem, P> ItemBuilder<T, P> hoe(P parent, String name, HoeItemFactory<T> factory, NonNullSupplier<IItemTier> itemTierSupplier, int attackDamage, float attackSpeed)
	{
		// @formatter:off
		return registrate
				.item(parent, name, properties -> factory.create(itemTierSupplier.get(), attackDamage, attackSpeed, properties))
				.model(RegistrateHelper::handheldModel)
		;
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Armor
	// region: Generic
	public ItemBuilder<ArmorItem, R> armor(NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return armor(registrate, currentName(), ArmorItem::new, armorMaterialSupplier, slotType);
	}

	public <P> ItemBuilder<ArmorItem, P> armor(P parent, NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return armor(parent, currentName(), ArmorItem::new, armorMaterialSupplier, slotType);
	}

	public ItemBuilder<ArmorItem, R> armor(String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return armor(registrate, name, ArmorItem::new, armorMaterialSupplier, slotType);
	}

	public <P> ItemBuilder<ArmorItem, P> armor(P parent, String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return armor(parent, name, ArmorItem::new, armorMaterialSupplier, slotType);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> armor(ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return armor(registrate, currentName(), factory, armorMaterialSupplier, slotType);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> armor(P parent, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return armor(parent, currentName(), factory, armorMaterialSupplier, slotType);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> armor(String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return armor(registrate, name, factory, armorMaterialSupplier, slotType);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> armor(P parent, String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier, EquipmentSlotType slotType)
	{
		return registrate.item(parent, name, properties -> factory.create(armorMaterialSupplier.get(), slotType, properties));
	}
	// endregion

	// region: Helmet
	public ItemBuilder<ArmorItem, R> helmet(NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return helmet(registrate, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> helmet(P parent, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return helmet(parent, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public ItemBuilder<ArmorItem, R> helmet(String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return helmet(registrate, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> helmet(P parent, String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return helmet(parent, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> helmet(ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return helmet(registrate, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> helmet(P parent, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return helmet(parent, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> helmet(String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return helmet(registrate, name, factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> helmet(P parent, String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return armor(parent, name, factory, armorMaterialSupplier, EquipmentSlotType.HEAD);
	}
	// endregion

	// region: Chestplate
	public ItemBuilder<ArmorItem, R> chestplate(NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return chestplate(registrate, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> chestplate(P parent, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return chestplate(parent, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public ItemBuilder<ArmorItem, R> chestplate(String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return chestplate(registrate, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> chestplate(P parent, String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return chestplate(parent, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> chestplate(ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return chestplate(registrate, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> chestplate(P parent, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return chestplate(parent, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> chestplate(String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return chestplate(registrate, name, factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> chestplate(P parent, String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return armor(parent, name, factory, armorMaterialSupplier, EquipmentSlotType.CHEST);
	}
	// endregion

	// region: Leggings
	public ItemBuilder<ArmorItem, R> leggings(NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return leggings(registrate, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> leggings(P parent, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return leggings(parent, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public ItemBuilder<ArmorItem, R> leggings(String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return leggings(registrate, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> leggings(P parent, String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return leggings(parent, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> leggings(ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return leggings(registrate, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> leggings(P parent, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return leggings(parent, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> leggings(String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return leggings(registrate, name, factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> leggings(P parent, String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return armor(parent, name, factory, armorMaterialSupplier, EquipmentSlotType.LEGS);
	}
	// endregion

	// region: Boots
	public ItemBuilder<ArmorItem, R> boots(NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return boots(registrate, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> boots(P parent, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return boots(parent, currentName(), ArmorItem::new, armorMaterialSupplier);
	}

	public ItemBuilder<ArmorItem, R> boots(String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return boots(registrate, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <P> ItemBuilder<ArmorItem, P> boots(P parent, String name, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return boots(parent, name, ArmorItem::new, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> boots(ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return boots(registrate, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> boots(P parent, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return boots(parent, currentName(), factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem> ItemBuilder<T, R> boots(String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return boots(registrate, name, factory, armorMaterialSupplier);
	}

	public <T extends ArmorItem, P> ItemBuilder<T, P> boots(P parent, String name, ArmorItemFactory<T> factory, NonNullSupplier<IArmorMaterial> armorMaterialSupplier)
	{
		return armor(parent, name, factory, armorMaterialSupplier, EquipmentSlotType.FEET);
	}
	// endregion

	// region: Horse
	private ResourceLocation generateHorseArmorTexturePath(String name)
	{
		return registrate.getMod().id("textures/entity/horse/armor/" + name + ".png");
	}

	public ItemBuilder<HorseArmorItem, R> horseArmor(int protection)
	{
		return horseArmor(registrate, currentName(), HorseArmorItem::new, protection);
	}

	public <P> ItemBuilder<HorseArmorItem, P> horseArmor(P parent, int protection)
	{
		return horseArmor(parent, currentName(), HorseArmorItem::new, protection);
	}

	public ItemBuilder<HorseArmorItem, R> horseArmor(String name, int protection)
	{
		return horseArmor(registrate, name, HorseArmorItem::new, protection);
	}

	public <P> ItemBuilder<HorseArmorItem, P> horseArmor(P parent, String name, int protection)
	{
		return horseArmor(parent, name, HorseArmorItem::new, protection);
	}

	public <T extends HorseArmorItem> ItemBuilder<T, R> horseArmor(HorseArmorItemFactory<T> factory, int protection)
	{
		return horseArmor(registrate, currentName(), factory, protection);
	}

	public <T extends HorseArmorItem, P> ItemBuilder<T, P> horseArmor(P parent, HorseArmorItemFactory<T> factory, int protection)
	{
		return horseArmor(parent, currentName(), factory, protection);
	}

	public <T extends HorseArmorItem> ItemBuilder<T, R> horseArmor(String name, HorseArmorItemFactory<T> factory, int protection)
	{
		return horseArmor(registrate, name, factory, protection);
	}

	public <T extends HorseArmorItem, P> ItemBuilder<T, P> horseArmor(P parent, String name, HorseArmorItemFactory<T> factory, int protection)
	{
		// @formatter:off
		return registrate
				.item(parent, name, properties -> factory.create(protection, generateHorseArmorTexturePath(name), properties))
				.properties(properties -> properties.stacksTo(1))
		;
		// @formatter:on
	}
	// endregion
	// endregion
	// endregion

	// region: Models
	public static <I extends Item> void blockItemModel(DataGenContext<Item, I> ctx, RegistrateItemModelProvider provider)
	{
		provider.blockItem(ctx);
	}

	public static <I extends Item> void handheldModel(DataGenContext<Item, I> ctx, RegistrateItemModelProvider provider)
	{
		provider.handheld(ctx);
	}
	// endregion

	// region: Recipes
	// region: Weapons
	// region: Sword
	public static <I extends Item> void swordRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		swordRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <I extends Item> void swordRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("I")
				.pattern("I")
				.pattern("S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Tools
	// region: Pickaxe
	public static <I extends Item> void pickaxeRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		pickaxeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <I extends Item> void pickaxeRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("III")
				.pattern(" S ")
				.pattern(" S ")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Axe
	public static <I extends Item> void axeRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		axeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <I extends Item> void axeRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("II")
				.pattern("IS")
				.pattern(" S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Shovel
	public static <I extends Item> void shovelRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		shovelRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <I extends Item> void shovelRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("I")
				.pattern("S")
				.pattern("S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Hoe
	public static <I extends Item> void hoeRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		hoeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <I extends Item> void hoeRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("II")
				.pattern(" S")
				.pattern(" S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Armor
	public static <I extends Item> void helmetRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("III")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <I extends Item> void chestplateRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("I I")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <I extends Item> void leggingsRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("III")
				.pattern("I I")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <I extends Item> void bootsRecipe(DataGenContext<Item, I> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("I I")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Factories
	@FunctionalInterface
	public interface SwordItemFactory<T extends SwordItem>
	{
		T create(IItemTier itemTier, int attackDamage, float attackSpeed, Item.Properties properties);
	}

	@FunctionalInterface
	public interface PickaxeItemFactory<T extends PickaxeItem>
	{
		T create(IItemTier itemTier, int attackDamage, float attackSpeed, Item.Properties properties);
	}

	@FunctionalInterface
	public interface AxeItemFactory<T extends AxeItem>
	{
		T create(IItemTier itemTier, float attackDamage, float attackSpeed, Item.Properties properties);
	}

	@FunctionalInterface
	public interface ShovelItemFactory<T extends ShovelItem>
	{
		T create(IItemTier itemTier, float attackDamage, float attackSpeed, Item.Properties properties);
	}

	@FunctionalInterface
	public interface HoeItemFactory<T extends HoeItem>
	{
		T create(IItemTier itemTier, int attackDamage, float attackSpeed, Item.Properties properties);
	}

	@FunctionalInterface
	public interface ArmorItemFactory<T extends ArmorItem>
	{
		T create(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Item.Properties properties);
	}

	@FunctionalInterface
	public interface HorseArmorItemFactory<T extends HorseArmorItem>
	{
		T create(int protection, ResourceLocation texturePath, Item.Properties properties);
	}
	// endregion
}
