package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class WearableItem extends Item
{
	private final Set<EquipmentSlot> slotTypes;

	public WearableItem(Properties properties, EquipmentSlot... slotTypes)
	{
		super(properties);

		this.slotTypes = ImmutableSet.copyOf(slotTypes);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity)
	{
		return armorType.getType() == EquipmentSlot.Type.HAND || slotTypes.contains(armorType);
	}
}
