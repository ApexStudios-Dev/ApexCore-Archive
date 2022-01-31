package xyz.apex.forge.apexcore.lib.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import xyz.apex.forge.apexcore.lib.container.slot.PlayerInventorySlot;

import javax.annotation.Nullable;

public class BaseMenu extends AbstractContainerMenu
{
	protected final Player opener;

	protected BaseMenu(@Nullable MenuType<?> containerType, int windowId, Inventory playerInventory)
	{
		super(containerType, windowId);

		opener = playerInventory.player;
		addMenuSlots();
		addPlayerMenuSlots();
	}

	protected void addMenuSlots() { }

	protected void addPlayerMenuSlots()
	{
		// main inventory
		for(var i = 0; i < 3; i++)
		{
			for(var j = 0; j < 9; j++)
			{
				addSlot(createPlayerInventorySlot(j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// hotbar
		for(var k = 0; k < 9; k++)
		{
			addSlot(createPlayerInventorySlot(k, 8 + k * 18, 142));
		}
	}

	protected Slot createPlayerInventorySlot(int slotIndex, int slotX, int slotY)
	{
		return new PlayerInventorySlot(opener.getInventory(), slotIndex, slotX, slotY);
	}

	@Override
	public boolean stillValid(Player player)
	{
		return player.getGameProfile().getId().equals(opener.getGameProfile().getId());
	}
}
