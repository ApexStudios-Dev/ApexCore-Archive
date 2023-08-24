package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.container.EmptyContainer;

import java.util.Iterator;
import java.util.stream.IntStream;

public class BaseContainerBlockEntityComponent<T extends BaseContainerBlockEntityComponent<T>> extends BaseBlockEntityComponent implements WorldlyContainer, ContainerData, Iterable<ItemStack>
{
    private int slotCount = 0;
    @Nullable private NonNullList<ItemStack> items;
    @Nullable private int[] defaultSlotsForSide;

    protected BaseContainerBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    @SuppressWarnings("unchecked")
    public T withSlotCount(int slotCount)
    {
        Validate.isTrue(!isRegistered(), "Can only set slot count during registration");

        if(items != null)
            items.clear();

        items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
        defaultSlotsForSide = IntStream.range(0, slotCount).toArray();

        this.slotCount = slotCount;
        return (T) this;
    }

    public final NonNullList<ItemStack> getItems()
    {
        Validate.notNull(items, "Items list is null! Did you register to set slot count using `.withSlotCount`?");
        return items;
    }

    protected void onRemoved(Level level, BlockState newBlockState)
    {
    }

    @MustBeInvokedByOverriders
    @Override
    public void serializeInto(CompoundTag tag, boolean forNetwork)
    {
        ContainerHelper.saveAllItems(tag, getItems());
    }

    @MustBeInvokedByOverriders
    @Override
    public void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
        var items = getItems();
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public final void onRemove(Level level, BlockState newBlockState)
    {
        var blockEntity = getGameObject();
        var blockState = blockEntity.getBlockState();

        if(blockState.is(newBlockState.getBlock()))
        {
            onRemoved(level, newBlockState);
            return;
        }

        var pos = blockEntity.getBlockPos();
        Containers.dropContents(level, pos, this);
        level.updateNeighbourForOutputSignal(pos, blockState.getBlock());

        onRemoved(level, newBlockState);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return defaultSlotsForSide == null ? EmptyContainer.NO_SLOTS : defaultSlotsForSide;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side)
    {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side)
    {
        return true;
    }

    @Override
    public final int getContainerSize()
    {
        return slotCount;
    }

    @Override
    public final boolean isEmpty()
    {
        return getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public final ItemStack getItem(int slot)
    {
        return getItems().get(slot);
    }

    @Override
    public final ItemStack removeItem(int slot, int amount)
    {
        var removed = ContainerHelper.removeItem(getItems(), slot, amount);

        if(!removed.isEmpty())
            setChanged();

        return removed;
    }

    @Override
    public final ItemStack removeItemNoUpdate(int slot)
    {
        return ContainerHelper.takeItem(getItems(), slot);
    }

    @Override
    public final void setItem(int slot, ItemStack stack)
    {
        getItems().set(slot, stack);

        var maxStackSize = getMaxStackSize();

        if(stack.getCount() > maxStackSize)
            stack.setCount(maxStackSize);

        setChanged();
    }

    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(getGameObject(), player);
    }

    @MustBeInvokedByOverriders
    @Override
    public void clearContent()
    {
        getItems().clear();
    }

    @Override
    public int get(int index)
    {
        return 0;
    }

    @Override
    public void set(int index, int value)
    {
    }

    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public Iterator<ItemStack> iterator()
    {
        return getItems().iterator();
    }
}
