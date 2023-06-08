package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

// commonly for internal usages only
// annotations are commented out due to being applied to inheritors,
// but we do not want these annotations applied to those types
// @ApiStatus.Internal
// @ApiStatus.NonExtendable
// Note: due to the nature of delegation, inheritors may not always be the same
// as what you would pull from Registrars, rather the Registrars keep track of the entry
// this class would be delegating
public class DelegatedRegistryEntry<T> implements RegistryEntry<T>
{
    protected final RegistryEntry<T> delegate;

    public DelegatedRegistryEntry(RegistryEntry<T> delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public final RegistrarManager getRegistrarManager()
    {
        return delegate.getRegistrarManager();
    }

    @Override
    public final Registrar<T> getRegistrar()
    {
        return delegate.getRegistrar();
    }

    @Override
    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return delegate.getRegistryType();
    }

    @Override
    public final ResourceKey<T> getRegistryKey()
    {
        return delegate.getRegistryKey();
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return delegate.getRegistryName();
    }

    @Override
    public final String getRegistrationName()
    {
        return delegate.getRegistrationName();
    }

    @Override
    public final String getOwnerId()
    {
        return delegate.getOwnerId();
    }

    @Override
    public final void addListener(Runnable listener)
    {
        delegate.addListener(listener);
    }

    @Override
    public final void addListener(Consumer<T> listener)
    {
        delegate.addListener(listener);
    }

    @Override
    public final T get()
    {
        return delegate.get();
    }

    @Override
    public final Holder<T> getHolderOrThrow()
    {
        return delegate.getHolderOrThrow();
    }

    @Override
    public final Optional<Holder<T>> getHolderOptional()
    {
        return delegate.getHolderOptional();
    }

    @Nullable
    @Override
    public final T getNullable()
    {
        return delegate.getNullable();
    }

    @Override
    public final boolean isPresent()
    {
        return delegate.isPresent();
    }

    @Override
    public final boolean isEmpty()
    {
        return delegate.isEmpty();
    }

    @Override
    public final void ifPresent(Consumer<? super T> action)
    {
        delegate.ifPresent(action);
    }

    @Override
    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
    {
        delegate.ifPresentOrElse(action, emptyAction);
    }

    @Override
    public RegistryEntry<T> filter(Predicate<? super T> predicate)
    {
        return delegate.filter(predicate);
    }

    @Override
    public final <U> Optional<U> map(Function<? super T, ? extends U> mapper)
    {
        return delegate.map(mapper);
    }

    @Override
    public final <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)
    {
        return delegate.flatMap(mapper);
    }

    @Override
    public final Optional<T> or(Supplier<? extends T> supplier)
    {
        return delegate.or(supplier);
    }

    @Override
    public RegistryEntry<T> orEntry(Supplier<? extends RegistryEntry<T>> supplier)
    {
        return delegate.orEntry(supplier);
    }

    @Override
    public final Stream<T> stream()
    {
        return delegate.stream();
    }

    @Override
    public final T orElse(T other)
    {
        return delegate.orElse(other);
    }

    @Override
    public final T orElseGet(Supplier<? extends T> supplier)
    {
        return delegate.orElseGet(supplier);
    }

    @Override
    public final T orElseThrow() throws NoSuchElementException
    {
        return delegate.orElseThrow();
    }

    @Override
    public final <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
    {
        return delegate.orElseThrow(exceptionSupplier);
    }

    @Override
    public final boolean is(T other)
    {
        return delegate.is(other);
    }

    @Override
    public final boolean is(Predicate<ResourceKey<T>> predicate)
    {
        return delegate.is(predicate);
    }

    @Override
    public final boolean is(TagKey<T> tag)
    {
        return delegate.is(tag);
    }

    @Override
    public final boolean is(ResourceKey<T> registryKey)
    {
        return delegate.is(registryKey);
    }

    @Override
    public final boolean is(ResourceLocation registryName)
    {
        return delegate.is(registryName);
    }

    @Override
    public final boolean is(RegistryEntry<T> registryEntry)
    {
        return delegate.is(registryEntry);
    }

    @Override
    public final boolean isFor(ResourceKey<? extends Registry<T>> registryType)
    {
        return delegate.isFor(registryType);
    }
}
