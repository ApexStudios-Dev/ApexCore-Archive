package xyz.apex.minecraft.apexcore.common.inventory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import xyz.apex.minecraft.apexcore.common.component.components.BlockEntityComponent;

public interface InventoryHolder
{
    Inventory getInventory();

    // Use this method rather than looking up yourself
    // its not as simple as just BlockEntity instanceof check
    // since MultiBlock types only create block entities at the origin point
    // we need to delegate those blocks off to the origin point
    // since no block entity will exist at those locations to instance check against
    @Nullable
    static Inventory lookupInventory(@Nullable BlockGetter level, BlockPos pos)
    {
        if(level == null) return null;
        return BlockEntityComponent
                .lookupBlockEntity(level, pos)
                .filter(InventoryHolder.class::isInstance)
                .map(InventoryHolder.class::cast)
                .map(InventoryHolder::getInventory)
                .orElse(null)
        ;
    }
}
