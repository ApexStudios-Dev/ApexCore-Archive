package xyz.apex.forge.apexcore.lib.container;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import xyz.apex.forge.apexcore.lib.block.MultiBlockPattern;
import xyz.apex.forge.apexcore.lib.block.entity.InventoryBlockEntity;
import xyz.apex.forge.apexcore.lib.net.SyncContainerPacket;
import xyz.apex.java.utility.api.function.QuadFunction;

public class BaseMenu extends AbstractContainerMenu
{
	public final Player player;
	public final BlockPos pos;

	public BaseMenu(@Nullable MenuType<? extends BaseMenu> menuType, int windowId, Inventory playerInventory, FriendlyByteBuf buffer)
	{
		super(menuType, windowId);

		player = playerInventory.player;

		pos = buffer.readBlockPos();

	}

	@Nullable
	protected IItemHandler getItemHandler()
	{
		return null;
	}

	protected void onInventoryChanges()
	{
	}

	public final void setBlockEntityChanged()
	{
		var blockEntity = player.level.getBlockEntity(pos);

		if(blockEntity != null)
			blockEntity.setChanged();
		if(blockEntity instanceof InventoryBlockEntity inventoryBlockEntity)
			SyncContainerPacket.sendToClient(inventoryBlockEntity);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex)
	{
		if(slots.isEmpty())
			return ItemStack.EMPTY;

		var stack = ItemStack.EMPTY;
		var slot = slots.get(slotIndex);

		if(slot.hasItem())
		{
			var stack1 = slot.getItem();
			stack = stack1.copy();
			IItemHandler itemHandler = getItemHandler();

			if(itemHandler != null && itemHandler.getSlots() > 0)
			{
				var itemStart = 0;
				var itemEnd = itemHandler.getSlots() - 1;

				var playerStart = itemEnd + 1;
				var playerEnd = playerStart + (9 * 3) - 1;

				var hotStart = playerEnd + 1;
				var hotEnd = hotStart + 8;

				if(slotIndex >= itemStart && slotIndex <= itemEnd)
				{
					if(!moveItemStackTo(stack1, playerStart, hotEnd, false))
						return ItemStack.EMPTY;
				}
				else if(slotIndex >= playerStart && slotIndex <= playerEnd)
				{
					if(!moveItemStackTo(stack1, itemStart, itemEnd, false) && !moveItemStackTo(stack1, hotStart, hotEnd, false))
						return ItemStack.EMPTY;
				}
				else if(slotIndex >= hotStart && slotIndex <= hotEnd)
				{
					if(!moveItemStackTo(stack1, itemStart, itemEnd, false) && !moveItemStackTo(stack1, playerStart, playerEnd, false))
						return ItemStack.EMPTY;
				}
			}
			else
			{
				var playerStart = 0;
				var playerEnd = playerStart + (9 * 3) - 1;

				var hotStart = playerEnd + 1;
				var hotEnd = hotStart + 8;

				if(slotIndex >= playerStart && slotIndex <= playerEnd)
				{
					if(!moveItemStackTo(stack1, hotStart, hotEnd, false))
						return ItemStack.EMPTY;
				}
				else if(slotIndex >= hotStart && slotIndex <= hotEnd)
				{
					if(!moveItemStackTo(stack1, playerStart, playerEnd, false))
						return ItemStack.EMPTY;
				}
			}

			if(stack1.isEmpty())
				slot.set(ItemStack.EMPTY);

			slot.setChanged();

			if(stack1.getCount() == stack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, stack1);
			broadcastChanges();
		}

		return stack;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return player.isAlive() && player.containerMenu == this;
	}

	@Override
	public void broadcastChanges()
	{
		super.broadcastChanges();
		onInventoryChanges();
	}

	protected static LazyOptional<IItemHandler> getItemHandlerFromBlockEntity(BlockEntity blockEntity, @Nullable Direction side)
	{
		return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
	}

	protected static LazyOptional<IItemHandler> getItemHandlerFromBlockEntity(BlockEntity blockEntity)
	{
		return getItemHandlerFromBlockEntity(blockEntity, null);
	}

	public static void bindItemHandlerSlots(BaseMenu menu, IItemHandler itemHandler, int rows, int cols, int x, int y, MultiBlockPattern.QuadFunction<IItemHandler, Integer, Integer, Integer, SlotItemHandler> slotFactory)
	{
		for(var j = 0; j < rows; j++)
		{
			for(var k = 0; k < cols; k++)
			{
				menu.addSlot(slotFactory.apply(itemHandler, k + j * cols, x + k * 18, y + j * 18));
			}
		}
	}

	public static void bindItemHandlerSlots(BaseMenu menu, IItemHandler itemHandler, int rows, int cols, int x, int y)
	{
		bindItemHandlerSlots(menu, itemHandler, rows, cols, x, y, SlotItemHandler::new);
	}

	public static void bindPlayerInventory(BaseMenu menu)
	{
		bindPlayerInventory(menu, Slot::new);
	}

	public static void bindPlayerInventory(BaseMenu menu, int x, int y)
	{
		bindPlayerInventory(menu, x, y, Slot::new);
	}

	public static void bindPlayerInventory(BaseMenu menu, QuadFunction<Container, Integer, Integer, Integer, Slot> factory)
	{
		bindPlayerInventory(menu, 8, 84, factory);
	}

	public static void bindPlayerInventory(BaseMenu menu, int x, int y, QuadFunction<Container, Integer, Integer, Integer, Slot> factory)
	{
		var playerInventory = menu.player.getInventory();

		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				menu.addSlot(factory.apply(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}

		for(int i = 0; i < 9; ++i)
		{
			menu.addSlot(factory.apply(playerInventory, i, x + i * 18, y + 58));
		}
	}
}