package xyz.apex.minecraft.apexcore.shared.registry.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.hooks.ItemHooks;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryEntryBuilder;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ItemBuilder<T extends Item> extends RegistryEntryBuilder<Item, T>
{
    private final Function<Item.Properties, T> factory;
    private Supplier<Item.Properties> properties = () -> ItemHooks.copyProperties(Items.STONE);
    private Function<Item.Properties, Item.Properties> propertiesModifier = UnaryOperator.identity();

    private ItemBuilder(ItemRegistry registry, String name, Function<Item.Properties, T> factory)
    {
        super(registry, name);

        this.factory = factory;
    }

    public ItemBuilder<T> initialProperties(Supplier<Item.Properties> properties)
    {
        this.properties = properties;
        return this;
    }

    public ItemBuilder<T> copyFrom(Supplier<ItemLike> copy)
    {
        return initialProperties(() -> ItemHooks.copyProperties(copy.get().asItem()));
    }

    public ItemBuilder<T> properties(UnaryOperator<Item.Properties> properties)
    {
        propertiesModifier = propertiesModifier.andThen(properties);
        return this;
    }

    public ItemBuilder<T> food(FoodProperties foodProperties)
    {
        return properties(properties -> properties.food(foodProperties));
    }

    public ItemBuilder<T> stacksTo(int stackSize)
    {
        return properties(properties -> properties.stacksTo(stackSize));
    }

    public ItemBuilder<T> defaultDurability(int durability)
    {
        return properties(properties -> properties.defaultDurability(durability));
    }

    public ItemBuilder<T> durability(int durability)
    {
        return properties(properties -> properties.durability(durability));
    }

    public ItemBuilder<T> craftRemainder(Supplier<ItemLike> craftRemainder)
    {
        return properties(properties -> properties.craftRemainder(craftRemainder.get().asItem()));
    }

    public ItemBuilder<T> tab(CreativeModeTab creativeModeTab)
    {
        return properties(properties -> properties.tab(creativeModeTab));
    }

    public ItemBuilder<T> rarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    public ItemBuilder<T> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }

    @Override
    protected T build()
    {
        var properties = propertiesModifier.apply(this.properties.get());
        return factory.apply(properties);
    }

    @Override
    public ItemRegistryEntry<T> register()
    {
        return (ItemRegistryEntry<T>) super.register();
    }

    public static <T extends Item> ItemBuilder<T> builder(ItemRegistry registry, String name, Function<Item.Properties, T> factory)
    {
        return new ItemBuilder<>(registry, name, factory);
    }

    public static ItemBuilder<Item> generic(ItemRegistry registry, String name)
    {
        return builder(registry, name, Item::new);
    }

    public static <T extends Item, E extends Block> ItemBuilder<T> forBlock(ItemRegistry registry, RegistryEntry<E> block, BiFunction<E, Item.Properties, T> factory)
    {
        return builder(registry, block.getRegistryName().getPath(), properties -> factory.apply(block.get(), properties));
    }

    public static ItemBuilder<BlockItem> forBlockGeneric(ItemRegistry registry, RegistryEntry<Block> block)
    {
        return forBlock(registry, block, BlockItem::new);
    }
}
