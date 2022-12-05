package xyz.apex.minecraft.apexcore.shared.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

public interface EnchancedTier extends Tier
{
    @Nullable TagKey<Block> getMiningLevelTag();
}
