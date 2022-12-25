package xyz.apex.minecraft.apexcore.shared.mixin.invokers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

@Mixin(Block.class)
public interface BlockInvoker
{
    @Invoker("createBlockStateDefinition") void ApexCore$createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);
    @Invoker("registerDefaultState") void ApexCore$registerDefaultState(BlockState defaultBlockState);
}
