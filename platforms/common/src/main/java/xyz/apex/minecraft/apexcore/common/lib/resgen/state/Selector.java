package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class Selector
{
    private static final Selector EMPTY = new Selector(Collections.emptyList());
    private static final Comparator<Property.Value<?>> COMPARE_BY_NAME = Comparator.comparing(value -> value.property().getName());

    private final List<Property.Value<?>> values;

    private Selector(Property.Value<?> first, Property.Value<?>... others)
    {
        values = List.of(ArrayUtils.addFirst(others, first));
    }

    private Selector(Collection<Property.Value<?>> values)
    {
        this.values = List.copyOf(values);
    }

    public List<Property.Value<?>> values()
    {
        return List.copyOf(values);
    }

    public Selector extend(Property.Value<?> value)
    {
        var newValues = Lists.newArrayList(values);
        newValues.add(value);
        return new Selector(newValues);
    }

    public Selector extend(Selector selector)
    {
        var newValues = Lists.newArrayList(values);
        newValues.addAll(selector.values);
        return new Selector(newValues);
    }

    public String key()
    {
        return values.stream().sorted(COMPARE_BY_NAME).map(Property.Value::toString).collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Selector other))
            return false;
        return values.equals(other.values);
    }

    @Override
    public int hashCode()
    {
        return values.hashCode();
    }

    @Override
    public String toString()
    {
        return "Selector(%s)".formatted(key());
    }

    public static Selector empty()
    {
        return EMPTY;
    }

    public static Selector of(Property.Value<?> first, Property.Value<?>... others)
    {
        return new Selector(first, others);
    }
}
