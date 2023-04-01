package xyz.apex.minecraft.apexcore.common.inventory;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Iterator;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.IntStream;

public class Inventory implements Iterable<ItemStack>
{
    private NonNullList<ItemStack> slots;
    private List<IntConsumer> listeners = Lists.newArrayList();
    private boolean listening = true;

    public Inventory(int slotCount)
    {
        slots = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }

    public void addListener(IntConsumer listener)
    {
        listeners.add(listener);
    }

    public void removeListener(IntConsumer listener)
    {
        listeners.remove(listener);
    }

    @NotNull
    @Override
    public Iterator<ItemStack> iterator()
    {
        return slots.iterator();
    }

    public void forEach(ObjIntConsumer<ItemStack> consumer)
    {
        IntStream.range(0, slots.size()).forEach(i -> consumer.accept(slots.get(i), i));
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
        var old = slots.set(slotIndex, stack);
        setChanged(slotIndex);
        return old;
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
            setChanged(slotIndex);
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
            setChanged(slotIndex);
            return existing;
        }
        else
        {
            if(!simulate)
            {
                slots.set(slotIndex, existing.copyWithCount(existing.getCount() - toExtract));
                setChanged(slotIndex);
            }

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
        ignoreChanges();
        slots = NonNullList.withSize(tag.getInt("Size"), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, slots);
        listen();
    }

    public void ignoreChanges()
    {
        listening = false;
    }

    public void listen()
    {
        listening = true;
    }

    public void setChanged(int slotIndex)
    {
        if(listening) listeners.forEach(listener -> listener.accept(slotIndex));
    }

    public static boolean isValidSlotIndex(int slotIndex, int slotCount)
    {
        return slotIndex < slotCount && slotIndex >= 0;
    }

    public static void dropContents(Level level, BlockPos pos, InventoryHolder inventoryHolder)
    {
        var inventory = inventoryHolder.getInventory();
        if(inventory == null) return;

        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();

        for(var stack : inventory)
        {
            Containers.dropItemStack(level, x, y, z, stack);
        }
    }

    public static int getRedstoneSignalFromInventory(InventoryHolder inventoryHolder)
    {
        var inventory = inventoryHolder.getInventory();
        if(inventory == null) return 0;

        var i = 0;
        var f = 0F;

        for(var j = 0; j < inventory.getSize(); j++)
        {
            var stack = inventory.getItem(j);

            if(!stack.isEmpty())
            {
                f += (float) stack.getCount() / (float) Math.min(inventory.getSlotLimit(j), stack.getMaxStackSize());
                i++;
            }
        }

        f /= (float) inventory.getSize();
        return Mth.floor(f * 14F) + (i > 0 ? 1 : 0);
    }
}
