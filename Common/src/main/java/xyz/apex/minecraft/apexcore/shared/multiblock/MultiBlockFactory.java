package xyz.apex.minecraft.apexcore.shared.multiblock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@FunctionalInterface
public interface MultiBlockFactory<T extends Block & MultiBlock>
{
    T create(MultiBlockType multiBlockType, BlockBehaviour.Properties properties);
}
