package xyz.apex.forge.apexcore.lib.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import xyz.apex.forge.apexcore.lib.container.inventory.ItemContainer;

import javax.annotation.Nullable;

public class ItemInventoryMenu extends BaseMenu
{
	protected final ItemContainer itemContainer;

	public ItemInventoryMenu(@Nullable MenuType<?> menuType, int windowId, Inventory playerInventory, ItemContainer itemContainer)
	{
		super(menuType, windowId, playerInventory);

		this.itemContainer = itemContainer;

		addSlots();
		addPlayerSlots();
		itemContainer.startOpen(playerInventory.player);
	}

	protected void addSlots()
	{
		super.addMenuSlots();
	}

	protected void addPlayerSlots()
	{
		super.addPlayerMenuSlots();
	}

	/**
	 * @deprecated use {@link #addSlots()}
	 */
	@Override @Deprecated
	protected final void addMenuSlots() { }

	/**
	 * @deprecated use {@link #addSlots()}
	 */
	@Override @Deprecated
	protected final void addPlayerMenuSlots() { }

	@Override
	public boolean stillValid(Player player)
	{
		return itemContainer.stillValid(player);
	}

	@Override
	public void removed(Player player)
	{
		super.removed(player);
		itemContainer.stopOpen(player);
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

			if(slotIndex < itemContainer.getContainerSize())
			{
				if(!moveItemStackTo(slotStack, itemContainer.getContainerSize(), slots.size(), true))
					return ItemStack.EMPTY;
			}
			else if(!moveItemStackTo(slotStack, 0, itemContainer.getContainerSize(), false))
				return ItemStack.EMPTY;

			if(slotStack.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
		}

		return result;
	}
}
