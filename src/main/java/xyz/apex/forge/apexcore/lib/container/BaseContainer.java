package xyz.apex.forge.apexcore.lib.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

import xyz.apex.forge.apexcore.lib.container.slot.PlayerInventorySlot;

import javax.annotation.Nullable;

public class BaseContainer extends Container
{
	protected final PlayerEntity opener;

	protected BaseContainer(@Nullable ContainerType<?> containerType, int windowId, PlayerInventory playerInventory)
	{
		super(containerType, windowId);

		opener = playerInventory.player;
		addContainerSlots();
		addPlayerInventorySlots();
	}

	protected void addContainerSlots() { }

	protected void addPlayerInventorySlots()
	{
		// main inventory
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				addSlot(createPlayerInventorySlot(j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// hotbar
		for(int k = 0; k < 9; k++)
		{
			addSlot(createPlayerInventorySlot(k, 8 + k * 18, 142));
		}
	}

	protected Slot createPlayerInventorySlot(int slotIndex, int slotX, int slotY)
	{
		return new PlayerInventorySlot(opener.inventory, slotIndex, slotX, slotY);
	}

	protected boolean isPlayerValid(PlayerEntity player)
	{
		return player.getGameProfile().getId().equals(opener.getGameProfile().getId());
	}

	// weird java compile error
	// is causing me to have to move this
	// apparently it always thinks this method is abstract
	// and thus cannot be accessed via super
	@Override
	public final boolean stillValid(PlayerEntity player)
	{
		return isPlayerValid(player);
	}
}
