package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.ItemEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.ItemFactory;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Item builder used to construct new item instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of item.
 */
public final class ItemBuilder<P, T extends Item> extends AbstractBuilder<P, Item, T, ItemEntry<T>, ItemBuilder<P, T>> implements FeatureElementBuilder<P, Item, T, ItemEntry<T>, ItemBuilder<P, T>>
{
    private Supplier<Item.Properties> initialProperties = Item.Properties::new;
    private ItemPropertiesModifier propertiesModifier = ItemPropertiesModifier.identity();
    private final ItemFactory<T> itemFactory;

    ItemBuilder(P parent, BuilderManager builderManager, String registrationName, ItemFactory<T> itemFactory)
    {
        super(parent, builderManager, Registries.ITEM, registrationName, ItemEntry::new);

        this.itemFactory = itemFactory;
    }

    // TODO: creative mode tab, item color

    @Override
    protected T createObject()
    {
        return itemFactory.create(propertiesModifier.modify(initialProperties().get()));
    }

    /**
     * Initial item properties for this item.
     *
     * @param initialProperties Initial item properties.
     * @return This builder instance
     */
    public ItemBuilder<P, T> initialProperties(Supplier<Item.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return self();
    }

    /**
     * Initial item properties for this item.
     *
     * @param initialProperties Initial item properties.
     * @return This builder instance.
     */
    public ItemBuilder<P, T> initialProperties(ItemPropertiesModifier initialProperties)
    {
        this.initialProperties = () -> initialProperties.modify(new Item.Properties());
        return self();
    }

    /**
     * @return Initial item properties for this item.
     */
    public Supplier<Item.Properties> initialProperties()
    {
        return initialProperties;
    }

    /**
     * @return Modifier used to construct finalized & modified item properties.
     */
    public ItemPropertiesModifier itemPropertiesModifier()
    {
        return propertiesModifier;
    }

    /**
     * Add a new property modifier to modify the finalized item properties.
     *
     * @param propertiesModifier Modifier used to modify the finalized item properties.
     * @return This builder instance.
     */
    public ItemBuilder<P, T> properties(ItemPropertiesModifier propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return self();
    }

    /**
     * Mark this item as being an edible item using the given food properties.
     *
     * @param foodProperties Food properties to bind to this item marking it as an edible item.
     * @return This builder instance.
     */
    public ItemBuilder<P, T> food(FoodProperties foodProperties)
    {
        return properties(properties -> properties.food(foodProperties));
    }

    /**
     * Set the max stack size for this item (1 - 64)
     *
     * @param maxStackSize Max stack size for this item (1 - 64)
     * @return This builder instance.
     */
    public ItemBuilder<P, T> stacksTo(int maxStackSize)
    {
        return properties(properties -> properties.stacksTo(maxStackSize));
    }

    /**
     * Set the durability of this item if it has not already been set.
     *
     * @param maxDamage Durability for this item.
     * @return This builder instance.
     */
    public ItemBuilder<P, T> defaultDurability(int maxDamage)
    {
        return properties(properties -> properties.defaultDurability(maxDamage));
    }

    /**
     * Set the durability of this item.
     *
     * @param maxDamage Durability of this item.
     * @return This builder instance.
     */
    public ItemBuilder<P, T> durability(int maxDamage)
    {
        return properties(properties -> properties.durability(maxDamage));
    }

    // TODO: Add item hooks into platforms to allow setting item stack remainder here or marking as self remainder item
    //  rather than needing to sub class item just to return 'this' in overridden methods

    /**
     * Set the remainder item lest behind after using this item during crafting recipes.
     *
     * @param craftingRemainderItem Remainder item left behind after crafting with this item.
     * @return This builder instance.
     */
    public ItemBuilder<P, T> craftingRemainderItem(Supplier<ItemLike> craftingRemainderItem)
    {
        return properties(properties -> properties.craftRemainder(craftingRemainderItem.get().asItem()));
    }

    /**
     * Set the rarity of this item.
     *
     * @param rarity Rarity for this item.
     * @return This builder instance/
     */
    public ItemBuilder<P, T> itemRarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    /**
     * Mark item as being fire-resistant.
     *
     * @return This builder instance.
     */
    public ItemBuilder<P, T> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }

    @Override
    public ItemBuilder<P, T> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        if(getParent() instanceof FeatureElementBuilder<?, ?, ?, ?, ?> feature)
            feature.requiredFeatures(requiredFeatures);
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    /**
     * Functional modifier class used to keep track of item property modifications.
     */
    @FunctionalInterface
    interface ItemPropertiesModifier extends UnaryOperator<Item.Properties>
    {
        /**
         * Returns the given properties after being applied with the current set of modifications.
         *
         * @param properties Properties to be modified.
         * @return Properties after being applied with the current set of modifications.
         */
        default Item.Properties modify(Item.Properties properties)
        {
            return apply(properties);
        }

        /**
         * Append a new properties modifier onto the current stack.
         *
         * @param modifier Properties modifier to be appended.
         * @return New properties modifier with the given one appended onto the end of the stack.
         */
        default ItemPropertiesModifier andThen(ItemPropertiesModifier modifier)
        {
            return properties -> modifier.apply(apply(properties));
        }

        /**
         * @return Properties modifier applying no modifications.
         */
        static ItemPropertiesModifier identity()
        {
            return properties -> properties;
        }
    }
}
