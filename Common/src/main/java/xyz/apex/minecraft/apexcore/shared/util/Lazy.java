package xyz.apex.minecraft.apexcore.shared.util;

import com.google.common.base.Suppliers;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T>
{
    private Supplier<T> supplier;
    @Nullable private T value;
    private boolean initialized = false;

    private Lazy(Supplier<T> supplier)
    {
        this.supplier = supplier;
    }

    private Lazy(T value)
    {
        this.value = value;
        supplier = Suppliers.memoize(() -> value);
        initialized = true;
    }

    @Override
    public T get()
    {
        if(!initialized)
        {
            value = supplier.get();
            supplier = Suppliers.memoize(() -> value);
            initialized = true;
        }

        return Objects.requireNonNull(value);
    }

    public static <T> Supplier<T> of(Supplier<T> supplier)
    {
        if(supplier instanceof LazyLike<T> lazy) return lazy;
        return new Lazy<>(supplier);
    }

    public static <T> Supplier<T> of(T value)
    {
        return new Lazy<>(value);
    }
}
