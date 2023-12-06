package dev.apexstudios.apexcore.common.menu;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class GhostSlot extends Slot
{
    private ItemStack ghostStack = ItemStack.EMPTY;

    public GhostSlot(int x, int y)
    {
        super(new SimpleContainer(0), -1, x, y);
    }

    public GhostSlot ghosting(ItemStack ghostStack)
    {
        this.ghostStack = ghostStack;
        return this;
    }

    public GhostSlot ghosting(ItemLike item)
    {
        return ghosting(new ItemStack(item));
    }

    @Override
    public ItemStack getItem()
    {
        return ghostStack;
    }

    @Override
    public void set(ItemStack stack)
    {
    }

    @Override
    public int getMaxStackSize()
    {
        return ghostStack.getMaxStackSize();
    }

    @Override
    public ItemStack remove(int amount)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return false;
    }
}
