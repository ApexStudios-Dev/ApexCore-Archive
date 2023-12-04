package dev.apexstudios.apexcore.common.inventory;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ItemStackHandlerProvider
{
    @Nullable
    ItemHandler getItemHandler(ItemStack stack);
}
