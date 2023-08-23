package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public final class MultiVariantBuilder implements BlockStateGenerator
{
    private final Block block;
    private final List<Variant> variants;
    private final Set<Property<?>> properties = Sets.newHashSet();
    private final List<PropertyDispatch<?>> propertySets = Lists.newArrayList();

    private MultiVariantBuilder(Block block, Variant first, Variant... others)
    {
        this.block = block;
        this.variants = List.of(ArrayUtils.addFirst(others, first));
    }

    @Override
    public Block block()
    {
        return block;
    }

    public MultiVariantBuilder with(PropertyDispatch<?> propertySet)
    {
        var block = block();
        var stateDefinition = block.getStateDefinition();

        propertySet.definedProperties().forEach(property -> {
            if(stateDefinition.getProperty(property.getName()) != property)
                throw new IllegalStateException("Property %s is not defined for block %s".formatted(property, block));
            if(!properties.add(property))
                throw new IllegalStateException("Values of property %s already defined for block %s".formatted(property, block));
        });

        propertySets.add(propertySet);
        return this;
    }

    @Override
    public JsonElement toJson()
    {
        var stream = Stream.of(Pair.of(Selector.empty(), variants));

        for(var propertySet : propertySets)
        {
            var map = propertySet.entries();

            stream = stream.flatMap(pair -> map.entrySet().stream().map(entry -> Pair.of(
                    pair.getKey().extend(entry.getKey()),
                    merge(pair.getValue(), entry.getValue())
            )));
        }

        var map = Maps.<String, JsonElement>newTreeMap();
        stream.forEach(pair -> map.put(pair.getKey().key(), Variant.toJson(pair.getValue())));

        var json1 = new JsonObject();
        map.forEach((key, value) -> JsonHelper.addJsonIfNotEmpty(json1, key, value));

        var json = new JsonObject();
        JsonHelper.addJsonIfNotEmpty(json, "variants", json1);
        return json;
    }

    private static List<Variant> merge(List<Variant> left, List<Variant> right)
    {
        var builder = ImmutableList.<Variant>builder();
        left.forEach(lVar -> right.forEach(rVar -> builder.add(Variant.merge(lVar, rVar))));
        return builder.build();
    }

    public static MultiVariantBuilder builder(Block block)
    {
        return builder(block, Variant.variant());
    }

    public static MultiVariantBuilder builder(Block block, Variant variant)
    {
        return new MultiVariantBuilder(block, variant);
    }

    public static MultiVariantBuilder builder(Block block, Variant first, Variant... others)
    {
        return new MultiVariantBuilder(block, first, others);
    }
}
