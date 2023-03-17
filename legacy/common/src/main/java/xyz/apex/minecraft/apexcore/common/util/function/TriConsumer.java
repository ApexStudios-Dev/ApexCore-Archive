package xyz.apex.minecraft.apexcore.common.util.function;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<A, B, C>
{
    void accept(A a, B b, C c);

    default TriConsumer<A, B, C> andThen(TriConsumer<? super A, ? super B, ? super C> after)
    {
        Objects.requireNonNull(after);

        return (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        };
    }

    static <A, B, C> TriConsumer<A, B, C> noop()
    {
        return (a, b, c) -> {};
    }
}
