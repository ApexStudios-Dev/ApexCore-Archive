package dev.apexstudios.apexcore.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ItemHandlerSlot extends Slot
{
    // TODO: Maybe extract out into more public api
    private static final Container EMPTY = new SimpleContainer(0);

    private final ItemHandler itemHandler;
    private final int slotIndex;

    public ItemHandlerSlot(ItemHandler itemHandler, int slotIndex, int x, int y)
    {
        super(EMPTY, slotIndex, x, y);

        this.itemHandler = itemHandler;
        this.slotIndex = slotIndex;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return itemHandler.isItemValid(slotIndex, stack);
    }

    @Override
    public ItemStack getItem()
    {
        return itemHandler.get(slotIndex);
    }

    @Override
    public void set(ItemStack stack)
    {
        itemHandler.set(slotIndex, stack);
        setChanged();
    }

    @Override
    public int getMaxStackSize()
    {
        return itemHandler.getSlotLimit(slotIndex);
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return !itemHandler.extract(slotIndex, 1, true).isEmpty();
    }

    @Override
    public ItemStack remove(int amount)
    {
        return itemHandler.extract(slotIndex, amount, false);
    }

    public ItemHandler itemHandler()
    {
        return itemHandler;
    }
}
