package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class BaseSlot extends Slot
{
	protected final PlayerEntity opener;

	public boolean allowOtherPlayerInteraction;

	public BaseSlot(IInventory inventory, PlayerEntity opener, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(inventory, slotIndex, slotX, slotY);

		this.opener = opener;
		this.allowOtherPlayerInteraction = allowOtherPlayerInteraction;
	}

	public BaseSlot(IInventory inventory, PlayerEntity opener, int slotIndex, int slotX, int slotY)
	{
		this(inventory, opener, slotIndex, slotX, slotY, false);
	}

	public boolean areOtherPlayersAllowed()
	{
		return allowOtherPlayerInteraction;
	}

	public PlayerEntity getOpener()
	{
		return opener;
	}

	public boolean isOpener(PlayerEntity player)
	{
		return opener.getGameProfile().getId().equals(opener.getGameProfile().getId());
	}

	@Override
	public boolean mayPickup(PlayerEntity player)
	{
		if(!areOtherPlayersAllowed() && !isOpener(player))
			return false;
		return super.mayPickup(player);
	}
}
