package xyz.apex.minecraft.apexcore.common.lib.resgen;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public sealed interface ProviderType<P extends DataProvider>
{
    ResourceLocation providerName();

    void addListener(String ownerId, ProviderListener<P> listener);

    boolean hasListeners(String ownerId);

    @ApiStatus.Internal
    @DoNotCall
    void gather(String ownerId, P provider, ProviderLookup lookup);

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
        private final Multimap<String, ProviderListener<P>> listeners = MultimapBuilder.hashKeys().linkedListValues().build();
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
        public void addListener(String ownerId, ProviderListener<P> listener)
        {
            listeners.put(ownerId, listener);
        }

        @Override
        public boolean hasListeners(String ownerId)
        {
            return !listeners.get(ownerId).isEmpty();
        }

        @Override
        public void gather(String ownerId, P provider, ProviderLookup lookup)
        {
            listeners.get(ownerId).forEach(listener -> listener.accept(provider, lookup));
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
}
