package xyz.apex.minecraft.apexcore.common.util.function;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<T> implements LazyLike<T>
{
    private Supplier<T> supplier;
    @Nullable private T value = null;
    private boolean initialized = false;

    private Lazy(Supplier<T> supplier)
    {
        this.supplier = supplier;
    }

    private Lazy(T value)
    {
        this.value = value;
        supplier = () -> value;
        initialized = true;
    }

    @Override
    public T get()
    {
        if(!initialized)
        {
            value = supplier.get();
            supplier = () -> value;
            initialized = true;
        }

        return Objects.requireNonNull(value);
    }

    public static <T> LazyLike<T> of(Supplier<T> supplier)
    {
        if(supplier instanceof LazyLike<T> lazy) return lazy;
        return new Lazy<>(supplier);
    }

    public static <T> LazyLike<T> of(T value)
    {
        return new Lazy<>(value);
    }
}
