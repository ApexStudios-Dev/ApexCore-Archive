package xyz.apex.forge.apexcore.lib.container.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Constants;

import xyz.apex.forge.apexcore.lib.constants.NbtNames;

public class ItemInventory extends SimpleContainer
{
	private final ItemStack item;
	private int openCount = 0;

	public ItemInventory(ItemStack item, int slots)
	{
		super(slots);

		this.item = item;
	}

	@Override
	public void startOpen(Player player)
	{
		if(openCount == 0)
		{
			var invTag = item.getTagElement(NbtNames.INVENTORY);

			if(invTag != null)
			{
				var slotTag = invTag.getList(NbtNames.ITEMS, Constants.NBT.TAG_COMPOUND);

				for(var i = 0; i < slotTag.size(); i++)
				{
					var itemTag = slotTag.getCompound(i);
					var slotIndex = itemTag.getByte(NbtNames.SLOT) & 255;

					if(slotIndex < getContainerSize())
						setItem(slotIndex, ItemStack.of(itemTag));
				}
			}
		}

		openCount++;
	}

	@Override
	public void stopOpen(Player player)
	{
		openCount--;

		if(openCount == 0)
		{
			var invTag = new CompoundTag();
			var slotTag = new ListTag();

			for(var i = 0; i < getContainerSize(); i++)
			{
				var stack = getItem(i);

				if(!stack.isEmpty())
				{
					var itemTag = new CompoundTag();
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
