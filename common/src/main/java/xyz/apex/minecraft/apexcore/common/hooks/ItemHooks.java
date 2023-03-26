package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemHooks
{
    @Nullable
    DyeColor getColorForItem(ItemStack stack);

    static ItemHooks getInstance()
    {
        return Hooks.getInstance().item();
    }
}
