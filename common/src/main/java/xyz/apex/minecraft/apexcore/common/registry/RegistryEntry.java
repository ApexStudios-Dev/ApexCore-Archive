package xyz.apex.minecraft.apexcore.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes", "DataFlowIssue"})
public class RegistryEntry<T> implements Supplier<T>
{
    private static final RegistryEntry<?> EMPTY = new RegistryEntry<>();

    @Nullable protected final ResourceKey<? extends Registry<T>> registryType;
    @Nullable protected final ResourceKey<T> registryKey;
    protected final ResourceLocation registryName;
    @Nullable private Holder<T> value;

    public RegistryEntry(ResourceKey<? extends Registry<? super T>> registryType, ResourceLocation registryName)
    {
        this.registryType = ResourceKey.createRegistryKey(registryType.location()); // hack: wrap to correct typings
        this.registryName = registryName;

        registryKey = ResourceKey.create(this.registryType, registryName);
    }

    private RegistryEntry()
    {
        registryType = null;
        registryKey = null;
        registryName = new ResourceLocation("empty");
    }

    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        Validate.isTrue(!isEmpty());
        return registryType;
    }

    public final ResourceKey<T> getRegistryKey()
    {
        Validate.isTrue(!isEmpty());
        return registryKey;
    }

    public final ResourceLocation getRegistryName()
    {
        return registryName;
    }

    public final String getRegistrationName()
    {
        return registryName.getPath();
    }

    public final String getOwnerId()
    {
        return registryName.getNamespace();
    }

    @Override
    public final T get()
    {
        Validate.isTrue(!isEmpty());
        return Objects.requireNonNull(value).value();
    }

    public final Holder<T> asHolder()
    {
        Validate.isTrue(!isEmpty());
        return Objects.requireNonNull(value);
    }

    @Nullable
    public final T getNullable()
    {
        return isEmpty() || value == null || !value.isBound() ? null : value.value();
    }

    public final boolean isPresent()
    {
        return !isEmpty() && value != null && value.isBound();
    }

    public final boolean isEmpty()
    {
        return registryType == null || registryKey == null;
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

    public boolean is(T other)
    {
        return getNullable() == other;
    }

    public final boolean is(Predicate<ResourceKey<T>> predicate)
    {
        return isPresent() && asHolder().is(predicate);
    }

    public final boolean is(TagKey<?> tag)
    {
        return isPresent() && isFor(tag.registry()) && asHolder().is((TagKey<T>) tag);
    }

    public final boolean is(ResourceKey<?> registryKey)
    {
        return !isEmpty() && equals(this.registryKey, registryKey);
    }

    public final boolean is(ResourceLocation registryName)
    {
        return this.registryName.equals(registryName);
    }

    public final boolean is(RegistryEntry<?> other)
    {
        if(isEmpty()) return other.isEmpty();
        if(other.isEmpty()) return false;
        return equals(registryType, other.registryType) && equals(registryKey, other.registryKey);
    }

    public final boolean isFor(ResourceKey<? extends Registry<?>> registryType)
    {
        return !isEmpty() && this.registryType.isFor(registryType);
    }

    private boolean equals(ResourceKey<?> a,ResourceKey<?> b)
    {
        return a.registry().equals(b.registry()) && a.location().equals(b.location());
    }

    @Override
    public final boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(obj instanceof RegistryEntry<?> entry) return is(entry);
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
        return isEmpty() ? "RegistryEntry.EMPTY" : "RegistryEntry<%s>(%s)".formatted(registryType, registryKey);
    }

    public final void updateReference(HolderLookup.RegistryLookup<? super T> registryLookup)
    {
        if(isEmpty() || isPresent()) return;
        registryLookup.get((ResourceKey) registryKey).ifPresentOrElse(delegate -> value = (Holder<T>) delegate, () -> value = null);
    }

    public static <T> RegistryEntry<T> empty()
    {
        return (RegistryEntry<T>) EMPTY;
    }
}
