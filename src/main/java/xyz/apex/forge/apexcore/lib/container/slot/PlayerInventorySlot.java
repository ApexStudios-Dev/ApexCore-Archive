package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.entity.player.PlayerInventory;

public class PlayerInventorySlot extends BaseSlot
{
	protected final PlayerInventory playerInventory;

	public PlayerInventorySlot(PlayerInventory playerInventory, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(playerInventory, playerInventory.player, slotIndex, slotX, slotY, allowOtherPlayerInteraction);

		this.playerInventory = playerInventory;
	}

	public PlayerInventorySlot(PlayerInventory playerInventory, int slotIndex, int slotX, int slotY)
	{
		this(playerInventory, slotIndex, slotX, slotY, false);
	}

	public PlayerInventory getPlayerInventory()
	{
		return playerInventory;
	}
}
