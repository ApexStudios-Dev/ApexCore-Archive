package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class WearableBlockItem extends BlockItem
{
	private final Set<EquipmentSlotType> slotTypes;

	public WearableBlockItem(Block block, Properties properties, EquipmentSlotType... slotTypes)
	{
		super(block, properties);

		this.slotTypes = ImmutableSet.copyOf(slotTypes);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity)
	{
		return armorType.getType() == EquipmentSlotType.Group.HAND || slotTypes.contains(armorType);
	}
}
