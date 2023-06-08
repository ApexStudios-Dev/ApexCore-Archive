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
}
