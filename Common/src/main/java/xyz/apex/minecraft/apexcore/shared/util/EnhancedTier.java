package xyz.apex.minecraft.apexcore.shared.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.ForPlatform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformType;

@ForPlatform(PlatformType.FORGE)
public interface EnhancedTier extends Tier
{
    ResourceLocation getRegistryName();
    @Nullable TagKey<Block> getToolLevelTag();
}
