package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Factory interface for constructing new BlockItem entries.
 *
 * @param <B> Type of Block to register this BlockItem for.
 * @param <I> Type of BlockItem to be constructed.
 */
@FunctionalInterface
public interface BlockItemFactory<I extends Item, B extends Block>
{
    /**
     * Returns new BlockItem instance with given properties.
     *
     * @param block Block this BlockItem will be tied to.
     * @param properties Properties for newly constructed BlockItem.
     * @return Newly constructed BlockItem with given properties.
     */
    I create(B block, Item.Properties properties);
}
