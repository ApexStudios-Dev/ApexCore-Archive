package xyz.apex.minecraft.apexcore.shared.registry.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public class ItemLikeEntry<T extends ItemLike> extends RegistryEntry<T> implements ItemLike
{
    protected ItemLikeEntry(ResourceKey<? extends Registry<? super T>> registryType, ResourceKey<T> registryKey)
    {
        super(registryType, registryKey);
    }

    @Override
    public Item asItem()
    {
        return get().asItem();
    }

    public final boolean isItem(@Nullable Item item)
    {
        return isPresent() && Objects.equals(asItem(), item);
    }

    public final ItemStack asStack(int count)
    {
        if(count <= 0) return ItemStack.EMPTY;
        return new ItemStack(this, count);
    }

    public final ItemStack asStack()
    {
        return asStack(1);
    }

    public final boolean isInItemStack(ItemStack stack)
    {
        return stack.is(asItem());
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public final boolean hasItemTag(TagKey<Item> tag)
    {
        if(isFor(Registries.ITEM)) return ((Holder<Item>) getHolder()).is(tag);
        return asItem().builtInRegistryHolder().is(tag);
    }
}
