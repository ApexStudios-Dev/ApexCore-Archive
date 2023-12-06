package dev.apexstudios.apexcore.common.menu;

import net.covers1624.quack.annotation.ReplaceWith;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BaseMenu extends AbstractContainerMenu
{
    protected final int width;
    protected final int height;
    protected final SlotManager slotManager;

    protected BaseMenu(MenuType<? extends BaseMenu> menuType, int windowId, int width, int height)
    {
        super(menuType, windowId);

        this.width = width;
        this.height = height;

        slotManager = new SlotManager(this, this::addSlot);
    }

    public final int width()
    {
        return width;
    }

    public final int height()
    {
        return height;
    }

    @ReplaceWith("SlotManager.handleShiftClick, Slot groups should auto-magically handle this method.")
    @Deprecated
    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex)
    {
        return slotManager.handleShiftClick(player, slotIndex, this::moveItemStackTo);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }

    @ReplaceWith("Use slotManager.addSlot(Slot, String)")
    @Deprecated
    @Override
    protected Slot addSlot(Slot slot)
    {
        return super.addSlot(slot);
    }
}
