package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.world.entity.player.Inventory;

@Deprecated(forRemoval = true)
public class PlayerInventorySlot extends BaseSlot
{
	protected final Inventory playerInventory;

	public PlayerInventorySlot(Inventory playerInventory, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(playerInventory, playerInventory.player, slotIndex, slotX, slotY, allowOtherPlayerInteraction);

		this.playerInventory = playerInventory;
	}

	public PlayerInventorySlot(Inventory playerInventory, int slotIndex, int slotX, int slotY)
	{
		this(playerInventory, slotIndex, slotX, slotY, false);
	}

	public Inventory getPlayerInventory()
	{
		return playerInventory;
	}
}