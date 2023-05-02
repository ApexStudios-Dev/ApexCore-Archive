package xyz.apex.minecraft.apexcore.common.lib.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.lib.Services;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class RegistrarImpl<T> implements Registrar<T>
{
    private final ResourceKey<? extends Registry<T>> registryType;
    private final RegistrarManager registrarManager;
    private final Map<ResourceLocation, RegistryEntry<T>> entries = Maps.newLinkedHashMap();
    private final Map<ResourceLocation, Supplier<? extends T>> factories = Maps.newHashMap();
    private final Set<ResourceLocation> keysView = Collections.unmodifiableSet(entries.keySet());
    private final Collection<RegistryEntry<T>> entriesView = Collections.unmodifiableCollection(entries.values());
    private final List<Runnable> listeners = Lists.newLinkedList();
    private boolean registered = false;

    RegistrarImpl(RegistrarManager registrarManager, ResourceKey<? extends Registry<T>> registryType)
    {
        this.registrarManager = registrarManager;
        this.registryType = registryType;
    }

    @Override
    public RegistrarManager getRegistrarManager()
    {
        return registrarManager;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    @Override
    public String getOwnerId()
    {
        return registrarManager.getOwnerId();
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries()
    {
        return entriesView;
    }

    @Override
    public Stream<RegistryEntry<T>> entries()
    {
        return entriesView.stream();
    }

    @Override
    public void addListener(Runnable listener)
    {
        if(registered) listener.run();
        else listeners.add(listener);
    }

    @Override
    public boolean isRegistered()
    {
        return registered;
    }

    private boolean shouldAppendToRegistry()
    {
        return registered || registrarManager.isRegistered();
    }

    @Override
    public void register()
    {
        if(registered) return;
        RegistryApi.LOGGER.debug("[{}] Registering registry entries of type '{}'", getOwnerId(), registryType.location());
        factories.forEach(this::registerEntry);
        factories.clear();
        listeners.forEach(Runnable::run);
        listeners.clear();
        registered = true;
    }

    @Override
    public Iterator<RegistryEntry<T>> iterator()
    {
        return entriesView.iterator();
    }

    private void registerEntry(ResourceLocation registryName, Supplier<? extends T> registryEntryFactory)
    {
        RegistryApi.LOGGER.info("[{}] Registered registry entry '{}' of type '{}'", getOwnerId(), registryName, registryType.location());
        Services.REGISTRIES.register(registryType, registryName, registryEntryFactory);
    }

    private ResourceLocation registryName(String registrationName)
    {
        return new ResourceLocation(getOwnerId(), registrationName);
    }

    // region: RegistryKey
    @Override
    public Set<ResourceKey<T>> getRegistryKeys()
    {
        return registryKeys().collect(Collectors.toSet());
    }

    @Override
    public Stream<ResourceKey<T>> registryKeys()
    {
        return keysView.stream().map(registryName -> ResourceKey.create(registryType, registryName));
    }

    @Override
    public RegistryEntry<T> registerOptional(ResourceKey<T> registryKey)
    {
        return registerOptional(registryKey.location());
    }

    @Override
    public RegistryEntry<T> get(ResourceKey<T> registryKey)
    {
        return get(registryKey.location());
    }

    @Override
    public boolean containsKey(ResourceKey<T> registryKey)
    {
        return containsKey(registryKey.location());
    }

    @Override
    public void addListener(ResourceKey<T> registryKey, Consumer<? super RegistryEntry<? extends T>> listener)
    {
        addListener(() -> listener.accept(get(registryKey)));
    }
    // endregion

    // region: RegistryName
    @Override
    public Set<ResourceLocation> getRegistryNames()
    {
        return keysView;
    }

    @Override
    public Stream<ResourceLocation> registryNames()
    {
        return keysView.stream();
    }

    @Override
    public RegistryEntry<T> registerOptional(ResourceLocation registryName)
    {
        var registryEntry = new RegistryEntryImpl<>(this, registryName);
        if(entries.put(registryName, registryEntry) != null)
            throw new IllegalStateException("Attempt to register duplicate registry entry with name '%s' of type '%s'".formatted(registryName, registryType.location()));
        RegistryApi.LOGGER.debug("[{}] Captured registration of optional registry entry '{}' for type '{}'", getOwnerId(), registryType.location(), registryType.location());
        return registryEntry;
    }

    @Override
    public RegistryEntry<T> get(ResourceLocation registryName)
    {
        return entries.getOrDefault(registryName, RegistryEntry.empty());
    }

    @Override
    public boolean containsKey(ResourceLocation registryName)
    {
        return keysView.contains(registryName);
    }

    @Override
    public void addListener(ResourceLocation registryName, Consumer<? super RegistryEntry<? extends T>> listener)
    {
        addListener(() -> listener.accept(get(registryName)));
    }
    // endregion

    // region: RegistrationName
    @Override
    public Set<String> getRegistrationNames()
    {
        return registrationNames().collect(Collectors.toSet());
    }

    @Override
    public Stream<String> registrationNames()
    {
        return registryNames().map(ResourceLocation::getPath);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <R extends T> RegistryEntry<R> register(String registrationName, Supplier<R> registryEntryFactory)
    {
        var registryName = registryName(registrationName);
        var registryEntry = new RegistryEntryImpl<>((Registrar) this, registryName);
        if(entries.put(registryName, registryEntry) != null)
            throw new IllegalStateException("Attempt to register duplicate registry entry with name '%s' of type '%s'".formatted(registryName, registryType.location()));
        if(factories.put(registryName, registryEntryFactory) != null)
            throw new IllegalStateException("Attempt to register duplicate registry entry factory with name '%s' of type '%s'".formatted(registryName, registryType.location()));
        RegistryApi.LOGGER.debug("[{}] Captured registration of registry entry '{}' for type '{}'", getOwnerId(), registryName, registryType.location());
        if(shouldAppendToRegistry()) registerEntry(registryName, registryEntryFactory);
        return registryEntry;
    }

    @Override
    public RegistryEntry<T> get(String registrationName)
    {
        return get(registryName(registrationName));
    }

    @Override
    public boolean containsKey(String registrationName)
    {
        return containsKey(registryName(registrationName));
    }

    @Override
    public void addListener(String registrationName, Consumer<? super RegistryEntry<? extends T>> listener)
    {
        addListener(() -> listener.accept(get(registrationName)));
    }
    // endregion
}
