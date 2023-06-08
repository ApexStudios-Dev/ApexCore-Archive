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

    /**
     * Binds container slots to menu.
     *
     * @param container Container to be bound.
     * @param rows      Number of rows in container.
     * @param cols      Number of cols in container.
     * @param xStart    X start pos.
     * @param yStart    Y start pos.
     * @param addSlot   Consumer used to bind slots.
     */
    public static void bindContainer(Container container, int rows, int cols, int xStart, int yStart, Consumer<Slot> addSlot)
    {
        for(var j = 0; j < rows; j++)
        {
            for(var k = 0; k < cols; k++)
            {
                addSlot.accept(new EnhancedSlot(container, k + j * cols, xStart + k * 18, yStart + j * 18));
            }
        }
    }

    /**
     * Binds player inventory slots to menu.
     *
     * @param playerInventory Player inventory to be bound.
     * @param xStart          X start pos.
     * @param yStart          Y start pos.
     * @param addSlot         Consumer used to bind slots.
     */
    public static void bindPlayerInventory(Inventory playerInventory, int xStart, int yStart, Consumer<Slot> addSlot)
    {
        for(var i = 0; i < 3; i++)
        {
            for(var j = 0; j < 9; j++)
            {
                addSlot.accept(new EnhancedSlot(playerInventory, j + i * 9 + 9, xStart + j * 18, yStart + i * 18));
            }
        }

        for(var i = 0; i < 9; i++)
        {
            addSlot.accept(new EnhancedSlot(playerInventory, i, xStart + i * 18, yStart + 58));
        }
    }
}
