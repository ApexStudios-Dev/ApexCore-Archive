package xyz.apex.minecraft.apexcore.shared.registry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import xyz.apex.minecraft.apexcore.shared.data.ItemLikeContext;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.data.providers.RecipeProvider;
import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemEntry;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ItemBuilder<R extends Item, P> extends AbstractBuilder<Item, R, P, ItemBuilder<R, P>, ItemEntry<R>>
{
    private final ItemFactory<R> itemFactory;
    private Supplier<Item.Properties> initialProperties = Item.Properties::new;
    private Function<Item.Properties, Item.Properties> propertiesModifier = Function.identity();

    private ItemBuilder(ResourceLocation registryName, @Nullable P parent, ItemFactory<R> itemFactory)
    {
        super(Registries.ITEM, registryName, parent, ItemEntry::new);

        this.itemFactory = itemFactory;

        // TODO
        defaultLang()/*.defaultModel()*/;
    }

    public ItemBuilder<R, P> initialProperties(Supplier<Item.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    public ItemBuilder<R, P> properties(UnaryOperator<Item.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public ItemBuilder<R, P> food(FoodProperties foodProperties)
    {
        return properties(properties -> properties.food(foodProperties));
    }

    public ItemBuilder<R, P> stacksTo(int stackSize)
    {
        return properties(properties -> properties.stacksTo(stackSize));
    }

    public ItemBuilder<R, P> defaultDurability(int durability)
    {
        return properties(properties -> properties.defaultDurability(durability));
    }

    public ItemBuilder<R, P> durability(int durability)
    {
        return properties(properties -> properties.durability(durability));
    }

    public ItemBuilder<R, P> rarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    public ItemBuilder<R, P> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }

    public ItemBuilder<R, P> color(Supplier<Supplier<ItemColor>> colorHandler)
    {
        GamePlatform.events().registerItemColor(asSupplier(), colorHandler);
        return this;
    }

    /*public ItemBuilder<R, P> defaultModel()
    {
        return this;
        // TODO:
        // return model((ctx, provider) -> provider.generated());
    }*/

    /*public ItemBuilder<R, P> model(BiConsumer<ItemLikeContext<R, ItemEntry<R>>, ItemModelProvider> consumer)
    {
        // TODO:
        return this;
        // return setData(ProviderTypes.ITEM_MODEL, consumer);
    }*/

    public ItemBuilder<R, P> defaultLang()
    {
        return lang(Item::getDescriptionId);
    }

    public ItemBuilder<R, P> lang(String name)
    {
        return lang(Item::getDescriptionId, name);
    }

    public ItemBuilder<R, P> recipe(BiConsumer<ItemLikeContext<R, ItemEntry<R>>, RecipeProvider> consumer)
    {
        return setData(ProviderTypes.RECIPES, (ctx, provider) -> consumer.accept(new ItemLikeContext<>(ctx), provider));
    }

    @SafeVarargs
    public final ItemBuilder<R, P> tag(TagKey<Item>... tags)
    {
        return tag(ProviderTypes.ITEM_TAGS, tags);
    }

    public ItemBuilder<R, P> removeTag(TagKey<Item>... tags)
    {
        return removeTag(ProviderTypes.ITEM_TAGS, tags);
    }

    @Override
    protected R construct()
    {
        var properties = propertiesModifier.apply(initialProperties.get());
        return itemFactory.create(properties);
    }

    public static <R extends Item, P> ItemBuilder<R, P> builder(String modId, String name, P parent, ItemFactory<R> itemFactory)
    {
        return new ItemBuilder<>(new ResourceLocation(modId, name), parent, itemFactory);
    }

    public static <R extends Item> ItemBuilder<R, Object> builder(String modId, String name, ItemFactory<R> itemFactory)
    {
        return new ItemBuilder<>(new ResourceLocation(modId, name), null, itemFactory);
    }

    public static <P> ItemBuilder<Item, P> item(String modId, String name, P parent)
    {
        return builder(modId, name, parent, Item::new);
    }

    public static ItemBuilder<Item, Object> item(String modId, String name)
    {
        return builder(modId, name, Item::new);
    }

    @FunctionalInterface
    public interface ItemFactory<T extends Item>
    {
        T create(Item.Properties properties);
    }
}
