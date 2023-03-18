package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

public interface ItemLikeEntry<T extends ItemLike> extends ItemLike
{
    @SuppressWarnings("unchecked")
    private RegistryEntry<T> self()
    {
        return (RegistryEntry<T>) this;
    }

    @Override
    default Item asItem()
    {
        return self().get().asItem();
    }

    default ItemStack asStack(int count)
    {
        return new ItemStack(this, count);
    }

    default ItemStack asStack()
    {
        return new ItemStack(this);
    }

    default boolean is(ItemStack stack)
    {
        return stack.is(asItem());
    }
}
