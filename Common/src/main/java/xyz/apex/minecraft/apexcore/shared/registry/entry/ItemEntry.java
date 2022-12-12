package xyz.apex.minecraft.apexcore.shared.registry.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class ItemEntry<T extends Item> extends ItemLikeEntry<T>
{
    public ItemEntry(ResourceLocation registryName)
    {
        super(Registries.ITEM, registryName);
    }

    @Override
    public Item asItem()
    {
        return get();
    }
}
