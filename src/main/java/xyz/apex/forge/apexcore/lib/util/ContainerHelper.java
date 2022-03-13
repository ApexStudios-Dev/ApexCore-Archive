package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public final class ContainerHelper
{
	public static int getRedstoneSignalFromContainer(IBlockReader level, BlockPos pos)
	{
		TileEntity blockEntity = level.getBlockEntity(pos);
		return getRedstoneSignalFromContainer(blockEntity);
	}

	public static int getRedstoneSignalFromContainer(@Nullable TileEntity blockEntity)
	{
		if(blockEntity == null)
			return 0;
		if(blockEntity instanceof IInventory)
			return Container.getRedstoneSignalFromContainer((IInventory) blockEntity);
		if(blockEntity instanceof IItemHandler)
			return getRedstoneSignalFromContainer((IItemHandler) blockEntity);

		return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(ContainerHelper::getRedstoneSignalFromContainer).orElse(0);
	}

	public static int getRedstoneSignalFromContainer(@Nullable IItemHandler itemHandler)
	{
		float f = 0F;
		int counter = 0;

		for(int i = 0; i < itemHandler.getSlots(); i++)
		{
			ItemStack stack = itemHandler.getStackInSlot(i);

			if(!stack.isEmpty())
			{
				f += (float)stack.getCount() / (float) Math.min(itemHandler.getSlotLimit(i), stack.getMaxStackSize());
				counter++;
			}
		}

		f = f / itemHandler.getSlots();
		return MathHelper.floor(f * 14F) + (counter > 0 ? 1 : 0);
	}
}
