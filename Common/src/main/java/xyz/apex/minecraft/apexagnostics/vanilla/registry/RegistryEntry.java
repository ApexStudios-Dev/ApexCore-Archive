package xyz.apex.minecraft.apexagnostics.vanilla.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface RegistryEntry<T> extends Comparable<RegistryEntry<?>>, Supplier<T>
{
    ResourceKey<? extends Registry<? super T>> getRegistryType();
    ResourceKey<T> getKey();
    ResourceLocation getRegistryName();
    ModdedRegistry<? super T> getModdedRegistry();
    Registry<? super T> getVanillaRegistry();
    Holder<T> getHolder();

    @Override T get();
    boolean isPresent();
    void ifPresent(Consumer<? super T> action);
    void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction);
    Optional<T> filter(Predicate<? super T> predicate);
    <U> Optional<U> map(Function<? super T, ? extends U> mapper);
    <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper);
    Optional<T> or(Supplier<? extends Optional<? extends T>> supplier);
    Stream<T> stream();
    T orElse(T value);
    T orElseGet(Supplier<? extends T> supplier);
    T orElseThrow() throws NoSuchElementException;
    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    boolean isFor(Registry<?> vanilla);
    boolean isFor(ResourceKey<Registry<?>> type);
    boolean isFor(ModdedRegistry<?> modded);
    boolean is(RegistryEntry<?> other);
    boolean is(T other);

    boolean hasTag(TagKey<? super T> tag);
}
