package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.world.item.Item;

/**
 * Factory interface for constructing new Item entries.
 *
 * @param <T> Type of Item to be constructed.
 */
@FunctionalInterface
public interface ItemFactory<T extends Item>
{
    /**
     * Returns new Item instance with given properties.
     *
     * @param properties Properties for newly constructed Item.
     * @return Newly constructed Item with given properties.
     */
    T create(Item.Properties properties);
}
