package xyz.apex.minecraft.apexcore.common.lib.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Extends vanilla Slot to fix not checking if item modification is currently allowed.
 */
public class EnhancedSlot extends Slot
{
    public EnhancedSlot(Container container, int slotIndex, int x, int y)
    {
        super(container, slotIndex, x, y);
    }

    @Override
    public boolean allowModification(Player player)
    {
        return container.stillValid(player) && super.allowModification(player);
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return container.canTakeItem(container, getContainerSlot(), getItem());
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return container.canPlaceItem(getContainerSlot(), stack);
    }
}
