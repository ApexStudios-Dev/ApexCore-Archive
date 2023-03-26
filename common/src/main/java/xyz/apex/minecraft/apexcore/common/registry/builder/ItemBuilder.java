package xyz.apex.minecraft.apexcore.common.registry.builder;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.common.platform.Side;
import xyz.apex.minecraft.apexcore.common.platform.SideExecutor;
import xyz.apex.minecraft.apexcore.common.registry.entry.ItemEntry;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ItemBuilder<T extends Item> extends Builder<Item, T, ItemEntry<T>, ItemBuilder<T>>
{
    private Function<Item.Properties, Item.Properties> propertiesModifier = Function.identity();
    private final ItemFactory<T> itemFactory;
    @Nullable private Supplier<ItemColor> itemColor = null;

    public ItemBuilder(String ownerId, String registrationName, ItemFactory<T> itemFactory)
    {
        super(Registries.ITEM, ownerId, registrationName);

        this.itemFactory = itemFactory;

        onRegister(item -> SideExecutor.runWhenOn(Side.CLIENT, () -> () -> {
            if(itemColor != null) RendererHooks.getInstance().registerItemColor(itemColor, () -> item);
        }));
    }

    // region: Custom
    public ItemBuilder<T> itemColor(Supplier<ItemColor> itemColor)
    {
        this.itemColor = itemColor;
        return this;
    }
    // endregion

    // region: Properties
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

    public ItemBuilder<T> defaultDurability(int maxDamage)
    {
        return properties(properties -> properties.defaultDurability(maxDamage));
    }

    public ItemBuilder<T> durability(int maxDamage)
    {
        return properties(properties -> properties.durability(maxDamage));
    }

    public ItemBuilder<T> craftRemainder(Supplier<Item> craftRemainingItem)
    {
        return properties(properties -> properties.craftRemainder(craftRemainingItem.get()));
    }

    public ItemBuilder<T> rarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    public ItemBuilder<T> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }
    // endregion

    @Override
    protected ItemEntry<T> createRegistryEntry(ResourceLocation registryName)
    {
        return new ItemEntry<>(registryName);
    }

    @Override
    protected T create()
    {
        return itemFactory.create(propertiesModifier.apply(new Item.Properties()));
    }

    public static <T extends Item> ItemBuilder<T> builder(String ownerId, String registrationName, ItemFactory<T> itemFactory)
    {
        return new ItemBuilder<>(ownerId, registrationName, itemFactory);
    }

    @FunctionalInterface
    public interface ItemFactory<T extends Item>
    {
        T create(Item.Properties properties);
    }
}
