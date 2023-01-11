package xyz.apex.minecraft.apexcore.shared.registry.entry;

import dev.architectury.registry.registries.RegistrySupplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;
import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.shared.util.function.LazyLike;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class RegistryEntry<T> implements LazyLike<T>, Comparable<RegistryEntry<?>>, PlatformHolder
{
    protected final AbstractRegistrar<?> owner;
    protected final ResourceKey<? extends Registry<? super T>> registryType;
    protected final ResourceKey<? super T> registryKey;
    private final RegistrySupplier<T> delegate;

    public RegistryEntry(AbstractRegistrar<?> owner, RegistrySupplier<T> delegate, ResourceKey<? extends Registry<? super T>> registryType, ResourceKey<? super T> registryKey)
    {
        this.owner = owner;
        this.registryType = registryType;
        this.registryKey = registryKey;
        this.delegate = delegate;
    }

    public final AbstractRegistrar<?> getOwner()
    {
        return owner;
    }

    @Override
    public final GamePlatform platform()
    {
        return owner.platform();
    }

    @ApiStatus.Internal
    public final <T1, R extends T1> RegistryEntry<R> getSibling(ResourceKey<? extends Registry<T1>> registryType)
    {
        return getOwner().get(registryType, getRegistrationName());
    }

    @ApiStatus.Internal
    public final <T1, R extends T1, E extends RegistryEntry<R>> E getSibling(ResourceKey<? extends Registry<T1>> registryType, Class<E> registryEntryType)
    {
        var entry = getSibling(registryType);
        return cast(registryEntryType, entry);
    }

    public final ResourceKey<? extends Registry<? super T>> getRegistryType()
    {
        return registryType;
    }

    public final ResourceKey<? super T> getRegistryKey()
    {
        return registryKey;
    }

    public final ResourceLocation getRegistryName()
    {
        return registryKey.location();
    }

    public final String getRegistrationName()
    {
        return getRegistryName().getPath();
    }

    public final String getModId()
    {
        return getRegistryName().getNamespace();
    }

    @Override
    public final T get()
    {
        return delegate.get();
    }

    @Nullable
    public final T getUnchecked()
    {
        return delegate.getOrNull();
    }

    public final boolean isPresent()
    {
        return delegate.isPresent();
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
            return isPresent() && entry.isPresent() && get() == entry.get();
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

    public static <E extends RegistryEntry<?>> E cast(Class<? super E> clazz, RegistryEntry<?> entry)
    {
        if(clazz.isInstance(entry)) return (E) entry;
        throw new IllegalArgumentException("Could not convert RegistryEntry: expecting %s, found %s".formatted(clazz, entry.getClass()));
    }
}
