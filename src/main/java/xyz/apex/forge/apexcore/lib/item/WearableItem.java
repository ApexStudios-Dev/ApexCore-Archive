package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class WearableItem extends Item
{
	private final Set<EquipmentSlotType> slotTypes;

	public WearableItem(Properties properties, EquipmentSlotType... slotTypes)
	{
		super(properties);

		this.slotTypes = ImmutableSet.copyOf(slotTypes);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity)
	{
		return armorType.getType() == EquipmentSlotType.Group.HAND || slotTypes.contains(armorType);
	}
}
