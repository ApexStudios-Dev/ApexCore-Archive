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
import java.util.function.*;
import java.util.stream.Stream;

public sealed interface ProviderType<P extends DataProvider>
{
    ResourceLocation providerName();

    void addListener(Consumer<P> listener);

    <T, R extends T> void addListener(ResourceKey<R> registryKey, BiConsumer<P, RegistryContext<T, R>> listener);

    <T, R extends T> void setListener(ResourceKey<R> registryKey, BiConsumer<P, RegistryContext<T, R>> listener);

    <T, R extends T> void clearListener(ResourceKey<R> registryKey);

    @ApiStatus.Internal
    @DoNotCall
    void provide(P provider);

    @ApiStatus.Internal
    @DoNotCall
    P create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries);

    static <P extends DataProvider> ProviderType<P> forRegistries(ResourceLocation providerName, BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, P> providerFactory)
    {
        var providerType = new ProviderTypeImpl<>(providerName, providerFactory);

        if(ProviderTypeImpl.PROVIDER_TYPES.put(providerName, providerType) != null)
            throw new IllegalStateException("Attempt to register ProviderType with duplicate name: '%s'".formatted(providerName));

        return providerType;
    }

    static <P extends DataProvider> ProviderType<P> simple(ResourceLocation providerName, Function<PackOutput, P> providerFactory)
    {
        return forRegistries(providerName, (packOutput, completableFuture) -> providerFactory.apply(packOutput));
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
        private final BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, P> providerFactory;
        private final List<Consumer<P>> listeners = Lists.newLinkedList();
        private final Multimap<ResourceKey<?>, BiConsumer<P, ? extends RegistryContext<?, ?>>> registryListeners = MultimapBuilder.hashKeys().linkedListValues().build();

        private ProviderTypeImpl(ResourceLocation providerName, BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, P> providerFactory)
        {
            this.providerName = providerName;
            this.providerFactory = providerFactory;
        }

        @Override
        public ResourceLocation providerName()
        {
            return providerName;
        }

        @Override
        public void addListener(Consumer<P> listener)
        {
            listeners.add(listener);
        }

        @Override
        public <T, R extends T> void addListener(ResourceKey<R> registryKey, BiConsumer<P, RegistryContext<T, R>> listener)
        {
            registryListeners.put(registryKey, listener);
        }

        @Override
        public <T, R extends T> void setListener(ResourceKey<R> registryKey, BiConsumer<P, RegistryContext<T, R>> listener)
        {
            clearListener(registryKey);
            addListener(registryKey, listener);
        }

        @Override
        public <T, R extends T> void clearListener(ResourceKey<R> registryKey)
        {
            registryListeners.removeAll(registryKey);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void provide(P provider)
        {
            listeners.forEach(listener -> listener.accept(provider));
            registryListeners.forEach((registryKey, listener) -> provide((ResourceKey) registryKey, provider, (BiConsumer) listener));
        }

        @SuppressWarnings("unchecked")
        private <T, R extends T> void provide(ResourceKey<R> registryKey, P provider, BiConsumer<P, RegistryContext<T, R>> listener)
        {
            var registryType = ResourceKey.<T>createRegistryKey(registryKey.registry());
            var registry = RegistryHooks.findVanillaRegistry(registryType).orElseThrow();

            var context = new RegistryContext<T, R>(
                    registryType,
                    registryKey,
                    registryKey.location(),
                    (Holder<R>) registry.getHolderOrThrow((ResourceKey<T>) registryKey)
            );

            listener.accept(provider, context);
        }

        @Override
        public P create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
        {
            return providerFactory.apply(packOutput, registries);
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

    record RegistryContext<T, R extends T>(ResourceKey<? extends Registry<T>> registryType, ResourceKey<R> registryKey, ResourceLocation registryName, Holder<R> registryHolder) implements Supplier<R>, Holder<R>
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
