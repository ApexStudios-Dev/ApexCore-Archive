package xyz.apex.forge.apexcore.lib.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import xyz.apex.forge.apexcore.lib.container.inventory.ItemInventory;

import javax.annotation.Nullable;

public class ItemInventoryContainer extends BaseContainer
{
	protected final ItemInventory itemInventory;

	public ItemInventoryContainer(@Nullable ContainerType<?> containerType, int windowId, PlayerInventory playerInventory, ItemInventory itemInventory)
	{
		super(containerType, windowId, playerInventory);

		this.itemInventory = itemInventory;

		addSlots();
		addPlayerSlots();
		itemInventory.startOpen(playerInventory.player);
	}

	protected void addSlots()
	{
		super.addContainerSlots();
	}

	protected void addPlayerSlots()
	{
		super.addPlayerInventorySlots();
	}

	/**
	 * @deprecated use {@link #addSlots()}
	 */
	@Override @Deprecated
	protected final void addContainerSlots() { }

	/**
	 * @deprecated use {@link #addSlots()}
	 */
	@Override @Deprecated
	protected final void addPlayerInventorySlots() { }

	@Override
	public boolean isPlayerValid(PlayerEntity player)
	{
		return itemInventory.stillValid(player);
	}

	@Override
	public void removed(PlayerEntity player)
	{
		super.removed(player);
		itemInventory.stopOpen(player);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotIndex)
	{
		ItemStack result = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);

		if(slot != null && slot.hasItem())
		{
			ItemStack slotStack = slot.getItem();
			result = slotStack.copy();

			if(slotIndex < itemInventory.getContainerSize())
			{
				if(!moveItemStackTo(slotStack, itemInventory.getContainerSize(), slots.size(), true))
					return ItemStack.EMPTY;
			}
			else if(!moveItemStackTo(slotStack, 0, itemInventory.getContainerSize(), false))
				return ItemStack.EMPTY;

			if(slotStack.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
		}

		return result;
	}
}
