package xyz.apex.forge.apexcore.lib.item;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);

		for(EquipmentSlotType slotType : slotTypes)
		{
			ItemStack slotItem = player.getItemBySlot(slotType);

			if(slotItem.isEmpty())
			{
				player.setItemSlot(slotType, itemstack.copy());
				itemstack.setCount(0);
				return ActionResult.sidedSuccess(itemstack, level.isClientSide);
			}
		}

		return ActionResult.fail(itemstack);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity)
	{
		return armorType.getType() == EquipmentSlotType.Group.HAND || slotTypes.contains(armorType);
	}
}
