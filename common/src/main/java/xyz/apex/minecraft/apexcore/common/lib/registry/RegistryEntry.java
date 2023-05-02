package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Registry entry used to manage a singular registered game object.
 * <p>
 * Use {@link Registrar#registerOptional(ResourceKey)} or {@link Registrar#registerOptional(ResourceLocation)} it is possible to register a optional registry entry.
 * These registry entries do not register any new game objects to the game, but rather lookup values from external sources,
 * meaning that optional registry entries may not always contain a registered game object, if the originating source that should have registered said object did not register the object correctly.
 * <br>
 * This is commonly due to the originating mod not being loaded or missing.
 * <br>
 * When using optional registry entries it is recommended to not make use of {@link #get()} where possible, as that will throw a {@link NoSuchElementException} if the required game object is missing.
 *
 * @param <T> Base game object type this registry entry is for.
 */
@ApiStatus.NonExtendable
public interface RegistryEntry<T> extends Supplier<T>
{
    /**
     * @return Registrar manager this registry entry is bound to.
     * @throws IllegalArgumentException if this registry entry is empty.
     */
    RegistrarManager getRegistrarManager();

    /**
     * @return Registrar this registry entry is bound to.
     * @throws IllegalArgumentException if this registry entry is empty.
     */
    Registrar<T> getRegistrar();

    /**
     * @return Type of registry this registry entry is bound to.
     * @throws IllegalArgumentException if this registry entry is empty.
     */
    ResourceKey<? extends Registry<T>> getRegistryType();

    /**
     * @return Registry key for this registry entry.
     * @throws IllegalArgumentException if this registry entry is empty.
     */
    ResourceKey<T> getRegistryKey();

    /**
     * @return Registry name for this registry entry.
     */
    ResourceLocation getRegistryName();

    /**
     * @return Registration name for this registry entry.
     */
    String getRegistrationName();

    /**
     * @return ID of mod owning this registry entry.
     */
    String getOwnerId();

    /**
     * Registers a listener to be invoked after this registry entry is registered.
     * <p>
     * Invokes the listener immediately if this registry entry has already been registered.
     * <p>
     * Does nothing for empty registry entries.
     *
     * @param listener Listener to be registered.
     */
    void addListener(Runnable listener);

    /**
     * Registers a listener to be invoked after this registry entry is registered.
     * <p>
     * Invokes the listener immediately if this registry entry has already been registered.
     * <p>
     * Game object bound to this registry entry is passed to the given listener.
     * <p>
     * Does nothing for empty registry entries.
     *
     * @param listener Listener to be registered.
     */
    void addListener(Consumer<T> listener);

    /**
     * @return Game object instance bound to this registry entry.
     * @throws NoSuchElementException If game object has not yet been registered or could not be found.
     */
    @Override
    T get();

    /**
     * @return Matching holder for this registry entry.
     * @throws NoSuchElementException If no holder could be found.
     */
    Holder<T> getHolderOrThrow();

    /**
     * @return Looks up matching holder for this registry entry or empty.
     */
    Optional<Holder<T>> getHolderOptional();

    /**
     * @return Registered game object instance or null.
     */
    @Nullable T getNullable();

    /**
     * @return True if this registry entry is not empty and has been registered.
     */
    boolean isPresent();

    /**
     * @return True if this registry entry is empty.
     */
    boolean isEmpty();

    /**
     * Invokes the given action if this registry entry is present.
     *
     * @param action Action to be invoked if this registry entry is present.
     */
    void ifPresent(Consumer<? super T> action);

    /**
     * Invokes the given action if this registry entry is present otherwise invokes the given emptyAction.
     *
     * @param action      Action to be invoked if this registry entry is present.
     * @param emptyAction Empty action to be invoked if this registry entry is not present.
     */
    void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction);

    /**
     * Returns this registry entry if it is present and matches the given predicate otherwise empty.
     *
     * @param predicate Predicate used to validate this registry entry against.
     * @return This registry entry if present and matching the given predicate otherwise empty.
     */
    RegistryEntry<T> filter(Predicate<? super T> predicate);

    /**
     * Returns optional holding mapped result of this registry entry if present otherwise empty.
     *
     * @param mapper Mapper used to map this registry entry.
     * @param <U>    Type to map this registry entry to.
     * @return Optional holding mapped result of this registry entry if present otherwise empty.
     */
    <U> Optional<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Returns optional holding mapped result of this registry entry if present otherwise empty.
     *
     * @param mapper Mapper used to map this registry entry.
     * @param <U>    Type to map this registry entry to.
     * @return Optional holding mapped result of this registry entry if present otherwise empty.
     */
    <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper);

    /**
     * Optional holding result of this registry entry or supplier if not present.
     *
     * @param supplier Supplier used to lookup value if this registry entry is not present.
     * @return Optional holding result of this registry entry or supplier if not present.
     */
    Optional<T> or(Supplier<? extends T> supplier);

    /**
     * Registry entry holding result of this registry entry or supplier if not present.
     *
     * @param supplier Supplier used to lookup value if this registry entry is not present.
     * @return Registry entry holding result of this registry entry or supplier if not present.
     */
    RegistryEntry<T> orEntry(Supplier<? extends RegistryEntry<T>> supplier);

    /**
     * @return Stream containing this registry entry value or empty if not present.
     */
    Stream<T> stream();

    /**
     * Returns this registry entry value or other if not present.
     *
     * @param other Value to be returned if this registry entry is not present.
     * @return This registry entry value or other if not present.
     */
    T orElse(T other);

    /**
     * Returns this registry entry value or result of given supplier if not present.
     *
     * @param supplier Supplier used to lookup value if this registry entry is not present.
     * @return This registry entry value or result of given supplier if not present.
     */
    T orElseGet(Supplier<? extends T> supplier);

    /**
     * Returns this registry entry value or throws exception if not present.
     *
     * @return This registry entry value.
     * @throws NoSuchElementException If this registry entry is not present.
     */
    T orElseThrow() throws NoSuchElementException;

    /**
     * Returns this registry entry value or throws exception if not present.
     *
     * @param exceptionSupplier Supplier used to construct exception.
     * @param <X>               Type of exception to be thrown.
     * @return This registry entry value.
     * @throws X If this registry entry is not present.
     */
    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * Returns true if this registry entry is present and matches the given value.
     *
     * @param other Value to validate this registry entry against.
     * @return True if this registry entry is present and matches the given value.
     */
    boolean is(T other);

    /**
     * Returns true if this registry entry is present and matches the given predicate.
     *
     * @param predicate Predicate to validate this registry entry against.
     * @return True if this registry entry is present and matches the given predicate.
     */
    boolean is(Predicate<ResourceKey<T>> predicate);

    /**
     * Returns true if this registry entry is present and tagged with the given tag.
     *
     * @param tag Tag to check validate this registry entry is tagged with.
     * @return True if this registry entry is present and tagged with the given tag.
     */
    boolean is(TagKey<T> tag);

    /**
     * Returns true if this registry entry is bound to the given registry key.
     *
     * @param registryKey Registry key to validate this registry entry against.
     * @return True if this registry entry is bound to the given registry key.
     */
    boolean is(ResourceKey<T> registryKey);

    /**
     * Returns true if this registry entry is bound to the given registry name.
     *
     * @param registryName Registry name to validate this registry entry against.
     * @return True if this registry entry is bound to the given registry name.
     */
    boolean is(ResourceLocation registryName);

    /**
     * Returns true if this registry entry matches the given registry entry.
     *
     * @param registryEntry Registry entry to validate this registry entry against.
     * @return True if this registry entry matches the given registry entry.
     */
    boolean is(RegistryEntry<T> registryEntry);

    /**
     * Returns true if this registry entry is bound to the given registry type.
     *
     * @param registryType Registry type to validate this registry entry against.
     * @return True if this registry entry is bound to the given registry type.
     */
    boolean isFor(ResourceKey<? extends Registry<T>> registryType);

    /**
     * @param <T> Type of registry entry.
     * @return Empty registry entry.
     */
    static <T> RegistryEntry<T> empty()
    {
        return RegistryEntryImpl.empty();
    }

    /**
     * Attempts to cast given registry entry type to given type, throwing exception if can not be cast to type.
     *
     * @param registryEntry         Registry entry to cast.
     * @param castRegistryEntryType Type to cast registry entry to.
     * @param <E>                   Type of registry entry after being cast.
     * @return Registry entry cast to given type.
     * @throws IllegalStateException If could not cast registry entry to given type.
     */
    @SuppressWarnings("unchecked")
    static <E extends RegistryEntry<?>> E cast(RegistryEntry<?> registryEntry, Class<? super E> castRegistryEntryType)
    {
        if(castRegistryEntryType.isInstance(registryEntry)) return (E) registryEntry;
        throw new IllegalStateException("Could not convert RegistryEntry: expecting '%s', found '%s'".formatted(castRegistryEntryType, registryEntry.getClass()));
    }

    /**
     * Returns true if both of the given registry keys are of same registry type and have same location property.
     *
     * @param a first registry key to test.
     * @param b second registry key to test.
     * @return True if both of the given registry keys are of same registry type and have same location property.
     */
    static boolean equals(@Nullable ResourceKey<?> a, @Nullable ResourceKey<?> b)
    {
        if(a == null) return b == null;
        if(b == null) return false;
        return a.registry().equals(b.registry()) && a.location().equals(b.location());
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    class Delegated<T> implements RegistryEntry<T>
    {
        protected final RegistryEntry<T> delegate;

        protected Delegated(RegistryEntry<T> delegate)
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
}
