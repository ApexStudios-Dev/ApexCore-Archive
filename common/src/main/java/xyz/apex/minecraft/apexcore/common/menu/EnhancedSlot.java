package xyz.apex.minecraft.apexcore.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

// Exists purely to fix vanilla not checking if item can be inserted into /extracted from container
// player placement directly bypasses any checks the container may have
// to say if item can be inserted into/extracted from
public class EnhancedSlot extends Slot
{
    public EnhancedSlot(Container container, int slotIndex, int x, int y)
    {
        super(container, slotIndex, x, y);
    }

    @Override
    public boolean allowModification(Player player)
    {
        return container.stillValid(player);
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return container.canTakeItem(player.getInventory(), getContainerSlot(), getItem());
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return container.canPlaceItem(getContainerSlot(), stack);
    }
}
