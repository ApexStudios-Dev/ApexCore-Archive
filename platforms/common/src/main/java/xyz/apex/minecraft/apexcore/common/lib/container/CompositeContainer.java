package xyz.apex.minecraft.apexcore.common.lib.container;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public final class CompositeContainer implements WorldlyContainer, ContainerData
{
    private final List<Container> containers;

    // slot logic pulled directly from NeoForge's CombinedInvWrapper
    // https://github.com/neoforged/NeoForge/blob/eb25ba6224acf22c58869e852f77578817f42b82/src/main/java/net/minecraftforge/items/wrapper/CombinedInvWrapper.java#L16-L74
    private final int[] baseSlotIndices;
    private final int[] baseDataIndices;
    private final int slotCount;
    private final int dataCount;

    public CompositeContainer(Container... containers)
    {
        this.containers = List.of(containers);

        baseSlotIndices = new int[containers.length];
        baseDataIndices = new int[containers.length];

        var slotIndex = 0;
        var dataIndex = 0;

        for(var i = 0; i < containers.length; i++)
        {
            slotIndex += containers[i].getContainerSize();
            baseSlotIndices[i] = slotIndex;
            baseDataIndices[i] = 0;

            if(containers[i] instanceof ContainerData data)
            {
                dataIndex += data.getCount();
                baseDataIndices[i] = dataIndex;
            }
        }

        slotCount = slotIndex;
        dataCount = dataIndex;
    }

    public Container getContainer(int containerIndex)
    {
        return containerIndex < 0 || containerIndex >= containers.size() ? EmptyContainer.INSTANCE : containers.get(containerIndex);
    }

    public ContainerData getContainerData(int containerDataIndex)
    {
        if(containerDataIndex < 0 || containerDataIndex >= containers.size())
            return EmptyContainer.INSTANCE;
        return containers.get(containerDataIndex) instanceof ContainerData containerData ? containerData : EmptyContainer.INSTANCE;
    }

    public int getContainerCount()
    {
        return containers.size();
    }

    public boolean contains(Container other)
    {
        return containers.stream().anyMatch(container -> container == other);
    }

    @Override
    public int getContainerSize()
    {
        return slotCount;
    }

    @Override
    public boolean isEmpty()
    {
        return containers.stream().allMatch(Container::isEmpty);
    }

    @Override
    public ItemStack getItem(int globalSlot)
    {
        return withContainer(globalSlot, Container::getItem);
    }

    @Override
    public ItemStack removeItem(int globalSlot, int amount)
    {
        return withContainer(globalSlot, (container, slotIndex) -> container.removeItem(slotIndex, amount));
    }

    @Override
    public ItemStack removeItemNoUpdate(int globalSlot)
    {
        return withContainer(globalSlot, Container::removeItemNoUpdate);
    }

    @Override
    public void setItem(int globalSlot, ItemStack stack)
    {
        asContainer(globalSlot, (container, slotIndex) -> container.setItem(slotIndex, stack));
    }

    @Override
    public int getMaxStackSize()
    {
        return LARGE_MAX_STACK_SIZE;
    }

    @Override
    public void setChanged()
    {
        containers.forEach(Container::setChanged);
    }

    @Override
    public void startOpen(Player player)
    {
        containers.forEach(container -> container.startOpen(player));
    }

    @Override
    public void stopOpen(Player player)
    {
        containers.forEach(container -> container.stopOpen(player));
    }

    @Override
    public boolean canPlaceItem(int globalSlot, ItemStack stack)
    {
        return withContainer(globalSlot, (container, slotIndex) -> container.canPlaceItem(slotIndex, stack));
    }

    @Override
    public boolean canTakeItem(Container target, int globalSlot, ItemStack stack)
    {
        return withContainer(globalSlot, (container, slotIndex) -> container.canTakeItem(target, slotIndex, stack));
    }

    @Override
    public void clearContent()
    {
        containers.forEach(Clearable::clearContent);
    }

    @Override
    public int countItem(Item item)
    {
        return containers.stream().mapToInt(container -> container.countItem(item)).sum();
    }

    @Override
    public boolean hasAnyOf(Set<Item> set)
    {
        return containers.stream().anyMatch(container -> container.hasAnyOf(set));
    }

    @Override
    public boolean hasAnyMatching(Predicate<ItemStack> predicate)
    {
        return containers.stream().anyMatch(container -> container.hasAnyMatching(predicate));
    }

    @Override
    public boolean stillValid(Player player)
    {
        return containers.stream().allMatch(container -> container.stillValid(player));
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        var slotsForSide = new IntOpenHashSet();

        for(var containerIndex = 0; containerIndex < baseSlotIndices.length; containerIndex++)
        {
            if(containers.get(containerIndex) instanceof WorldlyContainer worldy)
            {
                for(var slotIndex : worldy.getSlotsForFace(side))
                {
                    slotsForSide.add(getGlobalSlotForContainerSlotIndex(slotIndex, containerIndex));
                }
            }
        }

        return slotsForSide.toIntArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int globalSlot, ItemStack stack, @Nullable Direction side)
    {
        return withContainer(globalSlot, (container, slotIndex) -> container instanceof WorldlyContainer worldly ? worldly.canPlaceItemThroughFace(slotIndex, stack, side) : container.canPlaceItem(slotIndex, stack));
    }

    @Override
    public boolean canTakeItemThroughFace(int globalSlot, ItemStack stack, Direction side)
    {
        return withContainer(globalSlot, (container, slotIndex) -> {
            if(container instanceof WorldlyContainer worldly)
                return worldly.canTakeItemThroughFace(slotIndex, stack, side);

            try
            {
                // takes target container as a none null argument
                // not the best solution, but we do need to know if
                // container allows items to be extracted from
                //
                // hoppers are only thing which call this method in vanilla
                // and jukeboxes / chiseled bookshelves are only things which implement it
                // our methods should never need the target argument, so we should be good to
                // pass null so long as we take care as to what containers the CompositeContainer is wrapping
                return container.canTakeItem(null, slotIndex, stack);
            }
            catch(NullPointerException ignored)
            {
                return false;
            }
        });
    }

    @Override
    public int get(int globalData)
    {
        return withContainerData(globalData, ContainerData::get);
    }

    @Override
    public void set(int globalData, int value)
    {
        asContainerData(globalData, (containerData, dataIndex) -> containerData.set(dataIndex, value));
    }

    @Override
    public int getCount()
    {
        return dataCount;
    }

    private void asContainer(int globalSlot, BiConsumer<Container, Integer> consumer)
    {
        var containerIndex = getContainerIndexForGlobalSlot(globalSlot);
        var container = getContainer(containerIndex);
        var slotIndex = getSlotIndexForGlobalSlot(globalSlot, containerIndex);

        consumer.accept(container, slotIndex);
    }

    private void asContainerData(int globalData, BiConsumer<ContainerData, Integer> consumer)
    {
        var containerDataIndex = getContainerDataIndexForGlobalData(globalData);
        var containerData = getContainerData(containerDataIndex);
        var dataIndex = getDataIndexForGlobalData(globalData, containerDataIndex);

        consumer.accept(containerData, dataIndex);
    }

    private <T> T withContainer(int globalSlot, BiFunction<Container, Integer, T> mapper)
    {
        var containerIndex = getContainerIndexForGlobalSlot(globalSlot);
        var container = getContainer(containerIndex);
        var slotIndex = getSlotIndexForGlobalSlot(globalSlot, containerIndex);

        return mapper.apply(container, slotIndex);
    }

    private <T> T withContainerData(int globalData, BiFunction<ContainerData, Integer, T> mapper)
    {
        var containerDataIndex = getContainerDataIndexForGlobalData(globalData);
        var containerData = getContainerData(containerDataIndex);
        var dataIndex = getDataIndexForGlobalData(globalData, containerDataIndex);

        return mapper.apply(containerData, dataIndex);
    }

    private int getContainerIndexForGlobalSlot(int globalSlot)
    {
        if(globalSlot < 0)
            return -1;

        for(var containerIndex = 0; containerIndex < baseSlotIndices.length; containerIndex++)
        {
            if(globalSlot - baseSlotIndices[containerIndex] < 0)
                return containerIndex;
        }

        return -1;
    }

    private int getContainerDataIndexForGlobalData(int globalData)
    {
        if(globalData < 0)
            return -1;

        for(var dataIndex = 0; dataIndex < baseDataIndices.length; dataIndex++)
        {
            if(globalData - baseDataIndices[dataIndex] < 0)
                return dataIndex;
        }

        return -1;
    }

    private int getSlotIndexForGlobalSlot(int globalSlot, int containerIndex)
    {
        if(containerIndex <= 0 || containerIndex >= baseSlotIndices.length)
            return globalSlot;

        return globalSlot - baseSlotIndices[containerIndex - 1];
    }

    private int getDataIndexForGlobalData(int globalData, int containerDataIndex)
    {
        if(containerDataIndex <= 0 || containerDataIndex >= baseDataIndices.length)
            return globalData;

        return globalData - baseDataIndices[containerDataIndex - 1];
    }

    private int getGlobalSlotForContainerSlotIndex(int slotIndex, int containerIndex)
    {
        return Math.abs(slotIndex - (baseSlotIndices[containerIndex] - 1));
    }

    private int getGlobalDataForContainerDataIndex(int dataIndex, int containerDataIndex)
    {
        return Math.abs(dataIndex - (baseDataIndices[containerDataIndex] - 1));
    }
}
