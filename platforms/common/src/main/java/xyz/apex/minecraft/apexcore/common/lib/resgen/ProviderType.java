package xyz.apex.minecraft.apexcore.common.lib.resgen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.errorprone.annotations.DoNotCall;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegistryHooks;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public sealed interface ProviderType<P extends DataProvider>
{
    ResourceLocation providerName();

    void addListener(ProviderListener<P> listener);

    <T, R extends T> void addMiscListener(ResourceKey<T> registryKey, ProviderRegistryListener<P, T, R> listener);

    <T, R extends T> void addListener(ResourceKey<T> registryKey, ProviderRegistryListener<P, T, R> listener);

    <T, R extends T> void setListener(ResourceKey<T> registryKey, ProviderRegistryListener<P, T, R> listener);

    <T> void clearListener(ResourceKey<T> registryKey);

    boolean hasListeners();

    @ApiStatus.Internal
    @DoNotCall
    void provide(P provider, ProviderLookup lookup);

    @ApiStatus.Internal
    @DoNotCall
    P create(ProviderContext context);

    Collection<ProviderType<?>> parents();

    static <P extends DataProvider> ProviderType<P> register(ResourceLocation providerName, Function<ProviderContext, P> providerFactory, ProviderType<?>... parents)
    {
        var providerType = new ProviderTypeImpl<>(providerName, providerFactory, parents);

        if(ProviderTypeImpl.PROVIDER_TYPES.put(providerName, providerType) != null)
            throw new IllegalStateException("Attempt to register ProviderType with duplicate name: '%s'".formatted(providerName));

        return providerType;
    }

    static Collection<ProviderType<?>> providerTypes()
    {
        return Collections.unmodifiableCollection(ProviderTypeImpl.PROVIDER_TYPES.values());
    }

    @ApiStatus.Internal
    final class ProviderTypeImpl<P extends DataProvider> implements ProviderType<P>
    {
        private static final Map<ResourceLocation, ProviderType<?>> PROVIDER_TYPES = Maps.newHashMap();

        private final ResourceLocation providerName;
        private final Function<ProviderContext, P> providerFactory;
        private final List<ProviderListener<P>> listeners = Lists.newLinkedList();
        private final Multimap<ResourceKey<?>, ProviderRegistryListener<P, ?, ?>> registryListeners = MultimapBuilder.hashKeys().linkedListValues().build();
        private final Collection<ProviderType<?>> parents;

        private ProviderTypeImpl(ResourceLocation providerName, Function<ProviderContext, P> providerFactory, ProviderType<?>... parents)
        {
            this.providerName = providerName;
            this.providerFactory = providerFactory;
            this.parents = List.of(parents);
        }

        @Override
        public ResourceLocation providerName()
        {
            return providerName;
        }

        @Override
        public void addListener(ProviderListener<P> listener)
        {
            listeners.add(listener);
        }

        @Override
        public <T, R extends T> void addMiscListener(ResourceKey<T> registryKey, ProviderRegistryListener<P, T, R> listener)
        {
            addListener((provider, lookup) -> listener.accept(provider, lookup, buildRegistryContext(registryKey)));
        }

        @Override
        public <T, R extends T> void addListener(ResourceKey<T> registryKey, ProviderRegistryListener<P, T, R> listener)
        {
            registryListeners.put(registryKey, listener);
        }

        @Override
        public <T, R extends T> void setListener(ResourceKey<T> registryKey, ProviderRegistryListener<P, T, R> listener)
        {
            clearListener(registryKey);
            addListener(registryKey, listener);
        }

        @Override
        public <T> void clearListener(ResourceKey<T> registryKey)
        {
            registryListeners.removeAll(registryKey);
        }

        @Override
        public boolean hasListeners()
        {
            return !listeners.isEmpty() || !registryListeners.isEmpty();
        }

        @Override
        public void provide(P provider, ProviderLookup lookup)
        {
            listeners.forEach(listener -> listener.accept(provider, lookup));
            registryListeners.keys().forEach(registryKey -> provide(registryKey, provider, lookup));
        }

        @SuppressWarnings("unchecked")
        private <T, R extends T> RegistryContext<T, R> buildRegistryContext(ResourceKey<T> registryKey)
        {
            var registryType = ResourceKey.<T>createRegistryKey(registryKey.registry());
            var registry = RegistryHooks.findVanillaRegistry(registryType).orElseThrow();

            return new RegistryContext<T, R>(
                    registryType,
                    registryKey,
                    registryKey.location(),
                    (Holder<R>) registry.getHolderOrThrow(registryKey)
            );
        }

        @SuppressWarnings("unchecked")
        private <T, R extends T> void provide(ResourceKey<T> registryKey, P provider, ProviderLookup lookup)
        {
            var registryContext = this.<T, R>buildRegistryContext(registryKey);
            registryListeners.get(registryKey).forEach(listener -> ((ProviderRegistryListener<P, T, R>) listener).accept(provider, lookup, registryContext));
        }

        @Override
        public P create(ProviderContext context)
        {
            return providerFactory.apply(context);
        }

        @Override
        public Collection<ProviderType<?>> parents()
        {
            return parents;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj)
                return true;
            if(!(obj instanceof ProviderType<?> other))
                return false;
            return providerName.equals(other.providerName());
        }

        @Override
        public int hashCode()
        {
            return providerName.hashCode();
        }

        @Override
        public String toString()
        {
            return "ProviderType<%s>".formatted(providerName);
        }
    }

    record ProviderContext(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String ownerId) { }

    record RegistryContext<T, R extends T>(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey, ResourceLocation registryName, Holder<R> registryHolder) implements Supplier<R>, Holder<R>
    {
        @Override
        public R get()
        {
            return value();
        }

        @Override
        public R value()
        {
            return registryHolder.value();
        }

        @Override
        public boolean isBound()
        {
            return registryHolder.isBound();
        }

        @Override
        public boolean is(ResourceLocation registryName)
        {
            return registryHolder.is(registryName);
        }

        @Override
        public boolean is(ResourceKey<R> registryKey)
        {
            return registryHolder.is(registryKey);
        }

        @Override
        public boolean is(Predicate<ResourceKey<R>> predicate)
        {
            return registryHolder.is(predicate);
        }

        @Override
        public boolean is(TagKey<R> tag)
        {
            return registryHolder.is(tag);
        }

        @Override
        public Stream<TagKey<R>> tags()
        {
            return registryHolder.tags();
        }

        @Override
        public Either<ResourceKey<R>, R> unwrap()
        {
            return registryHolder.unwrap();
        }

        @Override
        public Optional<ResourceKey<R>> unwrapKey()
        {
            return registryHolder.unwrapKey();
        }

        @Override
        public Kind kind()
        {
            return registryHolder.kind();
        }

        @Override
        public boolean canSerializeIn(HolderOwner<R> owner)
        {
            return registryHolder.canSerializeIn(owner);
        }
    }
}
