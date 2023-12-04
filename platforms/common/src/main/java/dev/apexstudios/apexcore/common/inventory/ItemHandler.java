package dev.apexstudios.apexcore.common.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import org.joml.Math;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface ItemHandler extends StackedContentsCompatible, Clearable
{
    default CompoundTag serialize()
    {
        return serializeInto(new CompoundTag());
    }

    CompoundTag serializeInto(CompoundTag tag);

    void addListener(IntConsumer listener);

    void removeListener(IntConsumer listener);

    int size();

    ItemStack get(int slotIndex);

    void set(int slotIndex, ItemStack stack);

    default int getSlotLimit(int slotIndex)
    {
        return Container.LARGE_MAX_STACK_SIZE;
    }

    default boolean isItemValid(int slotIndex, ItemStack stack)
    {
        return true;
    }

    default ItemStack extract(int slotIndex, int amount, boolean simulate)
    {
        if(amount == 0 || slotIndex < 0 || slotIndex >= size())
            return ItemStack.EMPTY;

        var existing = get(slotIndex);

        if(existing.isEmpty())
            return ItemStack.EMPTY;

        var toExtract = Math.min(amount, existing.getMaxStackSize());

        if(existing.getCount() <= toExtract)
        {
            if(simulate)
                return existing.copy();

            set(slotIndex, ItemStack.EMPTY);
            return existing;
        }

        if(!simulate)
            set(slotIndex, existing.copyWithCount(existing.getCount() - toExtract));

        return existing.copyWithCount(toExtract);
    }

    default ItemStack extract(int slotIndex, int amount)
    {
        return extract(slotIndex, amount, false);
    }

    default ItemStack insert(int slotIndex, ItemStack stack, boolean simulate)
    {
        if(slotIndex < 0 || slotIndex >= size() || stack.isEmpty() || !isItemValid(slotIndex, stack))
            return ItemStack.EMPTY;

        var existing = get(slotIndex);
        var limit = Math.min(getSlotLimit(slotIndex), stack.getMaxStackSize());

        if(!existing.isEmpty())
        {
            if(!ItemStack.isSameItemSameTags(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if(limit <= 0)
            return stack;

        var stackCount = stack.getCount();
        var reachedLimit = stackCount > limit;

        if(!simulate)
        {
            if(existing.isEmpty())
                set(slotIndex, reachedLimit ? stack.copyWithCount(limit) : stack);
            else
                existing.grow(reachedLimit ? limit : stackCount);
        }

        return reachedLimit ? stack.copyWithCount(stackCount - limit) : ItemStack.EMPTY;
    }

    default ItemStack insert(int slotIndex, ItemStack stack)
    {
        return insert(slotIndex, stack, false);
    }

    Stream<ItemStack> stream();

    default void forEach(Consumer<ItemStack> consumer)
    {
        stream().forEach(consumer);
    }

    @Override
    default void fillStackedContents(StackedContents contents)
    {
        forEach(contents::accountStack);
    }

    @Override
    default void clearContent()
    {
        IntStream.range(0, size()).forEach(slotIndex -> set(slotIndex, ItemStack.EMPTY));
    }

    default boolean isEmpty()
    {
        return stream().allMatch(ItemStack::isEmpty);
    }

    static ItemHandler empty()
    {
        return EmptyItemHandler.INSTANCE;
    }
}
