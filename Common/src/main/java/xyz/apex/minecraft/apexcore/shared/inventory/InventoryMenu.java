package xyz.apex.minecraft.apexcore.shared.inventory;

import org.apache.commons.lang3.Validate;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class InventoryMenu extends AbstractContainerMenu
{
    protected final BlockPos pos;
    protected final BlockEntity blockEntity;
    protected final Inventory inventory;

    protected InventoryMenu(MenuType<? extends InventoryMenu> menuType, int containerId, Player player, FriendlyByteBuf data)
    {
        super(menuType, containerId);

        pos = data.readBlockPos();
        blockEntity = Objects.requireNonNull(player.level.getBlockEntity(pos));
        Validate.isInstanceOf(InventoryHolder.class, blockEntity);
        var inventoryHolder = (InventoryHolder) blockEntity;
        inventory = inventoryHolder.getInventory();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex)
    {
        if(slots.isEmpty()) return ItemStack.EMPTY;

        var stack = ItemStack.EMPTY;
        var slot = slots.get(slotIndex);

        if(slot.hasItem())
        {
            var stack1 = slot.getItem();
            stack = stack1.copy();

            var itemStart = 0;
            var itemEnd = inventory.getSize() - 1;

            var playerStart = itemEnd + 1;
            var playerEnd = playerStart + (9 * 3) - 1;

            var hotStart = playerEnd + 1;
            var hotEnd = hotStart + 8;

            if(slotIndex >= itemStart && slotIndex <= itemEnd)
            {
                if(!moveItemStackTo(stack1, playerStart, hotEnd, false)) return ItemStack.EMPTY;
            }
            else if(slotIndex >= playerStart && slotIndex <= playerEnd)
            {
                if(!moveItemStackTo(stack1, itemStart, itemEnd, false) && !moveItemStackTo(stack1, hotStart, hotEnd, false)) return ItemStack.EMPTY;
            }
            else if(slotIndex >= hotStart && slotIndex <= hotEnd)
            {
                if(!moveItemStackTo(stack1, itemStart, itemEnd, false) && !moveItemStackTo(stack1, playerStart, playerEnd, false)) return ItemStack.EMPTY;
            }

            if(stack1.isEmpty()) slot.set(ItemStack.EMPTY);
            slot.setChanged();
            if(stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;
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
        blockEntity.setChanged();
    }

    public static void bindInventory(InventoryMenu menu, Inventory inventory, int rows, int cols, int x, int y)
    {
        for(int j = 0; j < rows; j++)
        {
            for(int k = 0; k < cols; k++)
            {
                menu.addSlot(new InventorySlot(inventory, k + j * cols, x + k * 18, y + j * 18));
            }
        }
    }

    public static void bindPlayerInventory(InventoryMenu menu, Player player, int x, int y)
    {
        var playerInventory = player.getInventory();

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                menu.addSlot(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i)
        {
            menu.addSlot(new Slot(playerInventory, i, x + i * 18, y + 58));
        }
    }
}
