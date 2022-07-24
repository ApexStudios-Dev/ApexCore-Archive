package xyz.apex.forge.apexcore.lib.util.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<A, B, C, RESULT>
{
	RESULT apply(A a, B b, C c);

	default <RETURN> TriFunction<A, B, C, RETURN> andThen(Function<? super RESULT, ? extends RETURN> after)
	{
		Objects.requireNonNull(after);
		return (a, b, c) -> after.apply(apply(a, b, c));
	}
}