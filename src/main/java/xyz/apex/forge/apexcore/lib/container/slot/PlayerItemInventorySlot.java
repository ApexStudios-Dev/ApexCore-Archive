package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import xyz.apex.forge.apexcore.lib.container.inventory.ItemInventory;

public class PlayerItemInventorySlot extends PlayerInventorySlot
{
	protected final ItemInventory itemInventory;

	public PlayerItemInventorySlot(Inventory playerInventory, ItemInventory itemInventory, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(playerInventory, slotIndex, slotX, slotY, allowOtherPlayerInteraction);

		this.itemInventory = itemInventory;
	}

	public PlayerItemInventorySlot(Inventory playerInventory, ItemInventory itemInventory, int slotIndex, int slotX, int slotY)
	{
		this(playerInventory, itemInventory, slotIndex, slotX, slotY, false);
	}

	public ItemInventory getItemInventory()
	{
		return itemInventory;
	}

	public ItemStack getContainerItem()
	{
		return itemInventory.getContainerItem();
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
