package xyz.apex.minecraft.apexcore.common.registry.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.common.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.registry.entry.ItemEntry;
import xyz.apex.minecraft.apexcore.common.util.Properties;
import xyz.apex.minecraft.apexcore.common.util.function.Lazy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings({ "UnstableApiUsage", "unchecked" })
public final class ItemBuilder<R extends Item, O extends AbstractRegistrar<O>, P> extends AbstractBuilder<Item, R, O, P, ItemBuilder<R, O, P>>
{
    private final Factory<R> factory;
    private Supplier<Item.Properties> initialProperties = Properties.ITEM_GENERIC;
    private Function<Item.Properties, Item.Properties> propertiesModifier = Function.identity();
    private int burnTime = -1;
    private Supplier<Supplier<ItemColor>> itemColorSupplier = () -> () -> null;
    private final Map<ResourceLocation, Supplier<Supplier<ClampedItemPropertyFunction>>> itemPropertiesMap = Maps.newHashMap();
    private final List<Pair<CreativeModeTab, Function<R, ItemStack>>> additionalCreativeModeTabs = Lists.newArrayList();
    @Nullable private MenuBuilder<?, ?, O, ItemBuilder<R, O, P>> menuBuilder = null;

    public ItemBuilder(O owner, P parent, String registrationName, Factory<R> factory)
    {
        super(owner, parent, Registries.ITEM, registrationName);

        this.factory = factory;

        onRegister(item -> {
            if(burnTime != -1) FuelRegistry.register(burnTime, item);

            EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
                var itemColor = itemColorSupplier.get().get();
                if(itemColor != null) ColorHandlerRegistry.registerItemColors(itemColor, item);

                itemPropertiesMap.forEach((propertyName, propertyFunctionSupplier) -> {
                    var propertyFunction = propertyFunctionSupplier.get().get();
                    if(propertyFunction != null) ItemPropertiesRegistry.register(item, propertyName, propertyFunction);
                });
            });

            if(!additionalCreativeModeTabs.isEmpty())
            {
                // batch everything down into modify call per tab
                var map = Maps.<CreativeModeTab, List<Supplier<ItemStack>>>newHashMap();

                for(var entry : additionalCreativeModeTabs)
                {
                    var stack = entry.getRight().apply(item);
                    if(stack.isEmpty()) continue;
                    map.computeIfAbsent(entry.getLeft(), $ -> Lists.newArrayList()).add(Lazy.of(stack));
                }

                map.forEach((creativeModeTab, stacks) -> CreativeTabRegistry.appendStack(creativeModeTab, stacks.toArray(Supplier[]::new)));
            }
        });
    }

    public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> ItemBuilder<R, O, P> menu(MenuBuilder.MenuFactory<M> menuFactory, Supplier<MenuBuilder.ScreenFactory<M, S>> screenFactorySupplier)
    {
        menuBuilder = new MenuBuilder<>(owner, this, getRegistrationName(), menuFactory, screenFactorySupplier);
        return this;
    }

    public ItemBuilder<R, O, P> noMenu()
    {
        menuBuilder = null;
        return this;
    }

    // Only to be used with vanilla tabs
    public ItemBuilder<R, O, P> creativeModeTab(CreativeModeTab creativeModeTab, Function<R, ItemStack> stackFunction)
    {
        additionalCreativeModeTabs.add(Pair.of(creativeModeTab, stackFunction));
        return this;
    }

    public ItemBuilder<R, O, P> creativeModeTab(CreativeModeTab creativeModeTab)
    {
        return creativeModeTab(creativeModeTab, Item::getDefaultInstance);
    }

    public ItemBuilder<R, O, P> itemProperty(ResourceLocation propertyName, Supplier<Supplier<ClampedItemPropertyFunction>> propertyFunction)
    {
        itemPropertiesMap.put(propertyName, propertyFunction);
        return this;
    }

    public ItemBuilder<R, O, P> itemColor(Supplier<Supplier<ItemColor>> itemColorSupplier)
    {
        this.itemColorSupplier = itemColorSupplier;
        return this;
    }

    public ItemBuilder<R, O, P> burnTime(int burnTime)
    {
        this.burnTime = burnTime;
        return this;
    }

    public ItemBuilder<R, O, P> initialProperties(Supplier<Item.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    public ItemBuilder<R, O, P> initialProperties(Item.Properties properties)
    {
        return initialProperties(() -> properties);
    }

    public ItemBuilder<R, O, P> properties(UnaryOperator<Item.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public ItemBuilder<R, O, P> food(FoodProperties foodProperties)
    {
        return properties(properties -> properties.food(foodProperties));
    }

    public ItemBuilder<R, O, P> stacksTo(int maxStackSize)
    {
        return properties(properties -> properties.stacksTo(maxStackSize));
    }

    public ItemBuilder<R, O, P> defaultDurability(int durability)
    {
        return properties(properties -> properties.defaultDurability(durability));
    }

    public ItemBuilder<R, O, P> durability(int durability)
    {
        return properties(properties -> properties.durability(durability));
    }

    @Deprecated
    public ItemBuilder<R, O, P> craftRemainder(Item craftingRemainingItem)
    {
        return properties(properties -> properties.craftRemainder(craftingRemainingItem));
    }

    public ItemBuilder<R, O, P> craftRemainder(Supplier<? extends ItemLike> craftingRemainingItem)
    {
        return properties(properties -> properties.craftRemainder(craftingRemainingItem.get().asItem()));
    }

    public ItemBuilder<R, O, P> rarity(Rarity rarity)
    {
        return properties(properties -> properties.rarity(rarity));
    }

    public ItemBuilder<R, O, P> fireResistant()
    {
        return properties(Item.Properties::fireResistant);
    }

    public ItemBuilder<R, O, P> requiredFeatures(FeatureFlag... flags)
    {
        return properties(properties -> properties.requiredFeatures(flags));
    }

    @Override
    protected R createEntry()
    {
        return factory.create(propertiesModifier.apply(initialProperties.get()));
    }

    @Override
    protected ItemEntry<R> createRegistryEntry(RegistrySupplier<R> delegate)
    {
        return new ItemEntry<>(owner, delegate, registryKey);
    }

    @Override
    public ItemEntry<R> register()
    {
        if(menuBuilder != null) menuBuilder.register();
        return (ItemEntry<R>) super.register();
    }

    @FunctionalInterface
    public interface Factory<T extends Item>
    {
        T create(Item.Properties properties);
    }
}
