package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TerminalCondition implements Condition
{
    private final Map<Property<?>, String> terms = Maps.newHashMap();

    TerminalCondition() { }

    public <T extends Comparable<T>> TerminalCondition with(Property<T> property, T value)
    {
        return put(property, property.getName(value));
    }

    @SafeVarargs
    public final <T extends Comparable<T>> TerminalCondition with(Property<T> property, T first, T... values)
    {
        return put(property, getTerm(property, first, values));
    }

    public <T extends Comparable<T>> TerminalCondition not(Property<T> property, T value)
    {
        return put(property, '!' + property.getName(value));
    }

    @SafeVarargs
    public final <T extends Comparable<T>> TerminalCondition not(Property<T> property, T first, T... values)
    {
        return put(property, '!' + getTerm(property, first, values));
    }

    @Override
    public void validate(StateDefinition<Block, BlockState> stateDefinition)
    {
        var missing = terms.keySet().stream().filter(property -> stateDefinition.getProperty(property.getName()) != property).toList();

        if(!missing.isEmpty())
        {
            var missingNames = missing.stream().map(Property::getName).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("Properties %s ar missing from %s".formatted(missingNames, stateDefinition));
        }
    }

    @Override
    public JsonElement toJson()
    {
        var json = new JsonObject();
        terms.forEach((property, term) -> json.addProperty(property.getName(), term));
        return json;
    }

    private TerminalCondition put(Property<?> property, String value)
    {
        var old = terms.put(property, value);

        if(old != null)
            throw new IllegalStateException("Tried to replace %s value from %s to %s".formatted(property.getName(), value, old));

        return this;
    }

    private static <T extends Comparable<T>> String join(Property<T> property, Stream<T> values)
    {
        return values.map(property::getName).collect(Collectors.joining("|"));
    }

    private static <T extends Comparable<T>> String getTerm(Property<T> property, T first, T[] values)
    {
        return join(property, Stream.concat(Stream.of(first), Stream.of(values)));
    }
}
