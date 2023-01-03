package xyz.apex.minecraft.apexcore.shared.registry.entry;

import dev.architectury.registry.registries.RegistrySupplier;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

@SuppressWarnings("deprecation")
public class ItemLikeEntry<T extends ItemLike> extends RegistryEntry<T> implements ItemLike
{
    public ItemLikeEntry(AbstractRegistrar<?> owner, RegistrySupplier<T> delegate, ResourceKey<? extends Registry<? super T>> registryType, ResourceKey<? super T> registryKey)
    {
        super(owner, delegate, registryType, registryKey);
    }

    @Override
    public final Item asItem()
    {
        return map(ItemLike::asItem).orElse(Items.AIR);
    }

    public final boolean is(@Nullable Item other)
    {
        if(other == null || other == Items.AIR) return false;
        return asItem() == other;
    }

    public final boolean is(@Nullable ItemLike other)
    {
        if(other == null) return false;
        return is(other.asItem());
    }

    public final boolean hasItemTag(TagKey<Item> tag)
    {
        return asItem().builtInRegistryHolder().is(tag);
    }

    public final ItemStack asStack(int count)
    {
        return asStack().copyWithCount(count);
    }

    public final ItemStack asStack()
    {
        return asItem().getDefaultInstance();
    }
}
