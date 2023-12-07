package dev.apexstudios.apexcore.common.generator;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

final class ProviderTypeImpl<T extends ResourceGenerator> implements ProviderType<T>
{
    private static final Map<ResourceLocation, ProviderType<?>> PROVIDER_TYPES = Maps.newHashMap();
    static final Collection<ProviderType<?>> PROVIDER_TYPES_VIEW = Collections.unmodifiableCollection(PROVIDER_TYPES.values());

    private final ResourceLocation name;
    private final Factory<T> factory;
    private final Multimap<String, BiConsumer<T, HolderLookup.Provider>> listeners = MultimapBuilder.hashKeys().linkedListValues().build();
    private final Map<String, T> providerMap = Maps.newHashMap();

    private ProviderTypeImpl(ResourceLocation name, Factory<T> factory)
    {
        this.name = name;
        this.factory = factory;
    }

    void provideFor(String ownerId, PackOutput output, CachedOutput cache, HolderLookup.Provider registries)
    {
        var provider = providerMap.computeIfAbsent(ownerId, $ -> factory.create(ownerId, output));
        listeners.removeAll(ownerId).forEach(listener -> listener.accept(provider, registries));
        ((AbstractResourceGenerator) provider).generate(cache, registries);
    }

    @Override
    public ResourceLocation name()
    {
        return name;
    }

    @Override
    public void addListener(String ownerId, Consumer<T> listener)
    {
        addListener(ownerId, (provider, registries) -> listener.accept(provider));
    }

    @Override
    public void addListener(String ownerId, BiConsumer<T, HolderLookup.Provider> listener)
    {
        listeners.put(ownerId, listener);
    }

    @Override
    public boolean hasListeners(String ownerId)
    {
        return listeners.containsKey(ownerId);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj == this || (obj instanceof ProviderType<?> other && other.name().equals(name));
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String toString()
    {
        return "ProviderType(%s)".formatted(name);
    }

    static <T extends ResourceGenerator> ProviderType<T> register(ResourceLocation name, Factory<T> factory)
    {
        var providerType = new ProviderTypeImpl<>(name, factory);

        if(PROVIDER_TYPES.put(name, providerType) != null)
            throw new IllegalStateException("Duplicate ProviderType registration: %s".formatted(name));

        return providerType;
    }
}
