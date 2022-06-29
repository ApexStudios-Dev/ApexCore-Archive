package xyz.apex.forge.apexcore.lib.container;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import xyz.apex.forge.apexcore.lib.container.inventory.ItemInventory;

public class ItemInventoryContainer extends BaseContainer
{
	protected final ItemInventory itemInventory;

	public ItemInventoryContainer(@Nullable MenuType<?> containerType, int windowId, Inventory playerInventory, ItemInventory itemInventory)
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
	public boolean stillValid(Player player)
	{
		return itemInventory.stillValid(player);
	}

	@Override
	public void removed(Player player)
	{
		super.removed(player);
		itemInventory.stopOpen(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex)
	{
		var result = ItemStack.EMPTY;
		var slot = slots.get(slotIndex);

		if(slot != null && slot.hasItem())
		{
			var slotStack = slot.getItem();
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