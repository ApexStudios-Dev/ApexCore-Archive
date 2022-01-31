package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import xyz.apex.forge.apexcore.lib.container.inventory.ItemContainer;

public class ItemInventorySlot extends BaseSlot
{
	protected final ItemContainer itemContainer;

	public ItemInventorySlot(ItemContainer itemContainer, Player opener, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(itemContainer, opener, slotIndex, slotX, slotY, allowOtherPlayerInteraction);

		this.itemContainer = itemContainer;
	}

	public ItemInventorySlot(ItemContainer itemContainer, Player opener, int slotIndex, int slotX, int slotY)
	{
		this(itemContainer, opener, slotIndex, slotX, slotY, false);
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

	@Override // disallow placing the container inside itself
	public boolean mayPlace(ItemStack stack)
	{
		return !isContainerItem(stack);
	}
}
