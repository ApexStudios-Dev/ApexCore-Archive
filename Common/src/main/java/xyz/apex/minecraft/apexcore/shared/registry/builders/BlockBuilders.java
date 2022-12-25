package xyz.apex.minecraft.apexcore.shared.registry.builders;

import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlock;
import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlockFactory;
import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlockType;
import xyz.apex.minecraft.apexcore.shared.multiblock.SimpleMultiBlock;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntry;

public interface BlockBuilders
{
    static <T extends Block> BlockBuilder<T> builder(String modId, String registryName, BlockBuilder.BlockFactory<T> factory)
    {
        return new BlockBuilder<>(modId, registryName, factory);
    }

    static BlockBuilder<Block> builder(String modId, String registryName)
    {
        return builder(modId, registryName, Block::new);
    }

    static <T extends Block> BlockEntry<T> simple(String modId, String registryName, BlockBuilder.BlockFactory<T> factory)
    {
        return builder(modId, registryName, factory).register();
    }

    static BlockEntry<Block> simple(String modId, String registryName)
    {
        return builder(modId, registryName, Block::new).register();
    }

    // region: MultiBlock
    static <T extends Block & MultiBlock> BlockBuilder<T> multiBlock(String modId, String registryName, MultiBlockType multiBlockType, MultiBlockFactory<T> factory)
    {
        return builder(modId, registryName, properties -> factory.create(multiBlockType, properties));
    }

    static BlockBuilder<SimpleMultiBlock> multiBlock(String modId, String registryName, MultiBlockType multiBlockType)
    {
        return multiBlock(modId, registryName, multiBlockType, SimpleMultiBlock::new);
    }

    static <T extends Block & MultiBlock> BlockEntry<T> simpleMultiBlock(String modId, String registryName, MultiBlockType multiBlockType, MultiBlockFactory<T> factory)
    {
        return multiBlock(modId, registryName, multiBlockType, factory).register();
    }

    static BlockEntry<SimpleMultiBlock> simpleMultiBlock(String modId, String registryName, MultiBlockType multiBlockType)
    {
        return multiBlock(modId, registryName, multiBlockType, SimpleMultiBlock::new).register();
    }
    // endregion
}
