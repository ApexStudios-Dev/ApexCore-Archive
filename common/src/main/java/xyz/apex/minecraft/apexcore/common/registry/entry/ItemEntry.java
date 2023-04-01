package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

public final class ItemEntry<T extends Item> extends RegistryEntry<T> implements ItemLikeEntry<T>, FeatureElementEntry<T>
{
    public ItemEntry(ResourceLocation registryName)
    {
        super(Registries.ITEM, registryName);
    }
}
