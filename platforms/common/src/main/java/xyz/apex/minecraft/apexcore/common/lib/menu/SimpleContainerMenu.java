package xyz.apex.minecraft.apexcore.common.lib.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/**
 * Basic menu implementation
 */
public abstract class SimpleContainerMenu extends AbstractContainerMenu
{
    public static final int SLOT_SIZE = 18;
    public static final int SLOT_BORDER_OFFSET = 8;

    protected final Container container;

    public SimpleContainerMenu(MenuType<? extends SimpleContainerMenu> menuType, int windowId, Inventory playerInventory, Container container)
    {
        super(menuType, windowId);

        this.container = container;
        bindSlots(playerInventory);
        container.startOpen(playerInventory.player);
    }

    protected abstract void bindSlots(Inventory playerInventory);

    public final Container getContainer()
    {
        return container;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex)
    {
        var stack = ItemStack.EMPTY;
        var slot = slots.get(slotIndex);

        if(slot.hasItem())
        {
            var stack1 = slot.getItem();
            stack = stack1.copy();
            var slotCount = container.getContainerSize();

            if(slotIndex < slotCount)
            {
                if(!moveItemStackTo(stack1, slotCount, slots.size(), true)) return ItemStack.EMPTY;
            }
            else
            {
                if(!moveItemStackTo(stack1, 0, slotIndex, false)) return ItemStack.EMPTY;
            }

            if(stack1.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return stack;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return container.stillValid(player);
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);
        container.stopOpen(player);
    }

    public static void bindContainer(Container container, int rows, int cols, int xStart, int yStart, Consumer<Slot> addSlot)
    {
        bindContainer(container, 0, rows, cols, xStart, yStart, addSlot);
    }

    public static void bindContainer(Container container, int initialSlotIndex, int rows, int cols, int xStart, int yStart, Consumer<Slot> addSlot)
    {
        for(var i = 0; i < rows; i++)
        {
            for(var j = 0; j < cols; j++)
            {
                addSlot.accept(new EnhancedSlot(container, initialSlotIndex + (j + i * cols), xStart + j * SLOT_SIZE, yStart + i * SLOT_SIZE));
            }
        }
    }

    public static void bindPlayerInventory(Inventory playerInventory, Consumer<Slot> addSlot)
    {
        bindPlayerInventory(playerInventory, SLOT_BORDER_OFFSET, 84, addSlot);
    }

    public static void bindPlayerInventory(Inventory playerInventory, int xStart, int yStart, Consumer<Slot> addSlot)
    {
        bindContainer(playerInventory, 9, 3, 9, xStart, yStart, addSlot);

        for(var i = 0; i < 9; i++)
        {
            addSlot.accept(new EnhancedSlot(playerInventory, i, xStart + i * SLOT_SIZE, yStart + 58));
        }
    }
}
