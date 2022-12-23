package xyz.apex.minecraft.apexcore.shared.registry;

import net.minecraft.world.level.block.Block;

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
}
