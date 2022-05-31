package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public final class ContainerHelper
{
	public static int getRedstoneSignalFromContainer(LevelAccessor level, BlockPos pos)
	{
		var blockEntity = level.getBlockEntity(pos);
		return getRedstoneSignalFromContainer(blockEntity);
	}

	public static int getRedstoneSignalFromContainer(@Nullable BlockEntity blockEntity)
	{
		if(blockEntity == null)
			return 0;
		if(blockEntity instanceof Container container)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(container);
		if(blockEntity instanceof IItemHandler itemHandler)
			return getRedstoneSignalFromContainer(itemHandler);

		return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(ContainerHelper::getRedstoneSignalFromContainer).orElse(0);
	}

	public static int getRedstoneSignalFromContainer(@Nullable IItemHandler itemHandler)
	{
		if(itemHandler == null)
			return 0;

		var f = 0F;
		var counter = 0;

		for(var i = 0; i < itemHandler.getSlots(); i++)
		{
			var stack = itemHandler.getStackInSlot(i);

			if(!stack.isEmpty())
			{
				f += (float)stack.getCount() / (float) Math.min(itemHandler.getSlotLimit(i), stack.getMaxStackSize());
				counter++;
			}
		}

		f = f / itemHandler.getSlots();
		return Mth.floor(f * 14F) + (counter > 0 ? 1 : 0);
	}
}
