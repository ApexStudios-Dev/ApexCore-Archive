package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import com.google.errorprone.annotations.DoNotCall;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Base interface for all Registry Entries.
 * <p>
 * Not to be manually implemented, extend {@link BaseRegistryEntry}.
 * <p>
 * Basically a copy of NeoForge's DeferredHolder type, which is a deferred implementation of Mojang's {@link Holder}.
 *
 * @param <T> Type of Entry.
 */
public interface RegistryEntry<T> extends Holder<T>, Supplier<T>
{
    /**
     * @return Owning Registrar.
     */
    @ApiStatus.NonExtendable
    AbstractRegistrar<?> getRegistrar();

    /**
     * @return Owning Registry.
     */
    @ApiStatus.NonExtendable
    Registry<T> getRegistry();

    /**
     * @return Registration name.
     */
    @ApiStatus.NonExtendable
    ResourceLocation getRegistryName();

    /**
     * @return Registration key.
     */
    @ApiStatus.NonExtendable
    ResourceKey<T> getRegistryKey();

    /**
     * Mostly here to fulfill Supplier implementation. Prefer using {@link #value()}.
     *
     * @return The entry stored by this entry.
     * @throws NullPointerException If the underlying Holder has not been populated.
     */
    @ApiStatus.NonExtendable
    @Override
    T get();

    /**
     * @return The entry stored by this entry.
     * @throws NullPointerException If the underlying Holder has not been populated.
     */
    @ApiStatus.NonExtendable
    @Override
    T value();

    /**
     * @return Optional containing target entry if {@link #isPresent()}, otherwise {@linkplain Optional#empty() an empty optional}.
     */
    @ApiStatus.NonExtendable
    Optional<T> getOptional();

    /**
     * @return {@code true} if this entry has been bound, and {@link #get()} may be called.
     */
    @ApiStatus.NonExtendable
    boolean isPresent();

    /**
     * @return {@code true} if {@link #isPresent()} and underlying holder {@linkplain Holder#isBound() is bound}.
     */
    @ApiStatus.NonExtendable
    @Override
    boolean isBound();

    /**
     * @return {@code true} if passed {@link ResourceLocation} is the same as {@linkplain #getRegistryName()}.
     */
    @ApiStatus.NonExtendable
    @Override
    boolean is(ResourceLocation location);

    /**
     * @return {@code true} if passed {@link ResourceKey} is the same as {@linkplain #getRegistryKey()}.
     */
    @ApiStatus.NonExtendable
    @Override
    boolean is(ResourceKey<T> resourceKey);

    /**
     * @return {@code true} if predicate matches {@linkplain #getRegistryKey()}.
     */
    @ApiStatus.NonExtendable
    @Override
    boolean is(Predicate<ResourceKey<T>> predicate);

    /**
     * @return {@code true} if {@linkplain #isPresent()} and underlying entry is a member of the passed tag.
     */
    @ApiStatus.NonExtendable
    @Override
    boolean is(TagKey<T> tagKey);

    /**
     * @return All tags present on the underlying entry.
     */
    @ApiStatus.NonExtendable
    @Override
    Stream<TagKey<T>> tags();

    /**
     * If this entry {@linkplain #isPresent() is present}, this method returns an {@link Either#right()} containing the underlying entry.<br>
     * Otherwise, this method returns an {@link Either#left()} containing {@linkplain #getRegistryKey() this entries registration key}.
     *
     * @return The unwrapped form of this entry.
     */
    @ApiStatus.NonExtendable
    @Override
    Either<ResourceKey<T>, T> unwrap();

    /**
     * @return An optional containing {@linkplain #getRegistryKey() this entries registration key}.
     */
    @ApiStatus.NonExtendable
    @Override
    Optional<ResourceKey<T>> unwrapKey();

    @ApiStatus.NonExtendable
    @Override
    Kind kind();

    @ApiStatus.NonExtendable
    @Override
    boolean canSerializeIn(HolderOwner<T> owner);

    /**
     * Looks up and returns matching sibling entry.
     *
     * @param registryType Type of sibling Registry.
     * @return Matching sibling entry or null.
     * @param <T1> Type of sibling Registry.
     * @param <R1> Type of sibling Entry.
     */
    @ApiStatus.NonExtendable
    <T1, R1 extends T1> RegistryEntry<R1> getSibling(ResourceKey<? extends Registry<T1>> registryType);

    @ApiStatus.NonExtendable
    @ApiStatus.Internal
    @DoNotCall
    void bind(boolean throwOnMissingRegistry);

    @SuppressWarnings("unchecked")
    static <E extends RegistryEntry<?>> E cast(Class<? super E> clazz, RegistryEntry<?> registryEntry)
    {
        if(clazz.isInstance(registryEntry))
            return (E) registryEntry;

        throw new IllegalArgumentException("Could not convert RegistryEntry: expecting %s, found %s".formatted(clazz, registryEntry.getClass()));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    static <T> Registry<T> getRegistry(ResourceKey<T> registryKey)
    {
        return (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.registry());
    }
}
