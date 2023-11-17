package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.holder.DeferredItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ItemBuilder<O extends AbstractRegister<O>, P, T extends Item> extends AbstractBuilder<O, P, Item, T, DeferredItem<T>, ItemBuilder<O, P, T>>
{
    private final Function<Item.Properties, T> itemFactory;
    private Supplier<Item.Properties> propertiesFactory = Item.Properties::new;
    private Function<Item.Properties, Item.Properties> propertiesModifier = Function.identity();

    @ApiStatus.Internal
    public ItemBuilder(O owner, P parent, String valueName, BuilderHelper helper, Function<Item.Properties, T> itemFactory)
    {
        super(owner, parent, Registries.ITEM, valueName, DeferredItem::createItem, helper);

        this.itemFactory = itemFactory;
    }

    public ItemBuilder<O, P, T> properties(UnaryOperator<Item.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public ItemBuilder<O, P, T> initialProperties(Supplier<Item.Properties> propertiesFactory)
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
        return itemFactory.apply(propertiesModifier.apply(propertiesFactory.get()));
    }
}
