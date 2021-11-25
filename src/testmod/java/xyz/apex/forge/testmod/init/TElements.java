package xyz.apex.forge.testmod.init;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.util.SoundEvents;

import xyz.apex.forge.utility.registrator.helper.ArmorMaterial;
import xyz.apex.forge.utility.registrator.helper.ItemTier;

public final class TElements
{
	private static final TRegistry REGISTRY = TRegistry.getRegistry();

	public static final IItemTier COPPER_ITEM_TIER = ItemTier
			.builder()
				.level(2)
				.uses(250)
				.speed(6F)
				.damage(2F)
				.enchantmentValue(14)
				.repairIngredient(TTags.Items.INGOTS_COPPER)
			.build();

	public static final IArmorMaterial COPPER_ARMOR_MATERIAL = ArmorMaterial
			.builder(REGISTRY.id("copper"))
				.durabilityMultiplier(15)
				.defenseForSlot(EquipmentSlotType.FEET, 2)
				.defenseForSlot(EquipmentSlotType.LEGS, 5)
				.defenseForSlot(EquipmentSlotType.CHEST, 6)
				.defenseForSlot(EquipmentSlotType.HEAD, 2)
				.enchantmentValue(9)
				.sound(() -> SoundEvents.ARMOR_EQUIP_IRON)
				.toughness(0F)
				.knockbackResistance(0F)
				.repairIngredient(TTags.Items.INGOTS_COPPER)
			.build();

	static void bootstrap() { }
}
