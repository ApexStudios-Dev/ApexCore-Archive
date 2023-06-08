package xyz.apex.minecraft.apexcore.common.lib.registry.factories;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Main factory used to construct new Block instances during registrations.
 *
 * @param <T> Type of block to be constructed.
 */
@FunctionalInterface
public interface BlockFactory<T extends Block>
{
    /**
     * Returns new block instance for given set of properties.
     *
     * @param properties Properties to construct block with.
     * @return New block instance for given set of properties.
     */
    T create(BlockBehaviour.Properties properties);
}
