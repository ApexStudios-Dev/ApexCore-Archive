package xyz.apex.minecraft.apexcore.common.lib.component.item;

import net.minecraft.world.item.Item;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentFactory;

/**
 * Factory used for all item components.
 *
 * @param <T> Item component type.
 */
@FunctionalInterface
public interface ItemComponentFactory<T extends ItemComponent> extends ComponentFactory<Item, ItemComponentHolder, T>
{
}
