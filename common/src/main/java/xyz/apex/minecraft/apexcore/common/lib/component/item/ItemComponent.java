package xyz.apex.minecraft.apexcore.common.lib.component.item;

import net.minecraft.world.item.Item;
import xyz.apex.minecraft.apexcore.common.lib.component.BaseComponent;

/**
 * Base implementation for item components.
 */
public class ItemComponent extends BaseComponent<Item, ItemComponentHolder>
{
    protected ItemComponent(ItemComponentHolder componentHolder)
    {
        super(componentHolder);
    }
}
