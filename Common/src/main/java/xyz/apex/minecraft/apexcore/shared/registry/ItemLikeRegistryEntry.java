package xyz.apex.minecraft.apexcore.shared.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public abstract class ItemLikeRegistryEntry<T extends ItemLike> extends BasicRegistryEntry<T> implements ItemLike
{
    public ItemLikeRegistryEntry(ModdedRegistry<? super T> modded, ResourceKey<T> key, Supplier<T> supplier)
    {
        super(modded, key, supplier);
    }

    // conflicts with ItemRegistryEntry, overriding and delegating to super fixes
    public boolean is(Item other)
    {
        // checks only if entry is present (registered)
        return map(value -> value == other).orElse(false);
    }

    public final ItemStack asStack(int count)
    {
        return new ItemStack(this, count);
    }

    public final ItemStack asStack()
    {
        return new ItemStack(this);
    }

    public final boolean isInStack(ItemStack stack)
    {
        return is(stack.getItem());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final boolean hasItemTag(TagKey<Item> tag)
    {
        return modded.hasTag((TagKey) tag, getHolder());
    }
}
