package xyz.apex.forge.apexcore.lib.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

@Deprecated(forRemoval = true)
public class BaseSlot extends Slot
{
	protected final Player opener;

	public boolean allowOtherPlayerInteraction;

	public BaseSlot(Container inventory, Player opener, int slotIndex, int slotX, int slotY, boolean allowOtherPlayerInteraction)
	{
		super(inventory, slotIndex, slotX, slotY);

		this.opener = opener;
		this.allowOtherPlayerInteraction = allowOtherPlayerInteraction;
	}

	public BaseSlot(Container inventory, Player opener, int slotIndex, int slotX, int slotY)
	{
		this(inventory, opener, slotIndex, slotX, slotY, false);
	}

	public boolean areOtherPlayersAllowed()
	{
		return allowOtherPlayerInteraction;
	}

	public Player getOpener()
	{
		return opener;
	}

	public boolean isOpener(Player player)
	{
		return opener.getGameProfile().getId().equals(opener.getGameProfile().getId());
	}

	@Override
	public boolean mayPickup(Player player)
	{
		if(!areOtherPlayersAllowed() && !isOpener(player))
			return false;
		return super.mayPickup(player);
	}
}