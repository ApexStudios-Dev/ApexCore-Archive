package dev.apexstudios.apexcore.common.inventory;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

public class SimpleItemHandler implements ItemHandler
{
    private final NonNullList<ItemStack> items;
    private final List<IntConsumer> listeners = Lists.newLinkedList();

    public SimpleItemHandler(int size)
    {
        items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public SimpleItemHandler(int expectedSize, CompoundTag tag)
    {
        this(expectedSize);

        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public CompoundTag serializeInto(CompoundTag tag)
    {
        return ContainerHelper.saveAllItems(tag, items, false);
    }

    @Override
    public void addListener(IntConsumer listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeListener(IntConsumer listener)
    {
        listeners.remove(listener);
    }

    @Override
    public int size()
    {
        return items.size();
    }

    @Override
    public ItemStack get(int slotIndex)
    {
        Objects.checkIndex(slotIndex, size());
        return items.get(slotIndex);
    }

    @Override
    public void set(int slotIndex, ItemStack stack)
    {
        Objects.checkIndex(slotIndex, size());
        items.set(slotIndex, stack);
        listeners.forEach(listener -> listener.accept(slotIndex));
    }

    @Override
    public Stream<ItemStack> stream()
    {
        return items.stream();
    }
}
