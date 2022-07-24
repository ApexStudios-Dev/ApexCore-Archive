package xyz.apex.forge.apexcore.lib.util.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface QuadFunction<A, B, C, D, RESULT>
{
	RESULT apply(A a, B b, C c, D d);

	default <RETURN> QuadFunction<A, B, C, D, RETURN> andThen(Function<? super RESULT, ? extends RETURN> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c, d) -> after.apply(apply(a, b, c, d));
	}
}