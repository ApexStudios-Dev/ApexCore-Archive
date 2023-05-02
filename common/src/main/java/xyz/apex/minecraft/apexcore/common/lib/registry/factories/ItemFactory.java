package xyz.apex.minecraft.apexcore.common.lib.registry.factories;

import net.minecraft.world.item.Item;

/**
 * Main factory used to construct new Item instances during registrations.
 *
 * @param <T> Type of item to be constructed.
 */
@FunctionalInterface
public interface ItemFactory<T extends Item>
{
    /**
     * Returns new item instance for given set of properties.
     *
     * @param properties Properties to construct item with.
     * @return New item instance for given set of properties.
     */
    T create(Item.Properties properties);
}
