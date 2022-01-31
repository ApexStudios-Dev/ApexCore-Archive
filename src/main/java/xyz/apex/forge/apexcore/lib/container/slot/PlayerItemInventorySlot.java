package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import xyz.apex.forge.apexcore.lib.container.inventory.ItemContainer;

public class PlayerItemInventorySlot extends PlayerInventorySlot
{
	protected final ItemContainer itemContainer;

	public PlayerItemInventorySlot(Inventory playerInventory, ItemContainer itemContainer, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(playerInventory, slotIndex, slotX, slotY, allowOtherPlayerInteraction);

		this.itemContainer = itemContainer;
	}

	public PlayerItemInventorySlot(Inventory playerInventory, ItemContainer itemContainer, int slotIndex, int slotX, int slotY)
	{
		this(playerInventory, itemContainer, slotIndex, slotX, slotY, false);
	}

	public ItemContainer getItemContainer()
	{
		return itemContainer;
	}

	public ItemStack getContainerItem()
	{
		return itemContainer.getContainerItem();
	}

	public boolean isContainerItem(ItemStack stack)
	{
		return ItemStack.matches(getContainerItem(), stack);
	}

	public boolean isContainerItem()
	{
		return isContainerItem(getItem());
	}

	@Override
	public boolean mayPickup(Player player)
	{
		if(isContainerItem())
			return false;

		return super.mayPickup(player);
	}
}
