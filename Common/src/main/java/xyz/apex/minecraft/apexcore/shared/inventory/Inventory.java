package xyz.apex.minecraft.apexcore.shared.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class Inventory
{
    private NonNullList<ItemStack> slots;

    public Inventory(int slotCount)
    {
        slots = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }

    public int getSize()
    {
        return slots.size();
    }

    public ItemStack getItem(int slotIndex)
    {
        return isValidSlotIndex(slotIndex, slots.size()) ? slots.get(slotIndex) : ItemStack.EMPTY;
    }

    public ItemStack setItem(int slotIndex, ItemStack stack)
    {
        if(!isValidSlotIndex(slotIndex, slots.size())) return ItemStack.EMPTY;
        if(!isItemValid(slotIndex, stack)) return ItemStack.EMPTY;
        return slots.set(slotIndex, stack);
    }

    public ItemStack insertItem(int slotIndex, ItemStack stack, boolean simulate)
    {
        if(!isValidSlotIndex(slotIndex, slots.size())) return ItemStack.EMPTY;
        if(stack.isEmpty()) return ItemStack.EMPTY;
        if(!isItemValid(slotIndex, stack)) return stack;

        var existing = slots.get(slotIndex);
        var limit = Math.min(getSlotLimit(slotIndex), stack.getMaxStackSize());

        if(!existing.isEmpty())
        {
            if(!ItemStack.isSameItemSameTags(stack, existing)) return stack;
            limit -= existing.getCount();
        }

        if(limit <= 0) return stack;
        var reachedLimit = stack.getCount() > limit;

        if(!simulate)
        {
            if(existing.isEmpty()) slots.set(slotIndex, reachedLimit ? stack.copyWithCount(limit) : stack);
            else existing.grow(reachedLimit ? limit : stack.getCount());
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public ItemStack extractItem(int slotIndex, int amount, boolean simulate)
    {
        if(!isValidSlotIndex(slotIndex, slots.size())) return ItemStack.EMPTY;
        if(amount <= 0) return ItemStack.EMPTY;
        var existing = slots.get(slotIndex);
        if(existing.isEmpty()) return ItemStack.EMPTY;
        var toExtract = Math.min(amount, existing.getMaxStackSize());

        if(existing.getCount() <= toExtract)
        {
            if(simulate) return existing.copy();
            slots.set(slotIndex, ItemStack.EMPTY);
            return existing;
        }
        else
        {
            if(!simulate) slots.set(slotIndex, existing.copyWithCount(existing.getCount() - toExtract));
            return existing.copyWithCount(toExtract);
        }
    }

    public boolean isEmpty()
    {
        if(slots.isEmpty()) return true;
        return slots.stream().allMatch(ItemStack::isEmpty);
    }

    public boolean isItemValid(int slotIndex, ItemStack stack)
    {
        return true;
    }

    public int getSlotLimit(int slotIndex)
    {
        return 64;
    }

    public CompoundTag serialize(CompoundTag tag)
    {
        tag.putInt("Size", slots.size());
        return ContainerHelper.saveAllItems(tag, slots, true);
    }

    public void deserialize(CompoundTag tag)
    {
        slots = NonNullList.withSize(tag.getInt("Size"), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, slots);
    }

    public static boolean isValidSlotIndex(int slotIndex, int slotCount)
    {
        return slotIndex < slotCount && slotIndex >= 0;
    }
}
