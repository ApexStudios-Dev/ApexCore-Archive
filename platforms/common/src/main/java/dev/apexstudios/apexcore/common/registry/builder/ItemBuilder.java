package dev.apexstudios.apexcore.common.registry.builder;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.holder.DeferredItem;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class ItemBuilder<O extends AbstractRegister<O>, P, T extends Item> extends AbstractBuilder<O, P, Item, T, DeferredItem<T>, ItemBuilder<O, P, T>>
{
    private final Function<Item.Properties, T> itemFactory;
    private OptionalLike<Item.Properties> propertiesFactory = OptionalLike.empty();
    private Function<Item.Properties, Item.Properties> propertiesModifier = Function.identity();
    private OptionalLike<OptionalLike<ItemColor>> colorHandler = OptionalLike.empty();
    private OptionalLike<DispenseItemBehavior> dispenseBehavior = OptionalLike.empty();
    private final Multimap<ResourceKey<CreativeModeTab>, OptionalLike<CreativeModeTab.DisplayItemsGenerator>> creativeModeTabs = MultimapBuilder.hashKeys().linkedListValues().build();

    @ApiStatus.Internal
    public ItemBuilder(O owner, P parent, String itemName, BuilderHelper helper, Function<Item.Properties, T> itemFactory)
    {
        super(owner, parent, Registries.ITEM, itemName, DeferredItem::createItem, helper);

        this.itemFactory = itemFactory;
        owner.withDefaultCreativeModeTab(this::creativeModeTab);
    }

    public ItemBuilder<O, P, T> creativeModeTab(ResourceKey<CreativeModeTab> creativeModeTab)
    {
        return creativeModeTab(creativeModeTab, () -> (parameters, output) -> output.accept(holder()));
    }

    public ItemBuilder<O, P, T> creativeModeTab(ResourceKey<CreativeModeTab> creativeModeTab, OptionalLike<CreativeModeTab.DisplayItemsGenerator> generator)
    {
        creativeModeTabs.put(creativeModeTab, generator);
        return this;
    }

    public ItemBuilder<O, P, T> removeCreativeModeTab(ResourceKey<CreativeModeTab> creativeModeTab)
    {
        creativeModeTabs.removeAll(creativeModeTab);
        return this;
    }

    public ItemBuilder<O, P, T> color(OptionalLike<OptionalLike<ItemColor>> colorHandler)
    {
        this.colorHandler = OptionalLike.of(colorHandler);
        return this;
    }

    public ItemBuilder<O, P, T> properties(UnaryOperator<Item.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public ItemBuilder<O, P, T> dispenseBehavior(OptionalLike<DispenseItemBehavior> dispenseBehavior)
    {
        this.dispenseBehavior = dispenseBehavior;
        return this;
    }

    public ItemBuilder<O, P, T> dispenseBehavior(@Nullable DispenseItemBehavior dispenseBehavior)
    {
        return dispenseBehavior(OptionalLike.of(dispenseBehavior));
    }

    public ItemBuilder<O, P, T> initialProperties(OptionalLike<Item.Properties> propertiesFactory)
    {
        this.propertiesFactory = propertiesFactory;
        return this;
    }

    public ItemBuilder<O, P, T> initialProperties(Item.Properties initialProperties)
    {
        return initialProperties(() -> initialProperties);
    }

    @Override
    protected T createValue()
    {
        return itemFactory.apply(propertiesModifier.apply(propertiesFactory.orElseGet(Item.Properties::new)));
    }

    @Override
    protected void onRegister(T value)
    {
        owner.registerColorHandler(value, colorHandler);
        owner.registerItemDispenseBehavior(value, dispenseBehavior);

        creativeModeTabs.keySet().forEach(
                creativeModeTab -> creativeModeTabs.get(creativeModeTab).forEach(
                        g -> g.ifPresent(
                                generator -> owner.registerCreativeModeTabItemGenerator(creativeModeTab, generator)
                        )
                )
        );
    }
}
