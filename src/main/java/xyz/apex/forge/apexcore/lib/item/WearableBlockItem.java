package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class WearableBlockItem extends BlockItem
{
	private final Set<EquipmentSlot> slotTypes;

	public WearableBlockItem(Block block, Properties properties, EquipmentSlot... slotTypes)
	{
		super(block, properties);

		this.slotTypes = ImmutableSet.copyOf(slotTypes);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity)
	{
		return armorType.getType() == EquipmentSlot.Type.HAND || slotTypes.contains(armorType);
	}
}
