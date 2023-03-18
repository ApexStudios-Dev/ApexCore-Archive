package xyz.apex.minecraft.apexcore.common.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public interface BlockUtils
{
    static BlockState replaceBlockStateDefinition(Block block)
    {
        var builder = new StateDefinition.Builder<Block, BlockState>(block);
        block.createBlockStateDefinition(builder);
        block.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        var defaultBlockState = block.stateDefinition.any();
        block.registerDefaultState(defaultBlockState);
        return defaultBlockState;
    }
}
