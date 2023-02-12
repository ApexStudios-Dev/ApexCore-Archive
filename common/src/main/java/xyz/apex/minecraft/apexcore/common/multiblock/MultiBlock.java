package xyz.apex.minecraft.apexcore.common.multiblock;

import net.minecraft.world.level.block.state.BlockState;

public interface MultiBlock
{
    MultiBlockType getMultiBlockType();
    boolean isSameBlockTypeForMultiBlock(BlockState blockState);
}