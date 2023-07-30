package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.ColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryProviderListener;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.ItemEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.ItemFactory;
import xyz.apex.minecraft.apexcore.common.lib.resgen.AdvancementProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.RecipeProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.model.ModelProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.tag.TagsProvider;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Item Builder implementation.
 * <p>
 * Used to build and register Item entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of Item [Entry].
 * @param <P> Type of Parent.
 */
public final class ItemBuilder<O extends AbstractRegistrar<O>, T extends Item, P> extends AbstractBuilder<O, P, Item, T, ItemBuilder<O, T, P>, ItemEntry<T>> implements FeaturedBuilder<O, P, Item, T, ItemBuilder<O, T, P>, ItemEntry<T>>
{
    private final ItemFactory<T> itemFactory;
    private Supplier<Item.Properties> initialProperties = Item.Properties::new;
    private Function<Item.Properties, Item.Properties> propertiesModifier = Function.identity();

    @Nullable private Supplier<Supplier<ItemColor>> colorHandler = null;

    @ApiStatus.Internal
    public ItemBuilder(O registrar, P parent, String registrationName, ItemFactory<T> itemFactory)
    {
        super(registrar, parent, Registries.ITEM, registrationName);

        this.itemFactory = itemFactory;

        defaultModel();
    }

    @Override
    protected void onRegister(T entry)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> {
            if(colorHandler != null)
                ColorHandlerHooks.get().registerItemHandler(this::getEntry, colorHandler);
        });
    }

    /**
     * Registers a color handler for this Item.
     *
     * @param colorHandler Color handler to be registered.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> colorHandler(Supplier<Supplier<ItemColor>> colorHandler)
    {
        this.colorHandler = colorHandler;
        return this;
    }

    /**
     * Initial Item properties for this Item.
     *
     * @param initialProperties Initial Item properties.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> initialProperties(Supplier<Item.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    /**
     * Adds a new property modifier, used to modify the finalized Item properties.
     *
     * @param propertiesModifier Modifier used to modify the finalized Item properties.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> properties(UnaryOperator<Item.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    /**
     * Mark this Item as being an edible Item using the given food properties.
     *
     * @param foodProperties Food properties to be bound to this Item.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> food(FoodProperties foodProperties)
    {
        return properties(properties -> properties.food(foodProperties));
    }

    /**
     * Set the max stack size for this item.
     *
     * @param maxStackSize Max stack size for this Item.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> stacksTo(int maxStackSize)
    {
        return properties(properties -> properties.stacksTo(maxStackSize));
    }

    /**
     * Set the durability of this Item, if it has not already been set.
     *
     * @param maxDamage Durability for this Item.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> defaultDurability(int maxDamage)
    {
        return properties(properties -> properties.defaultDurability(maxDamage));
    }

    /**
     * Set the durability of this Item.
     *
     * @param maxDamage Durability for this Item.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> durability(int maxDamage)
    {
        return properties(properties -> properties.durability(maxDamage));
    }

    /**
     * Set the crafting remainder Item left behind after using this Item during crafting recipes.
     *
     * @param craftingRemainderItem Crafting remainder Item.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> craftRemainderItem(Supplier<ItemLike> craftingRemainderItem)
    {
        return properties(properties -> properties.craftRemainder(craftingRemainderItem.get().asItem()));
    }

    /**
     * Set the rarity of this Item.
     *
     * @param rarity
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> rarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    /**
     * Mark this Item has being fire-resistant.
     *
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }

    /**
     * Set the Model generator for this Item.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> model(RegistryProviderListener<ModelProvider, T, ItemEntry<T>> listener)
    {
        return setProvider(ProviderTypes.MODELS, listener);
    }

    /**
     * Clears the currently registered Model generator.
     *
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> noModel()
    {
        return clearProvider(ProviderTypes.MODELS);
    }

    /**
     * Set the default Model generator for this Item.
     *
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> defaultModel()
    {
        return model((provider, lookup, entry) -> {
            var resourcePath = entry.getRegistryName().withPrefix("item/");
            provider.generated(resourcePath, resourcePath);
        });
    }

    /**
     * Set the default BlockItem Model generator for this Item.
     *
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> defaultBlockItemModel()
    {
        return model((provider, lookup, entry) -> {
            var registryName = entry.getRegistryName();
            provider.withParent(
                    registryName.withPrefix("item/"),
                    registryName.withPrefix("block/")
            );
        });
    }

    /**
     * Resister a set of Tags for this Item.
     *
     * @param tags Tags to be set.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> tag(TagKey<Item>... tags)
    {
        return tag((provider, lookup, entry) -> Stream.of(tags).map(provider::tag).forEach(tag -> tag.addElement(entry.value())));
    }

    /**
     * Set the Tag generator for this Item.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> tag(RegistryProviderListener<TagsProvider<Item>, T, ItemEntry<T>> listener)
    {
        return setProvider(ProviderTypes.ITEM_TAGS, listener);
    }

    /**
     * Clears the currently registered Tag generator.
     *
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> noTags()
    {
        return clearProvider(ProviderTypes.ITEM_TAGS);
    }

    /**
     * Set the Recipe generator for this Item.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> recipe(RegistryProviderListener<RecipeProvider, T, ItemEntry<T>> listener)
    {
        return setProvider(ProviderTypes.RECIPES, listener);
    }

    /**
     * Clears the currently registered Recipe generator.
     *
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> noRecipe()
    {
        return clearProvider(ProviderTypes.RECIPES);
    }

    /**
     * Set the Advancement generator for this Item.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> advancement(RegistryProviderListener<AdvancementProvider, T, ItemEntry<T>> listener)
    {
        return setProvider(ProviderTypes.ADVANCEMENTS, listener);
    }

    /**
     * Clears the currently registered Advancement generator.
     *
     * @return This Builder.
     */
    public ItemBuilder<O, T, P> noAdvancement()
    {
        return clearProvider(ProviderTypes.ADVANCEMENTS);
    }

    @Override
    public ItemBuilder<O, T, P> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    @Override
    protected ItemEntry<T> createRegistryEntry()
    {
        return new ItemEntry<>(registrar, registryKey);
    }

    @Override
    protected T createEntry()
    {
        return itemFactory.create(propertiesModifier.apply(initialProperties.get()));
    }

    @Override
    protected String getDescriptionId(ItemEntry<T> entry)
    {
        return entry.value().getDescriptionId();
    }
}
