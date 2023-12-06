package dev.apexstudios.apexcore.common.inventory;

import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemHandlerProvider
{
    @FunctionalInterface
    interface Directional
    {
        OptionalLike<ItemHandler> getItemHandler(@Nullable Direction side);

        default OptionalLike<ItemHandler> getItemHandler()
        {
            return getItemHandler(null);
        }
    }

    @FunctionalInterface
    interface Item
    {
        OptionalLike<ItemHandler> getItemHandler(ItemStack stack);
    }
}
