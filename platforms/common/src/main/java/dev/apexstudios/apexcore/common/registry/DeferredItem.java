package dev.apexstudios.apexcore.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.UnaryOperator;

public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> implements ItemLike
{
    protected DeferredItem(ResourceKey<Item> valueKey)
    {
        super(valueKey);
    }

    public ItemStack asStack(int count, UnaryOperator<ItemStack> modifier)
    {
        return isPresent() ? modifier.apply(new ItemStack((ItemLike) this, count)) : ItemStack.EMPTY;
    }

    public ItemStack asStack(int count)
    {
        return asStack(count, UnaryOperator.identity());
    }

    public ItemStack asStack(UnaryOperator<ItemStack> modifier)
    {
        return asStack(1, modifier);
    }

    public ItemStack asStack()
    {
        return asStack(1, UnaryOperator.identity());
    }

    public boolean is(ItemStack stack)
    {
        return isPresent() && stack.is(asItem());
    }

    @Override
    public Item asItem()
    {
        return value();
    }

    public static <T extends Item> DeferredItem<T> createItem(ResourceLocation valueId)
    {
        return createItem(ResourceKey.create(Registries.ITEM, valueId));
    }

    public static <T extends Item> DeferredItem<T> createItem(ResourceKey<Item> valueKey)
    {
        return new DeferredItem<>(valueKey);
    }
}
