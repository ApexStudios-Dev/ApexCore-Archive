package xyz.apex.minecraft.apexcore.common.lib.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Basic empty container implementation.
 */
public final class EmptyContainer implements WorldlyContainer, ContainerData
{
    public static final EmptyContainer INSTANCE = new EmptyContainer();
    public static final int[] NO_SLOTS = new int[0];

    private EmptyContainer()
    {
    }

    @Override
    public int getContainerSize()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public ItemStack getItem(int slot)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
    }

    @Override
    public void setChanged()
    {
    }

    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }

    @Override
    public void clearContent()
    {
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean canTakeItem(Container target, int index, ItemStack stack)
    {
        return false;
    }

    @Override
    public int countItem(Item item)
    {
        return 0;
    }

    @Override
    public boolean hasAnyOf(Set<Item> set)
    {
        return false;
    }

    @Override
    public boolean hasAnyMatching(Predicate<ItemStack> predicate)
    {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return NO_SLOTS;
    }

    @Override
    public int getMaxStackSize()
    {
        return 0;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction)
    {
        return false;
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
}
