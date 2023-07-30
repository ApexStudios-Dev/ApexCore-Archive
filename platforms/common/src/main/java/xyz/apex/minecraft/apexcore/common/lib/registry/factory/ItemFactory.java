package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemFactory<T extends Item>
{
    T create(Item.Properties properties);
}
