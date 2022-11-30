package xyz.apex.minecraft.apexcore.shared.registry.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;

import java.util.function.Supplier;

public final class ItemRegistryEntry<T extends Item> extends ItemLikeRegistryEntry<T>
{
    public ItemRegistryEntry(ModdedRegistry<? super T> modded, ResourceKey<T> key, Supplier<T> supplier)
    {
        super(modded, key, supplier);
    }

    @Override
    public boolean is(Item other)
    {
        return super.is(other);
    }

    @NotNull
    @Override
    public Item asItem()
    {
        return get();
    }
}
