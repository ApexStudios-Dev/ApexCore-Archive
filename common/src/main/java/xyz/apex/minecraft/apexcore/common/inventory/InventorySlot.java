package xyz.apex.minecraft.apexcore.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class InventorySlot extends Slot
{
    private static final Container EMPTY = new SimpleContainer(0);

    protected final Inventory inventory;

    public InventorySlot(Inventory inventory, int index, int x, int y)
    {
        super(EMPTY, index, x, y);

        this.inventory = inventory;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return inventory.isItemValid(index, stack);
    }

    @Override
    public ItemStack getItem()
    {
        return inventory.getItem(index);
    }

    @Override
    public void set(ItemStack stack)
    {
        inventory.setItem(index, stack);
        setChanged();
    }

    @Override
    public void setChanged()
    {
        inventory.setChanged(index);
    }

    @Override
    public int getMaxStackSize()
    {
        return inventory.getSlotLimit(index);
    }

    @Override
    public int getMaxStackSize(ItemStack stack)
    {
        var maxAdd = stack.copy();
        var maxInput = stack.getMaxStackSize();
        maxAdd.setCount(maxInput);
        var currentStack = inventory.getItem(index);
        inventory.setItem(index, ItemStack.EMPTY);
        var remainder = inventory.insertItem(index, maxAdd, true);
        inventory.setItem(index, currentStack);
        return maxInput - remainder.getCount();
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return !inventory.extractItem(index, 1, true).isEmpty();
    }

    @Override
    public ItemStack remove(int amount)
    {
        return inventory.extractItem(index, amount, false);
    }
}
