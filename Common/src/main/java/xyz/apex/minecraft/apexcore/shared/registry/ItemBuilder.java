package xyz.apex.minecraft.apexcore.shared.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemEntry;
import xyz.apex.minecraft.apexcore.shared.util.Properties;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ItemBuilder<T extends Item> extends AbstractBuilder<Item, T, ItemBuilder<T>, ItemEntry<T>>
{
    private final ItemFactory<T> factory;

    private Supplier<Item.Properties> initialProperties = Properties.ITEM_GENERIC;
    private Function<Item.Properties, Item.Properties> propertiesModifier = Function.identity();

    ItemBuilder(String modId, String registryName, ItemFactory<T> factory)
    {
        super(Registries.ITEM, modId, registryName, ItemEntry::new);

        this.factory = factory;
    }

    @Override
    protected T construct()
    {
        return factory.create(propertiesModifier.apply(initialProperties.get()));
    }

    public ItemBuilder<T> initialProperties(Supplier<Item.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    public ItemBuilder<T> initialProperties(Item.Properties properties)
    {
        return initialProperties(() -> properties);
    }

    public ItemBuilder<T> properties(UnaryOperator<Item.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public ItemBuilder<T> food(FoodProperties foodProperties)
    {
        return properties(properties -> properties.food(foodProperties));
    }

    public ItemBuilder<T> stacksTo(int maxStackSize)
    {
        return properties(properties -> properties.stacksTo(maxStackSize));
    }

    public ItemBuilder<T> defaultDurability(int durability)
    {
        return properties(properties -> properties.defaultDurability(durability));
    }

    public ItemBuilder<T> durability(int durability)
    {
        return properties(properties -> properties.durability(durability));
    }

    @Deprecated
    public ItemBuilder<T> craftRemainder(Item craftingRemainingItem)
    {
        return properties(properties -> properties.craftRemainder(craftingRemainingItem));
    }

    public ItemBuilder<T> craftRemainder(Supplier<? extends ItemLike> craftingRemainingItem)
    {
        return properties(properties -> properties.craftRemainder(craftingRemainingItem.get().asItem()));
    }

    public ItemBuilder<T> rarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    public ItemBuilder<T> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }

    public ItemBuilder<T> requiredFeatures(FeatureFlag... flags)
    {
        return properties(properties -> properties.requiredFeatures(flags));
    }

    @FunctionalInterface
    public interface ItemFactory<T extends Item>
    {
        T create(Item.Properties properties);
    }
}
