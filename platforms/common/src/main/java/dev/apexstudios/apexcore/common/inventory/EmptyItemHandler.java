package dev.apexstudios.apexcore.common.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

final class EmptyItemHandler implements ItemHandler
{
    public static final ItemHandler INSTANCE = new EmptyItemHandler();

    private EmptyItemHandler()
    {
    }

    @Override
    public CompoundTag serializeInto(CompoundTag tag)
    {
        return tag;
    }

    @Override
    public void addListener(IntConsumer listener)
    {
    }

    @Override
    public void removeListener(IntConsumer listener)
    {
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public ItemStack get(int slotIndex)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public void set(int slotIndex, ItemStack stack)
    {
    }

    @Override
    public ItemStack extract(int slotIndex, int amount, boolean simulate)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insert(int slotIndex, ItemStack stack, boolean simulate)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public Stream<ItemStack> stream()
    {
        return Stream.empty();
    }

    @Override
    public void forEach(Consumer<ItemStack> consumer)
    {
    }

    @Override
    public void fillStackedContents(StackedContents contents)
    {
    }

    @Override
    public void clearContent()
    {
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }
}
