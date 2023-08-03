package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

public final class ItemEntry<T extends Item> extends BaseRegistryEntry<T> implements ItemLikeEntry<T>, FeaturedEntry<T>
{
    @ApiStatus.Internal
    public ItemEntry(AbstractRegistrar<?> registrar, ResourceKey<T> registryKey)
    {
        super(registrar, registryKey);
    }

    public static <T extends Item> ItemEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(ItemEntry.class, registryEntry);
    }
}
