package xyz.apex.minecraft.apexcore.shared.registry.item;

import com.google.common.collect.Maps;

import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class DefaultedItemProperties
{
    private static final Map<String, UnaryOperator<Item.Properties>> MODIFIERS = Maps.newHashMap();

    public static UnaryOperator<Item.Properties> modifier(String modId)
    {
        return MODIFIERS.getOrDefault(modId, UnaryOperator.identity());
    }

    public static Item.Properties apply(String modId, Item.Properties properties, Function<Item.Properties, Item.Properties> additionalModification)
    {
        return modifier(modId).andThen(additionalModification).apply(properties);
    }

    public static Item.Properties apply(String modId, Item.Properties properties)
    {
        return apply(modId, properties, UnaryOperator.identity());
    }

    public static void register(String modId, UnaryOperator<Item.Properties> modifier)
    {
        MODIFIERS.put(modId, modifier);
    }
}
