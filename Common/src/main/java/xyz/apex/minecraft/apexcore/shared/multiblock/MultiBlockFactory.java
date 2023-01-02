package xyz.apex.minecraft.apexcore.shared.multiblock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import xyz.apex.minecraft.apexcore.shared.registry.builder.BlockBuilder;

@FunctionalInterface
public interface MultiBlockFactory<T extends Block & MultiBlock>
{
    T create(MultiBlockType multiBlockType, BlockBehaviour.Properties properties);

    default BlockBuilder.Factory<T> toBlockFactory(MultiBlockType multiBlockType)
    {
        return properties -> create(multiBlockType, properties);
    }
}
