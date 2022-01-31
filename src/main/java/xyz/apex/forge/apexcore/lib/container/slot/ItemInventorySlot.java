package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import xyz.apex.forge.apexcore.lib.container.inventory.ItemInventory;

public class ItemInventorySlot extends BaseSlot
{
	protected final ItemInventory itemInventory;

	public ItemInventorySlot(ItemInventory itemInventory, Player opener, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(itemInventory, opener, slotIndex, slotX, slotY, allowOtherPlayerInteraction);

		this.itemInventory = itemInventory;
	}

	public ItemInventorySlot(ItemInventory itemInventory, Player opener, int slotIndex, int slotX, int slotY)
	{
		this(itemInventory, opener, slotIndex, slotX, slotY, false);
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

	@Override // disallow placing the container inside itself
	public boolean mayPlace(ItemStack stack)
	{
		return !isContainerItem(stack);
	}
}
