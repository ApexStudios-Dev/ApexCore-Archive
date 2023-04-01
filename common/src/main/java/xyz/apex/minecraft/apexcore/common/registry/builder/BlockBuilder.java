package xyz.apex.minecraft.apexcore.common.registry.builder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class BlockBuilder<T extends Block> extends AbstractBlockBuilder<T, BlockBuilder<T>>
{
    private final BlockFactory<T> blockFactory;

    private BlockBuilder(String ownerId, String registrationName, BlockFactory<T> blockFactory)
    {
        super(ownerId, registrationName);

        this.blockFactory = blockFactory;
    }

    @Override
    protected T createBlock(BlockBehaviour.Properties properties)
    {
        return blockFactory.create(properties);
    }

    public static <T extends Block> BlockBuilder<T> builder(String ownerId, String registrationName, BlockFactory<T> blockFactory)
    {
        return new BlockBuilder<>(ownerId, registrationName, blockFactory);
    }

    @FunctionalInterface
    public interface BlockFactory<T extends Block>
    {
        T create(BlockBehaviour.Properties properties);
    }
}
