package xyz.apex.minecraft.apexcore.fabric.hooks;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.hooks.ItemHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

public final class FabricItemHooks extends FabricPlatformHolder implements ItemHooks
{
    FabricItemHooks(FabricPlatform platform)
    {
        super(platform);
    }

    @Nullable
    @Override
    public DyeColor getColorForItem(ItemStack stack)
    {
        if(stack.getItem() instanceof DyeItem dye) return dye.getDyeColor();

        if(stack.is(ConventionalItemTags.BLACK_DYES)) return DyeColor.BLACK;
        if(stack.is(ConventionalItemTags.BLUE_DYES)) return DyeColor.BLUE;
        if(stack.is(ConventionalItemTags.BROWN_DYES)) return DyeColor.BROWN;
        if(stack.is(ConventionalItemTags.CYAN_DYES)) return DyeColor.CYAN;
        if(stack.is(ConventionalItemTags.GRAY_DYES)) return DyeColor.GRAY;
        if(stack.is(ConventionalItemTags.GREEN_DYES)) return DyeColor.GREEN;
        if(stack.is(ConventionalItemTags.LIGHT_BLUE_DYES)) return DyeColor.LIGHT_BLUE;
        if(stack.is(ConventionalItemTags.LIGHT_GRAY_DYES)) return DyeColor.LIGHT_GRAY;
        if(stack.is(ConventionalItemTags.LIME_DYES)) return DyeColor.LIME;
        if(stack.is(ConventionalItemTags.MAGENTA_DYES)) return DyeColor.MAGENTA;
        if(stack.is(ConventionalItemTags.ORANGE_DYES)) return DyeColor.ORANGE;
        if(stack.is(ConventionalItemTags.PINK_DYES)) return DyeColor.PINK;
        if(stack.is(ConventionalItemTags.PURPLE_DYES)) return DyeColor.PURPLE;
        if(stack.is(ConventionalItemTags.RED_DYES)) return DyeColor.RED;
        if(stack.is(ConventionalItemTags.WHITE_DYES)) return DyeColor.WHITE;
        if(stack.is(ConventionalItemTags.YELLOW_DYES)) return DyeColor.YELLOW;

        return null;
    }
}
