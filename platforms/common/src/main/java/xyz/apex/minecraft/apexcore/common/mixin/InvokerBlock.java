package xyz.apex.minecraft.apexcore.common.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface InvokerBlock
{
    @Invoker("createBlockStateDefinition")
    void ApexCore$createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);

    @Invoker("registerDefaultState")
    void ApexCore$registerDefaultState(BlockState defaultBlockState);
}
