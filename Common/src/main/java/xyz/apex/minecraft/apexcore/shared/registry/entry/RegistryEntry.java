package xyz.apex.minecraft.apexcore.shared.registry.entry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.util.LazyLike;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RegistryEntry<T> implements Supplier<T>, LazyLike<T>, Comparable<RegistryEntry<?>>
{
    protected final ResourceKey<? extends Registry<? super T>> registryType;
    protected final ResourceKey<T> registryKey;
    @Nullable private T value;
    @Nullable private Holder<? super T> holder;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public RegistryEntry(ResourceKey<? extends Registry<? super T>> registryType, ResourceLocation registryName)
    {
        this.registryType = registryType;
        registryKey = ResourceKey.create((ResourceKey) registryType, registryName);
    }

    public final ResourceKey<? extends Registry<? super T>> getRegistryType()
    {
        return registryType;
    }

    public final ResourceKey<T> getRegistryKey()
    {
        return registryKey;
    }

    public final ResourceLocation getRegistryName()
    {
        return registryKey.location();
    }

    public final String getModId()
    {
        return getRegistryName().getNamespace();
    }

    public final String getName()
    {
        return getRegistryName().getPath();
    }

    public final Holder<? super T> getHolder()
    {
        return Objects.requireNonNull(holder);
    }

    @Override
    public final T get()
    {
        return Objects.requireNonNull(value);
    }

    @Nullable
    public final T getUnchecked()
    {
        return value;
    }

    public final boolean isPresent()
    {
        return value != null;
    }

    @ApiStatus.Internal
    public final void updateReference(T value, Holder<? super T> holder)
    {
        this.value = value;
        this.holder = holder;
    }

    public final void ifPresent(Consumer<? super T> action)
    {
        if(isPresent()) action.accept(get());
    }

    public final void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
    {
        if(isPresent()) action.accept(get());
        else emptyAction.run();
    }

    public final Optional<T> filter(Predicate<? super T> predicate)
    {
        return isPresent() && predicate.test(get()) ? Optional.of(get()) : Optional.empty();
    }

    public final <U> Optional<U> map(Function<? super T, ? extends U> mapper)
    {
        if(isPresent()) return Optional.ofNullable(mapper.apply(get()));
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public final <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)
    {
        if(isPresent()) return (Optional<U>) mapper.apply(get());
        return Optional.empty();
    }

    public final Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)
    {
        if(isPresent()) return Optional.of(get());
        return Optional.of(supplier.get().get());
    }

    public final Stream<T> stream()
    {
        return isPresent() ? Stream.of(get()) : Stream.empty();
    }

    public final T orElse(T other)
    {
        return isPresent() ? get() : other;
    }

    public final T orElseGet(Supplier<? extends T> supplier)
    {
        return isPresent() ? get() : supplier.get();
    }

    public final T orElseThrow() throws NoSuchElementException
    {
        return orElseThrow(() -> new NoSuchElementException("No value present"));
    }

    public final <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
    {
        if(isPresent()) return get();
        throw exceptionSupplier.get();
    }

    public boolean is(@Nullable T other)
    {
        return isPresent() && get() == other;
    }

    public final boolean is(@Nullable RegistryEntry<?> other)
    {
        if(other == null) return false;
        return equals(registryType, other.registryType) && equals(registryKey, other.registryKey);
    }

    public final boolean isFor(@Nullable ResourceKey<? extends Registry<?>> registryType)
    {
        return registryType != null && this.registryType.isFor(registryType);
    }

    private boolean equals(ResourceKey<?> a, ResourceKey<?> b)
    {
        if(!a.registry().equals(b.registry())) return false;
        if(!a.location().equals(b.location())) return false;
        return true;
    }

    @Override
    public final int compareTo(RegistryEntry<?> other)
    {
        var cmp = compare(registryType, other.registryKey);
        cmp += compare(registryKey, other.registryKey);

        if(isPresent()) cmp++;
        else cmp--;

        return cmp;
    }

    private int compare(ResourceKey<?> a, ResourceKey<?> b)
    {
        var cmp = a.registry().compareTo(b.registry());
        cmp += a.location().compareTo(b.location());
        return cmp;
    }

    @Override
    public final boolean equals(Object obj)
    {
        if(this == obj) return true;

        if(obj instanceof RegistryEntry<?> entry)
        {
            if(!is(entry)) return false;
            return value == entry.value;
        }

        return false;
    }

    @Override
    public final int hashCode()
    {
        return Objects.hashCode(registryKey);
    }

    @Override
    public final String toString()
    {
        return "RegistryEntry<%s>(%s)".formatted(registryType, registryKey);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Optional<Registry<T>> findRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        return BuiltInRegistries.REGISTRY.getOptional((ResourceKey) registryType);
    }

    public static <T> Registry<T> getRegistryOrThrow(ResourceKey<? extends Registry<T>> registryType)
    {
        return findRegistry(registryType).orElseThrow(() -> new IllegalStateException("Missing key in %s: %s".formatted(BuiltInRegistries.REGISTRY.key(), registryType)));
    }
}
