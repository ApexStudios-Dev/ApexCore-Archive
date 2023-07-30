package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

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
import java.util.stream.Stream;

/**
 * Base RegistryEntry implementation, to be extended by all further implementations.
 *
 * @param <T> Type of Entry.
 */
public class BaseRegistryEntry<T> implements RegistryEntry<T>
{
    protected final AbstractRegistrar<?> registrar;
    protected final ResourceKey<T> registryKey;
    @Nullable private Holder<T> delegate = null;
    @Nullable private Registry<T> registry = null;

    @ApiStatus.Internal
    public BaseRegistryEntry(AbstractRegistrar<?> registrar, ResourceKey<T> registryKey)
    {
        this.registrar = registrar;
        this.registryKey = registryKey;
        bind();
    }

    @Override
    public final AbstractRegistrar<?> getRegistrar()
    {
        return registrar;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Registry<T> getRegistry()
    {
        if(registry == null)
        {
            var registry = BuiltInRegistries.REGISTRY.get(registryKey.registry());

            if(registry == null)
                throw new IllegalStateException("Registry not present for %s: %s".formatted(registryKey, registryKey.registry()));

            this.registry = (Registry<T>) registry;
        }

        return registry;
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return registryKey.location();
    }

    @Override
    public final ResourceKey<T> getRegistryKey()
    {
        return registryKey;
    }

    @Override
    public final T get()
    {
        return value();
    }

    @Override
    public final T value()
    {
        bind();

        if(delegate == null)
            throw new IllegalStateException("Trying to access unbound value: %s".formatted(registryKey));

        return delegate.value();
    }

    @Override
    public final Optional<T> getOptional()
    {
        return isPresent() ? Optional.of(get()) : Optional.empty();
    }

    @Override
    public final boolean isPresent()
    {
        bind();
        return delegate != null;
    }

    @Override
    public final boolean isBound()
    {
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
        return this.registryKey == registryKey;
    }

    @Override
    public final boolean is(Predicate<ResourceKey<T>> predicate)
    {
        return predicate.test(registryKey);
    }

    @Override
    public final boolean is(TagKey<T> tag)
    {
        return delegate != null && delegate.is(tag);
    }

    @Override
    public final Stream<TagKey<T>> tags()
    {
        return delegate == null ? Stream.empty() : delegate.tags();
    }

    @Override
    public final Either<ResourceKey<T>, T> unwrap()
    {
        return delegate == null ? Either.left(registryKey) : delegate.unwrap();
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
        return delegate != null && delegate.canSerializeIn(owner);
    }

    @Override
    public final <T1, R1 extends T1> RegistryEntry<R1> getSibling(ResourceKey<? extends Registry<T1>> registryType)
    {
        return registrar.get(registryType, getRegistryName().getPath());
    }

    @Override
    public final boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof RegistryEntry<?> other))
            return false;
        return registryKey == other.getRegistryKey();
    }

    @Override
    public final int hashCode()
    {
        return registryKey.hashCode();
    }

    @Override
    public final String toString()
    {
        return "RegistryEntry{%s}".formatted(registryKey);
    }

    @Override
    public final void bind()
    {
        if(delegate != null)
            return;

        getRegistry().getHolder(registryKey).ifPresent(holder -> delegate = holder);
    }
}
