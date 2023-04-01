package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import xyz.apex.minecraft.apexcore.common.mixin.AccessorBlock;
import xyz.apex.minecraft.apexcore.common.mixin.InvokerBlock;

public interface BlockHooks
{
    static BlockState replaceBlockStateDefinition(Block block)
    {
        var builder = new StateDefinition.Builder<Block, BlockState>(block);
        ((InvokerBlock) block).ApexCore$createBlockStateDefinition(builder);
        var stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        ((AccessorBlock) block).ApexCore$setStateDefinition(stateDefinition);
        var defaultBlockState = stateDefinition.any();
        ((InvokerBlock) block).ApexCore$registerDefaultState(defaultBlockState);
        return defaultBlockState;
    }
}
