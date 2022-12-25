package xyz.apex.minecraft.apexcore.shared.util.function;

import java.util.Objects;

@FunctionalInterface
public interface QuadPredicate<A, B, C, D> extends QuadFunction<A, B, C, D, Boolean>
{
    boolean test(A a, B b, C c, D d);

    @Override
    default Boolean apply(A a, B b, C c, D d)
    {
        return test(a, b, c, d);
    }

    default QuadPredicate<A, B, C, D> and(QuadPredicate<? super A, ? super B, ? super C, ? super D> other)
    {
        Objects.requireNonNull(other);
        return (a, b, c, d) -> test(a, b, c, d) && other.test(a, b, c, d);
    }

    default QuadPredicate<A, B, C, D> negate()
    {
        return (a, b, c, d) -> !test(a, b, c,d );
    }

    default QuadPredicate<A, B, C, D> or(QuadPredicate<? super A, ? super B, ? super C, ? super D> other)
    {
        Objects.requireNonNull(other);
        return (a, b, c, d) -> test(a, b, c, d) || other.test(a, b, c, d);
    }
}
