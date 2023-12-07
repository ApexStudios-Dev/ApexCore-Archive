package dev.apexstudios.apexcore.common.registry;

import com.mojang.datafixers.util.Either;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import dev.apexstudios.apexcore.common.util.PlatformTag;
import net.covers1624.quack.annotation.ReplaceWith;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredHolder<T, R extends T> implements Holder<T>, OptionalLike<R>
{
    protected final String ownerId;
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceKey<T> registryKey;
    @Nullable private Holder<T> delegate = null;

    protected DeferredHolder(String ownerId, ResourceKey<T> registryKey)
    {
        this.ownerId = ownerId;
        this.registryKey = registryKey;
        registryType = ResourceKey.createRegistryKey(registryKey.registry());
        bind(false);
    }

    public final ResourceKey<? extends Registry<T>> registryType()
    {
        return registryType;
    }

    public final ResourceLocation registryName()
    {
        return registryKey.location();
    }

    public final ResourceKey<T> registryKey()
    {
        return registryKey;
    }

    public final boolean is(@Nullable T value)
    {
        bind(false);
        return delegate != null && delegate.value() == value;
    }

    public final boolean is(PlatformTag<T> tag, Predicate<R> filter)
    {
        return is(tag) && filter.test((R) delegate.value());
    }

    public final boolean is(PlatformTag<T> tag)
    {
        bind(false);
        return delegate != null && tag.contains(delegate);
    }

    public final boolean is(TagKey<T> tag, Predicate<R> filter)
    {
        return is(tag) && filter.test((R) delegate.value());
    }

    public final boolean is(HolderSet<T> holderSet)
    {
        bind(false);
        return delegate != null && holderSet.contains(delegate);
    }

    public final boolean is(Holder<T> holder)
    {
        return is(holder.value());
    }

    @Override
    public final R value()
    {
        bind(true);
        Validate.notNull(delegate, "Trying to access unbound value: %s", registryKey);
        return (R) delegate.value();
    }

    @Override
    public final boolean isBound()
    {
        bind(false);
        return delegate != null && delegate.isBound();
    }

    @Override
    public final boolean is(ResourceLocation registryName)
    {
        return registryName.equals(registryKey.location());
    }

    @Override
    public final boolean is(ResourceKey<T> registryKey)
    {
        return registryKey == this.registryKey;
    }

    @Override
    public final boolean is(Predicate<ResourceKey<T>> filter)
    {
        return filter.test(registryKey);
    }

    @Override
    public final boolean is(TagKey<T> tag)
    {
        bind(false);
        return delegate != null && delegate.is(tag);
    }

    @Override
    public final Stream<TagKey<T>> tags()
    {
        bind(false);
        return delegate != null ? delegate.tags() : Stream.empty();
    }

    @Override
    public final Either<ResourceKey<T>, T> unwrap()
    {
        return Either.left(registryKey);
    }

    @Override
    public final Optional<ResourceKey<T>> unwrapKey()
    {
        return Optional.of(registryKey);
    }

    @Override
    public final Kind kind()
    {
        return Kind.REFERENCE;
    }

    @Override
    public final boolean canSerializeIn(HolderOwner<T> owner)
    {
        bind(false);
        return delegate != null && delegate.canSerializeIn(owner);
    }

    @Nullable
    @Override
    public final R getRaw()
    {
        bind(false);
        return delegate == null ? null : (R) delegate.value();
    }

    @Deprecated
    @ReplaceWith("#value()")
    @Override
    public final R get()
    {
        return value();
    }

    @Deprecated
    @ReplaceWith("#isBound()")
    @Override
    public final boolean isPresent()
    {
        bind(false);
        return delegate != null;
    }

    @Override
    public final boolean isEmpty()
    {
        return !isBound();
    }

    @Override
    public final void ifPresent(Consumer<? super R> action)
    {
        bind(false);

        if(delegate != null)
            action.accept((R) delegate.value());
    }

    @Override
    public final void ifPresentOrElse(Consumer<? super R> action, Runnable emptyAction)
    {
        bind(false);

        if(delegate == null)
            emptyAction.run();
        else
            action.accept((R) delegate.value());
    }

    @Override
    public final OptionalLike<R> filter(Predicate<? super R> filter)
    {
        bind(false);
        return delegate == null ? OptionalLike.empty() : this;
    }

    @Override
    public final <U> OptionalLike<U> map(Function<? super R, ? extends U> mapper)
    {
        bind(false);
        return delegate == null ? OptionalLike.empty() : OptionalLike.of(mapper.apply((R) delegate.value()));
    }

    @Override
    public final <U> OptionalLike<U> flatMap(Function<? super R, ? extends OptionalLike<? extends U>> mapper)
    {
        bind(false);
        return delegate == null ? OptionalLike.empty() : (OptionalLike<U>) mapper.apply((R) delegate.value());
    }

    @Override
    public final OptionalLike<R> or(Supplier<? extends OptionalLike<? extends R>> supplier)
    {
        bind(false);
        return delegate == null ? (OptionalLike<R>) supplier.get() : this;
    }

    @Override
    public final Stream<R> stream()
    {
        bind(false);
        return delegate == null ? Stream.empty() : Stream.of((R) delegate.value());
    }

    @Override
    public final R orElse(R other)
    {
        bind(false);
        return delegate == null ? other : (R) delegate.value();
    }

    @Override
    public final R orElseGet(Supplier<? extends R> supplier)
    {
        bind(false);
        return delegate == null ? supplier.get() : (R) delegate.value();
    }

    @Override
    public final R orElseThrow()
    {
        bind(true);
        return (R) delegate.value();
    }

    @Override
    public final <X extends Throwable> R orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
    {
        bind(false);

        if(delegate == null)
            throw exceptionSupplier.get();

        return (R) delegate.value();
    }

    @Override
    public final Optional<R> toOptional()
    {
        bind(false);
        return delegate == null ? Optional.empty() : Optional.of((R) delegate.value());
    }

    protected final void bind(boolean throwOnMissing)
    {
        if(delegate != null)
            return;

        delegate = RegistryHelper.get(ownerId).getDelegate(registryKey).orElse(null);

        if(delegate == null && throwOnMissing)
            throw new IllegalStateException("Registry delegate not present for %s: %s".formatted(this, registryKey.registry()));
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        return obj instanceof DeferredHolder<?,?> other && other.registryKey.equals(registryKey);
    }

    @Override
    public int hashCode()
    {
        return registryKey.hashCode();
    }

    @Override
    public String toString()
    {
        return "DeferredHolder{%s}".formatted(registryKey);
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceKey<? extends Registry<T>> registryType, String ownerId, ResourceLocation registryName)
    {
        return create(ownerId, ResourceKey.create(registryType, registryName));
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName)
    {
        return create(registryType, registryName.getNamespace(), registryName);
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceLocation registryTypeName, String ownerId, ResourceLocation registryName)
    {
        return create(ResourceKey.createRegistryKey(registryTypeName), ownerId, registryName);
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceLocation registryTypeName, ResourceLocation registryName)
    {
        return create(registryTypeName, registryName.getNamespace(), registryName);
    }

    public static <T, R extends T> DeferredHolder<T, R> create(String ownerId, ResourceKey<T> registryKey)
    {
        return new DeferredHolder<>(ownerId, registryKey);
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceKey<T> registryKey)
    {
        return create(registryKey.location().getNamespace(), registryKey);
    }
}
