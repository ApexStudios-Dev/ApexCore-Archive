package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class PropertyDispatch<P extends PropertyDispatch<P>>
{
    private final Map<Selector, List<Variant>> values = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    protected P put(Selector selector, List<Variant> variants)
    {
        if(values.put(selector, variants) != null)
            throw new IllegalStateException("Value %s is already defined".formatted(selector.key()));

        return (P) this;
    }

    Map<Selector, List<Variant>> entries()
    {
        verifyComplete();
        return ImmutableMap.copyOf(values);
    }

    private void verifyComplete()
    {
        var stream = Stream.of(Selector.empty());

        for(var property : definedProperties())
        {
            stream = stream.flatMap(selector -> property.getAllValues().map(selector::extend));
        }

        var missingProperties = stream.filter(Predicate.not(this.values::containsKey)).toList();

        if(!missingProperties.isEmpty())
            throw new IllegalStateException("Missing definition for properties: %s".formatted(missingProperties));
    }

    abstract List<Property<?>> definedProperties();

    public static <P1 extends Comparable<P1>> C1<P1> property(Property<P1> property)
    {
        return new C1<>(property);
    }

    public static <P1 extends Comparable<P1>, P2 extends Comparable<P2>> C2<P1, P2> property(Property<P1> property1, Property<P2> property2)
    {
        return new C2<>(property1, property2);
    }

    public static <P1 extends Comparable<P1>, P2 extends Comparable<P2>, P3 extends Comparable<P3>> C3<P1, P2, P3> property(Property<P1> property1, Property<P2> property2, Property<P3> property3)
    {
        return new C3<>(property1, property2, property3);
    }

    public static <P1 extends Comparable<P1>, P2 extends Comparable<P2>, P3 extends Comparable<P3>, P4 extends Comparable<P4>> C4<P1, P2, P3, P4> property(Property<P1> property1, Property<P2> property2, Property<P3> property3, Property<P4> property4)
    {
        return new C4<>(property1, property2, property3, property4);
    }

    public static <P1 extends Comparable<P1>, P2 extends Comparable<P2>, P3 extends Comparable<P3>, P4 extends Comparable<P4>, P5 extends Comparable<P5>> C5<P1, P2, P3, P4, P5> property(Property<P1> property1, Property<P2> property2, Property<P3> property3, Property<P4> property4, Property<P5> property5)
    {
        return new C5<>(property1, property2, property3, property4, property5);
    }

    public static final class C1<P1 extends Comparable<P1>> extends PropertyDispatch<C1<P1>>
    {
        private final Property<P1> property;

        private C1(Property<P1> property)
        {
            this.property = property;
        }

        public C1<P1> select(P1 value, Variant variant)
        {
            return select(value, List.of(variant));
        }

        public C1<P1> select(P1 value, Variant first, Variant... others)
        {
            return select(value, List.of(ArrayUtils.addFirst(others, first)));
        }

        public C1<P1> select(P1 value, List<Variant> variants)
        {
            return put(Selector.of(property.value(value)), variants);
        }

        public C1<P1> generate(Function<P1, Variant> mapper)
        {
            property.getPossibleValues().forEach(value -> select(value, mapper.apply(value)));
            return this;
        }

        public C1<P1> generateList(Function<P1, List<Variant>> mapper)
        {
            property.getPossibleValues().forEach(value -> select(value, mapper.apply(value)));
            return this;
        }

        @Override
        List<Property<?>> definedProperties()
        {
            return List.of(property);
        }
    }

    public static final class C2<P1 extends Comparable<P1>, P2 extends Comparable<P2>> extends PropertyDispatch<C2<P1, P2>>
    {
        private final Property<P1> property1;
        private final Property<P2> property2;

        private C2(Property<P1> property1, Property<P2> property2)
        {
            this.property1 = property1;
            this.property2 = property2;
        }

        public C2<P1, P2> select(P1 value1, P2 value2, Variant variant)
        {
            return select(value1, value2, List.of(variant));
        }

        public C2<P1, P2> select(P1 value1, P2 value2, Variant first, Variant... others)
        {
            return select(value1, value2, List.of(ArrayUtils.addFirst(others, first)));
        }

        public C2<P1, P2> select(P1 value1, P2 value2, List<Variant> variants)
        {
            return put(Selector.of(
                    property1.value(value1),
                    property2.value(value2)
            ), variants);
        }

        public C2<P1, P2> generate(BiFunction<P1, P2, Variant> mapper)
        {
            property1
                    .getPossibleValues()
                    .forEach(value1 -> property2
                            .getPossibleValues()
                            .forEach(value2 -> select(value1, value2, mapper.apply(value1, value2))
                            )
                    );
            return this;
        }

        public C2<P1, P2> generateList(BiFunction<P1, P2, List<Variant>> mapper)
        {
            property1
                    .getPossibleValues()
                    .forEach(value1 -> property2
                            .getPossibleValues()
                            .forEach(value2 -> select(value1, value2, mapper.apply(value1, value2))
                            )
                    );
            return this;
        }

        @Override
        List<Property<?>> definedProperties()
        {
            return List.of(property1, property2);
        }
    }

    public static final class C3<P1 extends Comparable<P1>, P2 extends Comparable<P2>, P3 extends Comparable<P3>> extends PropertyDispatch<C3<P1, P2, P3>>
    {
        private final Property<P1> property1;
        private final Property<P2> property2;
        private final Property<P3> property3;

        private C3(Property<P1> property1, Property<P2> property2, Property<P3> property3)
        {
            this.property1 = property1;
            this.property2 = property2;
            this.property3 = property3;
        }

        public C3<P1, P2, P3> select(P1 value1, P2 value2, P3 value3, Variant variant)
        {
            return select(value1, value2, value3, List.of(variant));
        }

        public C3<P1, P2, P3> select(P1 value1, P2 value2, P3 value3, Variant first, Variant... others)
        {
            return select(value1, value2, value3, List.of(ArrayUtils.addFirst(others, first)));
        }

        public C3<P1, P2, P3> select(P1 value1, P2 value2, P3 value3, List<Variant> variants)
        {
            return put(Selector.of(
                    property1.value(value1),
                    property2.value(value2),
                    property3.value(value3)
            ), variants);
        }

        public C3<P1, P2, P3> generate(TriFunction<P1, P2, P3, Variant> mapper)
        {
            property1.getPossibleValues()
                     .forEach(value1 -> property2
                             .getPossibleValues()
                             .forEach(value2 -> property3
                                     .getPossibleValues()
                                     .forEach(value3 -> select(value1, value2, value3, mapper.apply(value1, value2, value3)))
                             )
                     );
            return this;
        }

        public C3<P1, P2, P3> generateList(TriFunction<P1, P2, P3, List<Variant>> mapper)
        {
            property1.getPossibleValues()
                     .forEach(value1 -> property2
                             .getPossibleValues()
                             .forEach(value2 -> property3
                                     .getPossibleValues()
                                     .forEach(value3 -> select(value1, value2, value3, mapper.apply(value1, value2, value3)))
                             )
                     );
            return this;
        }

        @Override
        List<Property<?>> definedProperties()
        {
            return List.of(property1, property2, property3);
        }
    }

    public static final class C4<P1 extends Comparable<P1>, P2 extends Comparable<P2>, P3 extends Comparable<P3>, P4 extends Comparable<P4>> extends PropertyDispatch<C4<P1, P2, P3, P4>>
    {
        private final Property<P1> property1;
        private final Property<P2> property2;
        private final Property<P3> property3;
        private final Property<P4> property4;

        private C4(Property<P1> property1, Property<P2> property2, Property<P3> property3, Property<P4> property4)
        {
            this.property1 = property1;
            this.property2 = property2;
            this.property3 = property3;
            this.property4 = property4;
        }

        public C4<P1, P2, P3, P4> select(P1 value1, P2 value2, P3 value3, P4 value4, Variant variant)
        {
            return select(value1, value2, value3, value4, List.of(variant));
        }

        public C4<P1, P2, P3, P4> select(P1 value1, P2 value2, P3 value3, P4 value4, Variant first, Variant... others)
        {
            return select(value1, value2, value3, value4, List.of(ArrayUtils.addFirst(others, first)));
        }

        public C4<P1, P2, P3, P4> select(P1 value1, P2 value2, P3 value3, P4 value4, List<Variant> variants)
        {
            return put(Selector.of(
                    property1.value(value1),
                    property2.value(value2),
                    property3.value(value3),
                    property4.value(value4)
            ), variants);
        }

        public C4<P1, P2, P3, P4> generate(QuadFunction<P1, P2, P3, P4, Variant> mapper)
        {
            property1.getPossibleValues()
                     .forEach(value1 -> property2
                             .getPossibleValues()
                             .forEach(value2 -> property3
                                     .getPossibleValues()
                                     .forEach(value3 -> property4
                                             .getPossibleValues()
                                             .forEach(value4 -> select(value1, value2, value3, value4, mapper.apply(value1, value2, value3, value4)))
                                     )
                             )
                     );
            return this;
        }

        public C4<P1, P2, P3, P4> generateList(QuadFunction<P1, P2, P3, P4, List<Variant>> mapper)
        {
            property1.getPossibleValues()
                     .forEach(value1 -> property2
                             .getPossibleValues()
                             .forEach(value2 -> property3
                                     .getPossibleValues()
                                     .forEach(value3 -> property4
                                             .getPossibleValues()
                                             .forEach(value4 -> select(value1, value2, value3, value4, mapper.apply(value1, value2, value3, value4)))
                                     )
                             )
                     );
            return this;
        }

        @Override
        List<Property<?>> definedProperties()
        {
            return List.of(property1, property2, property3, property4);
        }
    }

    public static final class C5<P1 extends Comparable<P1>, P2 extends Comparable<P2>, P3 extends Comparable<P3>, P4 extends Comparable<P4>, P5 extends Comparable<P5>> extends PropertyDispatch<C5<P1, P2, P3, P4, P5>>
    {
        private final Property<P1> property1;
        private final Property<P2> property2;
        private final Property<P3> property3;
        private final Property<P4> property4;
        private final Property<P5> property5;

        private C5(Property<P1> property1, Property<P2> property2, Property<P3> property3, Property<P4> property4, Property<P5> property5)
        {
            this.property1 = property1;
            this.property2 = property2;
            this.property3 = property3;
            this.property4 = property4;
            this.property5 = property5;
        }

        public C5<P1, P2, P3, P4, P5> select(P1 value1, P2 value2, P3 value3, P4 value4, P5 value5, Variant variant)
        {
            return select(value1, value2, value3, value4, value5, List.of(variant));
        }

        public C5<P1, P2, P3, P4, P5> select(P1 value1, P2 value2, P3 value3, P4 value4, P5 value5, Variant first, Variant... others)
        {
            return select(value1, value2, value3, value4, value5, List.of(ArrayUtils.addFirst(others, first)));
        }

        public C5<P1, P2, P3, P4, P5> select(P1 value1, P2 value2, P3 value3, P4 value4, P5 value5, List<Variant> variants)
        {
            return put(Selector.of(
                    property1.value(value1),
                    property2.value(value2),
                    property3.value(value3),
                    property4.value(value4),
                    property5.value(value5)
            ), variants);
        }

        public C5<P1, P2, P3, P4, P5> generate(PentaFunction<P1, P2, P3, P4, P5, Variant> mapper)
        {
            property1.getPossibleValues()
                     .forEach(value1 -> property2
                             .getPossibleValues()
                             .forEach(value2 -> property3
                                     .getPossibleValues()
                                     .forEach(value3 -> property4
                                             .getPossibleValues()
                                             .forEach(value4 -> property5
                                                     .getPossibleValues()
                                                     .forEach(value5 -> select(value1, value2, value3, value4, value5, mapper.apply(value1, value2, value3, value4, value5)))
                                             )
                                     )
                             )
                     );
            return this;
        }

        public C5<P1, P2, P3, P4, P5> generateList(PentaFunction<P1, P2, P3, P4, P5, List<Variant>> mapper)
        {
            property1.getPossibleValues()
                     .forEach(value1 -> property2
                             .getPossibleValues()
                             .forEach(value2 -> property3
                                     .getPossibleValues()
                                     .forEach(value3 -> property4
                                             .getPossibleValues()
                                             .forEach(value4 -> property5
                                                     .getPossibleValues()
                                                     .forEach(value5 -> select(value1, value2, value3, value4, value5, mapper.apply(value1, value2, value3, value4, value5)))
                                             )
                                     )
                             )
                     );
            return this;
        }

        @Override
        List<Property<?>> definedProperties()
        {
            return List.of(property1, property2, property3, property4, property5);
        }
    }

    @FunctionalInterface
    public interface QuadFunction<P1, P2, P3, P4, R>
    {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4);
    }

    @FunctionalInterface
    public interface PentaFunction<P1, P2, P3, P4, P5, R>
    {
        R apply(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
    }
}
