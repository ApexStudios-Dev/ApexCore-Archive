package xyz.apex.minecraft.apexcore.shared.registry.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.registry.BasicRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistries;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryKeys;
import xyz.apex.minecraft.apexcore.shared.registry.block.BlockRegistryEntry;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ItemRegistry extends BasicRegistry<Item>
{
    public static final ResourceKey<Registry<Item>> TYPE = RegistryKeys.ITEM;

    private ItemRegistry(String modId)
    {
        super(TYPE, modId);
    }

    public <T extends Item> ItemBuilder<T> builder(String name, Function<Item.Properties, T> factory)
    {
        return new ItemBuilder<>(this, name, factory);
    }

    public ItemBuilder<Item> genericBuilder(String name)
    {
        return builder(name, Item::new);
    }

    public <T extends Item, B extends Block> ItemBuilder<T> blockBuilder(String name, Supplier<B> block, BiFunction<B, Item.Properties, T> factory)
    {
        return builder(name, properties -> factory.apply(block.get(), properties));
    }

    public <T extends Item, B extends Block> ItemBuilder<T> blockBuilder(RegistryEntry<B> block, BiFunction<B, Item.Properties, T> factory)
    {
        return blockBuilder(block.getRegistryName().getPath(), block, factory);
    }

    public <T extends Item, B extends Block> ItemBuilder<T> blockBuilder(BlockRegistryEntry<B> block, BiFunction<B, Item.Properties, T> factory)
    {
        return blockBuilder(block.getRegistryName().getPath(), block, factory);
    }

    public ItemBuilder<BlockItem> genericBlockBuilder(String name, Supplier<Block> block)
    {
        return blockBuilder(name, block, BlockItem::new);
    }

    public ItemBuilder<BlockItem> genericBlockBuilder(RegistryEntry<Block> block)
    {
        return genericBlockBuilder(block.getRegistryName().getPath(), block);
    }

    public ItemBuilder<BlockItem> genericBlockBuilder(BlockRegistryEntry<Block> block)
    {
        return genericBlockBuilder(block.getRegistryName().getPath(), block);
    }

    @Override
    protected <R extends Item> ItemRegistryEntry<R> createRegistryEntry(ResourceKey<R> key, Supplier<R> supplier)
    {
        return new ItemRegistryEntry<>(this, key, supplier);
    }

    @Override
    public <R extends Item> ItemRegistryEntry<R> register(ResourceKey<Item> key, Supplier<R> factory)
    {
        return (ItemRegistryEntry<R>) super.register(key, factory);
    }

    @Override
    public <R extends Item> ItemRegistryEntry<R> register(String name, Supplier<R> factory)
    {
        return (ItemRegistryEntry<R>) super.register(name, factory);
    }

    @Nullable
    @Override
    public ItemRegistryEntry<Item> getEntry(ResourceKey<Item> key)
    {
        var entry = super.getEntry(key);
        return entry instanceof ItemRegistryEntry<Item> item ? item : null;
    }

    @Nullable
    @Override
    public ItemRegistryEntry<Item> getEntry(ResourceLocation id)
    {
        var entry = super.getEntry(id);
        return entry instanceof ItemRegistryEntry<Item> item ? item : null;
    }

    @Nullable
    @Override
    public ItemRegistryEntry<Item> getEntry(String name)
    {
        var entry = super.getEntry(name);
        return entry instanceof ItemRegistryEntry<Item> item ? item : null;
    }

    public static ItemRegistry create(String modId)
    {
        return ModdedRegistries.INSTANCE.getOrCreate(TYPE, modId, ModdedRegistries.Constructor.of(ItemRegistry::new));
    }
}
