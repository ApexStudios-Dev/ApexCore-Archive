package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.ColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.ItemEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.ItemFactory;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;
import xyz.apex.minecraft.apexcore.common.lib.resgen.model.ModelProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.tag.TagsProvider;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Item builder used to construct new item instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of item.
 * @param <M> Type of builder manager.
 */
public final class ItemBuilder<P, T extends Item, M extends BuilderManager<M>> extends AbstractBuilder<P, Item, T, ItemEntry<T>, ItemBuilder<P, T, M>, M> implements FeatureElementBuilder<P, Item, T, ItemEntry<T>, ItemBuilder<P, T, M>, M>
{
    private Supplier<Item.Properties> initialProperties = Item.Properties::new;
    private ItemPropertiesModifier propertiesModifier = ItemPropertiesModifier.identity();
    private final ItemFactory<T> itemFactory;

    ItemBuilder(P parent, M builderManager, String registrationName, ItemFactory<T> itemFactory)
    {
        super(parent, builderManager, Registries.ITEM, registrationName, ItemEntry::new);

        this.itemFactory = itemFactory;

        defaultModel();
    }

    @Override
    protected T createObject()
    {
        return itemFactory.create(propertiesModifier.modify(initialProperties().get()));
    }

    /**
     * Registers a color handler for this item.
     *
     * @param colorHandler Color handler to be registered.
     * @return This builder instance.
     */
    public ItemBuilder<P, T, M> colorHandler(Supplier<Supplier<ItemColor>> colorHandler)
    {
        return addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> ColorHandlerHooks.get().registerItemHandler(() -> value, colorHandler)));
    }

    /**
     * Initial item properties for this item.
     *
     * @param initialProperties Initial item properties.
     * @return This builder instance
     */
    public ItemBuilder<P, T, M> initialProperties(Supplier<Item.Properties> initialProperties)
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
    public ItemBuilder<P, T, M> initialProperties(ItemPropertiesModifier initialProperties)
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
    public ItemBuilder<P, T, M> properties(ItemPropertiesModifier propertiesModifier)
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
    public ItemBuilder<P, T, M> food(FoodProperties foodProperties)
    {
        return properties(properties -> properties.food(foodProperties));
    }

    /**
     * Set the max stack size for this item (1 - 64)
     *
     * @param maxStackSize Max stack size for this item (1 - 64)
     * @return This builder instance.
     */
    public ItemBuilder<P, T, M> stacksTo(int maxStackSize)
    {
        return properties(properties -> properties.stacksTo(maxStackSize));
    }

    /**
     * Set the durability of this item if it has not already been set.
     *
     * @param maxDamage Durability for this item.
     * @return This builder instance.
     */
    public ItemBuilder<P, T, M> defaultDurability(int maxDamage)
    {
        return properties(properties -> properties.defaultDurability(maxDamage));
    }

    /**
     * Set the durability of this item.
     *
     * @param maxDamage Durability of this item.
     * @return This builder instance.
     */
    public ItemBuilder<P, T, M> durability(int maxDamage)
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
    public ItemBuilder<P, T, M> craftingRemainderItem(Supplier<ItemLike> craftingRemainderItem)
    {
        return properties(properties -> properties.craftRemainder(craftingRemainderItem.get().asItem()));
    }

    /**
     * Set the rarity of this item.
     *
     * @param rarity Rarity for this item.
     * @return This builder instance/
     */
    public ItemBuilder<P, T, M> itemRarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    /**
     * Mark item as being fire-resistant.
     *
     * @return This builder instance.
     */
    public ItemBuilder<P, T, M> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }

    @Override
    public ItemBuilder<P, T, M> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        if(getParent() instanceof FeatureElementBuilder<?, ?, ?, ?, ?, ?> feature)
            feature.requiredFeatures(requiredFeatures);
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    // TODO: Resource Gen providers [ recipe, lang, tag, model ]

    public ItemBuilder<P, T, M> model(BiConsumer<ModelProvider, ProviderType.RegistryContext<Item, T>> consumer)
    {
        return setProvider(ModelProvider.PROVIDER_TYPE, consumer);
    }

    public ItemBuilder<P, T, M> noModel()
    {
        return clearProvider(ModelProvider.PROVIDER_TYPE);
    }

    public ItemBuilder<P, T, M> defaultModel()
    {
        return model((provider, context) -> provider.generated(
                context.registryName().withPrefix("item/"),
                context.registryName().withPrefix("item/")
        ));
    }

    public ItemBuilder<P, T, M> defaultBlockItemModel()
    {
        return model((provider, context) -> provider.withParent(
                context.registryName().withPrefix("item/"),
                context.registryName().withPrefix("block/")
        ));
    }

    public ItemBuilder<P, T, M> tag(TagKey<Item>... tags)
    {
        return tag((provider, context) -> {
            for(var tag : tags)
            {
                provider.tag(tag).addElement(context.registryName());
            }
        });
    }

    public ItemBuilder<P, T, M> tag(BiConsumer<TagsProvider<Item>, ProviderType.RegistryContext<Item, T>> listener)
    {
        return addProvider(TagsProvider.ITEM, listener);
    }

    public ItemBuilder<P, T, M> noTags()
    {
        return clearProvider(TagsProvider.ITEM);
    }

    @Override
    protected String getDescriptionId(ProviderType.RegistryContext<Item, T> context)
    {
        return context.value().getDescriptionId();
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
