package dev.apexstudios.apexcore.common.inventory;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BlockEntityItemHandlerProvider
{
    @Nullable
    ItemHandler getItemHandler(@Nullable Direction side);

    @Nullable
    default ItemHandler getItemHandler()
    {
        return getItemHandler(null);
    }
}
