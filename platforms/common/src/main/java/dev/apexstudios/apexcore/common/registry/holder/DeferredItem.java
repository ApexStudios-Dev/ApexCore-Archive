package dev.apexstudios.apexcore.common.registry.holder;

import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class DeferredItem<T extends Item> extends DeferredHolder<Item, T> implements ItemLike
{
    private DeferredItem(String ownerId, ResourceKey<Item> registryKey)
    {
        super(ownerId, registryKey);
    }

    public ItemStack asStack(int count, UnaryOperator<ItemStack> modifier)
    {
        return map(Item::getDefaultInstance).map(stack -> {
            stack.setCount(count);
            return stack;
        }).map(modifier).orElse(ItemStack.EMPTY);
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
        return map(stack::is).orElse(false);
    }

    @Override
    public Item asItem()
    {
        return this.<Item>map(Function.identity()).orElse(Items.AIR);
    }

    public static ResourceKey<Item> createRegistryKey(ResourceLocation registryName)
    {
        return ResourceKey.create(Registries.ITEM, registryName);
    }

    public static <T extends Item> DeferredItem<T> createItem(String ownerId, ResourceLocation registryName)
    {
        return createItem(ownerId, createRegistryKey(registryName));
    }

    public static <T extends Item> DeferredItem<T> createItem(ResourceLocation registryName)
    {
        return createItem(registryName.getNamespace(), registryName);
    }

    public static <T extends Item> DeferredItem<T> createItem(String ownerId, ResourceKey<Item> registryKey)
    {
        return new DeferredItem<>(ownerId, registryKey);
    }

    public static <T extends Item> DeferredItem<T> createItem(ResourceKey<Item> registryKey)
    {
        return createItem(registryKey.location().getNamespace(), registryKey);
    }
}
