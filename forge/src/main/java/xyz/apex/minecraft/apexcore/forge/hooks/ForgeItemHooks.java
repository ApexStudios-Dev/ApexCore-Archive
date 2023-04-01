package xyz.apex.minecraft.apexcore.forge.hooks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.hooks.ItemHooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

public final class ForgeItemHooks extends ForgePlatformHolder implements ItemHooks
{
    ForgeItemHooks(ForgePlatform platform)
    {
        super(platform);
    }

    @Nullable
    @Override
    public DyeColor getColorForItem(ItemStack stack)
    {
        return DyeColor.getColor(stack);
    }
}
