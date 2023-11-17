package dev.apexstudios.apexcore.common.registry;

import com.mojang.datafixers.util.Either;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DeferredHolder<T, R extends T> implements Holder<T>, OptionalLike<R>
{
    protected final ResourceKey<T> valueKey;
    @Nullable private Holder<T> delegate = null;

    protected DeferredHolder(ResourceKey<T> valueKey)
    {
        this.valueKey = valueKey;
        bind(false);
    }

    @Nullable
    protected Registry<T> getRegistry()
    {
        return (Registry<T>) BuiltInRegistries.REGISTRY.get(valueKey.registry());
    }

    protected void bind(boolean throwOnMissingRegistry)
    {
        if(delegate != null)
            return;

        var registry = getRegistry();

        if(registry != null)
            delegate = registry.getHolder(valueKey).orElse(null);
        else if(throwOnMissingRegistry)
            throw new IllegalStateException("Registry not present for %s: %s".formatted(this, valueKey.registry()));
    }

    public ResourceLocation getValueId()
    {
        return valueKey.location();
    }

    public ResourceKey<T> getValueKey()
    {
        return valueKey;
    }

    public boolean is(@Nullable T value)
    {
        return isPresent() && value == delegate.value();
    }

    public boolean is(TagKey<T> tag, Predicate<R> filter)
    {
        return is(tag) && filter.test((R) delegate.value());
    }

    public boolean is(HolderSet<T> holderSet)
    {
        return isPresent() && holderSet.contains(delegate);
    }

    public boolean is(Holder<T> holder)
    {
        return is(holder.value());
    }

    @Override
    public boolean isPresent()
    {
        bind(false);
        return delegate != null;
    }

    @Nullable
    @Override
    public R getRaw()
    {
        bind(false);
        return delegate == null ? null : (R) delegate.value();
    }

    @Override
    public R get()
    {
        return value();
    }

    @Override
    public R value()
    {
        bind(true);
        Validate.notNull(delegate, "Trying to access unbound value: %s", valueKey);
        return (R) delegate.value();
    }

    @Override
    public boolean isBound()
    {
        return isPresent() && delegate.isBound();
    }

    @Override
    public boolean is(ResourceLocation valueId)
    {
        return valueId.equals(valueKey.location());
    }

    @Override
    public boolean is(ResourceKey<T> valueKey)
    {
        return valueKey == this.valueKey;
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> filter)
    {
        return filter.test(valueKey);
    }

    @Override
    public boolean is(TagKey<T> tag)
    {
        return isPresent() && delegate.is(tag);
    }

    @Override
    public Stream<TagKey<T>> tags()
    {
        return isPresent() ? delegate.tags() : Stream.empty();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap()
    {
        return isPresent() ? delegate.unwrap() : Either.left(valueKey);
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey()
    {
        return Optional.of(valueKey);
    }

    @Override
    public Kind kind()
    {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner)
    {
        return isPresent() && delegate.canSerializeIn(owner);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        return obj instanceof DeferredHolder<?,?> other && other.valueKey.equals(valueKey);
    }

    @Override
    public int hashCode()
    {
        return valueKey.hashCode();
    }

    @Override
    public String toString()
    {
        return "DeferredHolder{%s}".formatted(valueKey);
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceKey<? extends Registry<T>> registryType, ResourceLocation valueName)
    {
        return create(ResourceKey.create(registryType, valueName));
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceLocation registryName, ResourceLocation valueName)
    {
        return create(ResourceKey.createRegistryKey(registryName), valueName);
    }

    public static <T, R extends T> DeferredHolder<T, R> create(ResourceKey<T> valueKey)
    {
        return new DeferredHolder<>(valueKey);
    }
}
