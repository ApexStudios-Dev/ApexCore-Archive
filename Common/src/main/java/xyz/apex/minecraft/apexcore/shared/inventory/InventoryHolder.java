package xyz.apex.minecraft.apexcore.shared.inventory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;

public interface InventoryHolder
{
    @Nullable Inventory getInventory(@Nullable Direction side);

    @Nullable
    default Inventory getInventory()
    {
        return getInventory(null);
    }
}
