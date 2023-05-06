package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RegistryEntryImpl<T> implements RegistryEntry<T>
{
    private static final RegistryEntry<?> EMPTY = new RegistryEntryImpl<>();

    @Nullable
    protected final Registrar<T> registrar;
    protected final ResourceLocation registryName;
    @Nullable
    protected final ResourceKey<T> registryKey;

    /**
     * DO NOT MANUALLY CALL PUBLIC FOR INTERNAL USAGES ONLY
     */
    @ApiStatus.Internal
    public RegistryEntryImpl(Registrar<T> registrar, ResourceLocation registryName)
    {
        this.registrar = registrar;
        this.registryName = registryName;
        registryKey = ResourceKey.create(registrar.getRegistryType(), registryName);
    }

    private RegistryEntryImpl()
    {
        registrar = null;
        registryKey = null;
        registryName = new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "empty");
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public final RegistrarManager getRegistrarManager()
    {
        Validate.isTrue(!isEmpty());
        return registrar.getRegistrarManager();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public final Registrar<T> getRegistrar()
    {
        Validate.isTrue(!isEmpty());
        return registrar;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        Validate.isTrue(!isEmpty());
        return registrar.getRegistryType();
    }

    @Override
    public final ResourceKey<T> getRegistryKey()
    {
        Validate.isTrue(!isEmpty());
        return Objects.requireNonNull(registryKey);
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return registryName;
    }

    @Override
    public final String getRegistrationName()
    {
        return registryName.getPath();
    }

    @Override
    public final String getOwnerId()
    {
        return registryName.getNamespace();
    }

    @Override
    public final void addListener(Runnable listener)
    {
        addListener(value -> listener.run());
    }

    @Override
    public final void addListener(Consumer<T> listener)
    {
        RegistryApi.get().addListener(getRegistryType(), getOwnerId(), registryName, listener);
    }

    @Override
    public final T get()
    {
        return getHolderOrThrow().value();
    }

    @Override
    public final Holder<T> getHolderOrThrow()
    {
        return getHolderOptional().orElseThrow();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public final Optional<Holder<T>> getHolderOptional()
    {
        if(isEmpty()) return Optional.empty();
        // none null cause not empty
        return RegistryApi.get().getDelegate(registrar.getRegistryType(), registryKey);
    }

    @Nullable
    @Override
    public final T getNullable()
    {
        return getHolderOptional().filter(Holder::isBound).map(Holder::value).orElse(null);
    }

    @Override
    public final boolean isPresent()
    {
        return getHolderOptional().map(Holder::isBound).orElse(false);
    }

    @Override
    public final boolean isEmpty()
    {
        return this == EMPTY;
    }

    @Override
    public final void ifPresent(Consumer<? super T> action)
    {
        if(isPresent()) action.accept(get());
    }

    @Override
    public final void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
    {
        if(isPresent()) action.accept(get());
        else emptyAction.run();
    }

    @Override
    public RegistryEntry<T> filter(Predicate<? super T> predicate)
    {
        return isPresent() && predicate.test(get()) ? this : empty();
    }

    @Override
    public final <U> Optional<U> map(Function<? super T, ? extends U> mapper)
    {
        return isPresent() ? Optional.ofNullable(mapper.apply(get())) : Optional.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)
    {
        return isPresent() ? (Optional<U>) mapper.apply(get()) : Optional.empty();
    }

    @Override
    public final Optional<T> or(Supplier<? extends T> supplier)
    {
        return isPresent() ? Optional.of(get()) : Optional.ofNullable(supplier.get());
    }

    @Override
    public RegistryEntry<T> orEntry(Supplier<? extends RegistryEntry<T>> supplier)
    {
        return isPresent() ? this : supplier.get();
    }

    @Override
    public final Stream<T> stream()
    {
        return isPresent() ? Stream.of(get()) : Stream.empty();
    }

    @Override
    public final T orElse(T other)
    {
        return isPresent() ? get() : other;
    }

    @Override
    public final T orElseGet(Supplier<? extends T> supplier)
    {
        return isPresent() ? get() : supplier.get();
    }

    @Override
    public final T orElseThrow() throws NoSuchElementException
    {
        return orElseThrow(NoSuchElementException::new);
    }

    @Override
    public final <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
    {
        if(isPresent()) return get();
        throw exceptionSupplier.get();
    }

    @Override
    public final boolean is(T other)
    {
        return isPresent() && get() == other;
    }

    @Override
    public final boolean is(Predicate<ResourceKey<T>> predicate)
    {
        return getHolderOptional().map(holder -> holder.is(predicate)).orElse(false);
    }

    @Override
    public final boolean is(TagKey<T> tag)
    {
        return getHolderOptional().map(holder -> holder.is(tag)).orElse(false);
    }

    @Override
    public final boolean is(ResourceKey<T> registryKey)
    {
        return isPresent() && RegistryEntry.equals(this.registryKey, registryKey);
    }

    @Override
    public final boolean is(ResourceLocation registryName)
    {
        return isPresent() && this.registryName.equals(registryName);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public final boolean is(RegistryEntry<T> registryEntry)
    {
        if(isEmpty()) return registryEntry.isEmpty();
        if(registryEntry.isEmpty() || !registryEntry.isPresent()) return false;
        return RegistryEntry.equals(registrar.getRegistryType(), registryEntry.getRegistryType()) && RegistryEntry.equals(registryKey, registryEntry.getRegistryKey());
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public final boolean isFor(ResourceKey<? extends Registry<T>> registryType)
    {
        return RegistryEntry.equals(registrar.getRegistryType(), registryType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public final boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(obj instanceof RegistryEntry<?> registryEntry) return is((RegistryEntry) registryEntry);
        return false;
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(registrar == null ? null : registrar.getRegistryType(), registryName);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public final String toString()
    {
        return isEmpty() ? "RegistryEntry.EMPTY" : "RegistryEntry<%s>(%s)".formatted(registrar.getRegistryType(), registryKey);
    }

    @SuppressWarnings("unchecked")
    public static <T> RegistryEntry<T> empty()
    {
        return (RegistryEntry<T>) EMPTY;
    }

    public static <T> RegistryEntry<T> simple(Registrar<T> registrar, ResourceLocation registryName)
    {
        return new RegistryEntryImpl<>(registrar, registryName);
    }
}
