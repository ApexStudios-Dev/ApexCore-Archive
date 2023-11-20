package dev.apexstudios.apexcore.common.util;

import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface OptionalLike<T> extends Supplier<T>
{
    OptionalLike<?> EMPTY = () -> null;

    @Nullable T getRaw();

    @Override
    default T get()
    {
        return Objects.requireNonNull(getRaw(), "No value present");
    }

    default boolean isPresent()
    {
        return !isEmpty();
    }

    default boolean isEmpty()
    {
        return this == EMPTY || getRaw() == null;
    }

    default void ifPresent(Consumer<? super T> action)
    {
        if(isPresent())
            action.accept(get());
    }

    default void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
    {
        if(isPresent())
            action.accept(get());
        else
            emptyAction.run();
    }

    default OptionalLike<T> filter(Predicate<? super T> filter)
    {
        return isPresent() && filter.test(get()) ? this : empty();
    }

    default <U> OptionalLike<U> map(Function<? super T, ? extends U> mapper)
    {
        return isPresent() ? of(mapper.apply(get())) : empty();
    }

    default <U> OptionalLike<U> flatMap(Function<? super T, ? extends OptionalLike<? extends U>> mapper)
    {
        return isPresent() ? (OptionalLike<U>) mapper.apply(get()) : empty();
    }

    default OptionalLike<T> or(Supplier<? extends OptionalLike<? extends T>> supplier)
    {
        return isPresent() ? this : (OptionalLike<T>) supplier.get();
    }

    default Stream<T> stream()
    {
        return isPresent() ? Stream.of(get()) : Stream.empty();
    }

    default T orElse(T other)
    {
        return isPresent() ? get() : other;
    }

    default T orElseGet(Supplier<? extends T> supplier)
    {
        return isPresent() ? get() : supplier.get();
    }

    default T orElseThrow()
    {
        return orElseThrow(() -> new NoSuchElementException("No value present"));
    }

    default <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
    {
        if(isPresent())
            return get();

        throw exceptionSupplier.get();
    }

    default Optional<T> toOptional()
    {
        return isPresent() ? Optional.of(get()) : Optional.empty();
    }

    static <T> OptionalLike<T> of(Supplier<@Nullable T> supplier)
    {
        if(supplier instanceof OptionalLike<T> optional)
            return optional;

        return supplier::get;
    }

    static <T> OptionalLike<T> of(@Nullable T value)
    {
        return value == null ? empty() : () -> value;
    }

    static <T> OptionalLike<T> of(Holder<T> holder)
    {
        return () -> holder.isBound() ? holder.value() : null;
    }

    static <T> OptionalLike<T> empty()
    {
        return (OptionalLike<T>) EMPTY;
    }
}
