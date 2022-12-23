package xyz.apex.minecraft.apexcore.shared.registry.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ItemEntry<T extends Item> extends ItemLikeEntry<T>
{
    public ItemEntry(ResourceKey<T> registryKey)
    {
        super(Registries.ITEM, registryKey);
    }

    @Override
    public Item asItem()
    {
        return get();
    }
}
