package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * Factory interface for constructing new MenuType entries.
 *
 * @param <T> Type of Menu to be constructed.
 */
@FunctionalInterface
public interface MenuFactory<T extends AbstractContainerMenu>
{
    /**
     * Returns new Menu instance with given properties.
     *
     * @param menuType MenuType for the newly constructed Menu.
     * @param syncId ID used when syncing this Menu with clients.
     * @param inventory Player inventory.
     * @param buffer Network buffer for reading additional data from [Must have been written].
     * @return Newly constructed Menu with given properties.
     */
    T create(MenuType<T> menuType, int syncId, Inventory inventory, FriendlyByteBuf buffer);
}
