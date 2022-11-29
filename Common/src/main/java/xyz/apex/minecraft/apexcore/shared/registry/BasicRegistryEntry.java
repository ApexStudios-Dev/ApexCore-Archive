package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.base.Suppliers;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BasicRegistryEntry<T> implements RegistryEntry<T>
{
    protected final ModdedRegistry<? super T> modded;
    protected final ResourceKey<T> key;

    private final List<ModdedRegistry.RegisterCallback<T>> callbacks = Lists.newArrayList();
    private Supplier<T> supplier;
    @Nullable private T value = null;
    private final Supplier<Holder<T>> holder;
    private boolean isPresent = false;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BasicRegistryEntry(ModdedRegistry<? super T> modded, ResourceKey<T> key, Supplier<T> supplier)
    {
        this.modded = modded;
        this.key = key;
        this.supplier = supplier;

        holder = Suppliers.memoize(() -> (Holder<T>) getVanillaRegistry().getHolder((ResourceKey) key).orElseThrow());
    }

    @SuppressWarnings("ConstantConditions")
    private T onRegisterInternal()
    {
        if(!isPresent)
        {
            value = supplier.get();
            supplier = () -> value;

            onRegister(value);

            callbacks.forEach(callback -> callback.onRegister(key, this, value));
            callbacks.clear();

            isPresent = true;
        }

        return value;
    }

    protected void onRegister(T value)
    {
    }

    @Override
    public final ResourceKey<? extends Registry<? super T>> getRegistryType()
    {
        return modded.getRegistryType();
    }

    @Override
    public final ResourceKey<T> getKey()
    {
        return key;
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return key.location();
    }

    @Override
    public final ModdedRegistry<? super T> getModdedRegistry()
    {
        return modded;
    }

    @Override
    public final Registry<? super T> getVanillaRegistry()
    {
        return modded.asVanilla();
    }

    @Override
    public final Holder<T> getHolder()
    {
        return Objects.requireNonNull(holder.get());
    }

    @Override
    public final T get()
    {
        return onRegisterInternal();
    }

    @Override
    public final boolean isPresent()
    {
        return isPresent && value != null;
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
    public final Optional<T> filter(Predicate<? super T> predicate)
    {
        return isPresent() && predicate.test(get()) ? Optional.of(get()) : Optional.empty();
    }

    @Override
    public final <U> Optional<U> map(Function<? super T, ? extends U> mapper)
    {
        if(isPresent()) return Optional.ofNullable(mapper.apply(get()));
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)
    {
        if(isPresent()) return (Optional<U>) mapper.apply(get());
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)
    {
        return isPresent() ? Optional.of(get()) : (Optional<T>) supplier.get();
    }

    @Override
    public final Stream<T> stream()
    {
        return isPresent() ? Stream.of(get()) : Stream.empty();
    }

    @Override
    public final T orElse(T value)
    {
        return isPresent() ? get() : value;
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
    public final boolean isFor(Registry<?> vanilla)
    {
        return modded.isFor(vanilla);
    }

    @Override
    public final boolean isFor(ResourceKey<Registry<?>> type)
    {
        return modded.isFor(type);
    }

    @Override
    public final boolean isFor(ModdedRegistry<?> modded)
    {
        return this.modded.is(modded);
    }

    @Override
    public final boolean is(RegistryEntry<?> other)
    {
        return modded.getRegistryType().isFor(other.getRegistryType()) && getRegistryName().equals(other.getRegistryName());
    }

    // conflicts with child entry types, overriding and delegating to super fixes
    @Override
    public boolean is(T other)
    {
        return isPresent() && Objects.equals(get(), other);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final boolean hasTag(TagKey<? super T> tag)
    {
        return modded.hasTag((TagKey) tag, getHolder());
    }

    @Override
    public int compareTo(@NotNull RegistryEntry<?> other)
    {
        var c = modded.compareTo(other.getModdedRegistry());
        if(c == 0) return getRegistryName().compareTo(other.getRegistryName());
        return c;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(obj instanceof BasicRegistryEntry<?> other) return is(other);
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getRegistryName());
    }

    @Override
    public String toString()
    {
        return "RegistryEntry<%s>(%s)".formatted(modded.getRegistryType(), getRegistryName());
    }
}
