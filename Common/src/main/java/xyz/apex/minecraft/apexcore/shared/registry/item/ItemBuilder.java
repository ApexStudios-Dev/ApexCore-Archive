package xyz.apex.minecraft.apexcore.shared.registry.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.hooks.ItemHooks;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryEntryBuilder;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ItemBuilder<T extends Item> extends RegistryEntryBuilder<Item, T, ItemBuilder<T>>
{
    private final ItemBuilders.ItemFactory<T> factory;
    private Supplier<Item.Properties> properties = Item.Properties::new;
    private Function<Item.Properties, Item.Properties> propertiesModifier = UnaryOperator.identity();

    ItemBuilder(ItemRegistry registry, String name, ItemBuilders.ItemFactory<T> factory)
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
        var properties = DefaultedItemProperties.apply(registry.getRegistryName().getNamespace(), this.properties.get(), propertiesModifier);
        return factory.create(properties);
    }

    @Override
    public ItemRegistryEntry<T> register()
    {
        return (ItemRegistryEntry<T>) super.register();
    }
}
