package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Factory interface for constructing new Block entries.
 *
 * @param <T> Type of Block to be constructed.
 */
@FunctionalInterface
public interface BlockFactory<T extends Block>
{
    /**
     * Returns new Block instance with given properties.
     *
     * @param properties Properties for newly constructed Block.
     * @return Newly constructed Block with given properties.
     */
    T create(BlockBehaviour.Properties properties);
}
