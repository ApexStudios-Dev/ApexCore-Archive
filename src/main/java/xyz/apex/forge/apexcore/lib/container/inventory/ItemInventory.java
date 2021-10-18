package xyz.apex.forge.apexcore.lib.container.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import xyz.apex.forge.apexcore.lib.constants.NbtNames;

public class ItemInventory extends Inventory
{
	private final ItemStack item;
	private int openCount = 0;

	public ItemInventory(ItemStack item, int slots)
	{
		super(slots);

		this.item = item;
	}

	@Override
	public void startOpen(PlayerEntity player)
	{
		if(openCount == 0)
		{
			CompoundNBT invTag = item.getTagElement(NbtNames.INVENTORY);

			if(invTag != null)
			{
				ListNBT slotTag = invTag.getList(NbtNames.ITEMS, Constants.NBT.TAG_COMPOUND);

				for(int i = 0; i < slotTag.size(); i++)
				{
					CompoundNBT itemTag = slotTag.getCompound(i);
					int slotIndex = itemTag.getByte(NbtNames.SLOT) & 255;

					if(slotIndex < getContainerSize())
						setItem(slotIndex, ItemStack.of(itemTag));
				}
			}
		}

		openCount++;
	}

	@Override
	public void stopOpen(PlayerEntity player)
	{
		openCount--;

		if(openCount == 0)
		{
			CompoundNBT invTag = new CompoundNBT();
			ListNBT slotTag = new ListNBT();

			for(int i = 0; i < getContainerSize(); i++)
			{
				ItemStack stack = getItem(i);

				if(!stack.isEmpty())
				{
					CompoundNBT itemTag = new CompoundNBT();
					itemTag.putByte(NbtNames.SLOT, (byte) i);
					stack.save(itemTag);
					slotTag.add(itemTag);
				}
			}

			invTag.put(NbtNames.ITEMS, slotTag);
			item.addTagElement(NbtNames.INVENTORY, invTag);
		}
	}

	public ItemStack getContainerItem()
	{
		return item;
	}
}
