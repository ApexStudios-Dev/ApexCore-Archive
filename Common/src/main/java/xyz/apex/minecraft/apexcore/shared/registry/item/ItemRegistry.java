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

import java.util.function.Supplier;

public final class ItemRegistry extends BasicRegistry<Item>
{
    public static final ResourceKey<Registry<Item>> TYPE = RegistryKeys.ITEM;

    private final ItemBuilders builders;

    private ItemRegistry(String modId)
    {
        super(TYPE, modId);

        builders = new ItemBuilders(this);
    }

    public ItemBuilders builders(String name)
    {
        return builders.pushName(name);
    }

    public <T extends Item, E extends Block> ItemBuilder<T> blockBuilder(String name, Supplier<? extends E> block, ItemBuilders.BlockItemFactory<T, E> factory)
    {
        return builders(name).builder(properties -> factory.create(block, properties));
    }

    public <T extends Item, E extends Block> ItemBuilder<T> blockBuilder(RegistryEntry<? extends E> block, ItemBuilders.BlockItemFactory<T, E> factory)
    {
        return blockBuilder(block.getRegistryName().getPath(), block, factory);
    }

    public <E extends Block> ItemBuilder<BlockItem> blockBuilder(String name, Supplier<? extends E> block)
    {
        return blockBuilder(name, block, BlockItem::new);
    }

    public <E extends Block> ItemBuilder<BlockItem> blockBuilder(RegistryEntry<? extends E> block)
    {
        return blockBuilder(block.getRegistryName().getPath(), block);
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
