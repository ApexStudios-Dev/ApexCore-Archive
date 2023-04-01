package xyz.apex.minecraft.apexcore.common.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class DeferredRegister<T>
{
    public static final Logger LOGGER = LogManager.getLogger();

    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final String ownerId;

    private final Map<String, RegistryEntry<? extends T>> entries = Maps.newHashMap();
    private final Map<ResourceLocation, RegistryEntry<? extends T>> optionalEntries = Maps.newHashMap();
    private final Map<String, Supplier<? extends T>> factories = Maps.newHashMap();
    private final Collection<RegistryEntry<? extends T>> entriesView = Collections.unmodifiableCollection(entries.values());
    private final Collection<RegistryEntry<? extends T>> optionalEntriesView = Collections.unmodifiableCollection(optionalEntries.values());
    private final List<Runnable> registerCallbacks = Lists.newLinkedList();

    protected DeferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        this.ownerId = ownerId;
        this.registryType = registryType;
    }

    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    public final String getOwnerId()
    {
        return ownerId;
    }

    public final Collection<RegistryEntry<? extends T>> entries()
    {
        return entriesView;
    }

    public final Collection<RegistryEntry<? extends T>> optionalEntries()
    {
        return optionalEntriesView;
    }

    public final <R extends T> RegistryEntry<R> register(String registrationName, Supplier<R> registryEntryFactory)
    {
        return register(registrationName, registryName -> new RegistryEntry<>(registryType, registryName), registryEntryFactory);
    }

    public final <R extends T, E extends RegistryEntry<R>> E register(String registrationName, Function<ResourceLocation, E> registryEntryBuilder, Supplier<R> registryEntryFactory)
    {
        var entry = registryEntryBuilder.apply(new ResourceLocation(ownerId, registrationName));
        if(entries.put(registrationName, entry) != null) throw new IllegalStateException("Attempt to register duplicate registry entry with name '%s' of type '%s'".formatted(registrationName, registryType.location()));
        if(factories.put(registrationName, registryEntryFactory) != null) throw new IllegalStateException("Attempt to register duplicate registry entry with name '%s' of type '%s'".formatted(registrationName, registryType.location()));
        LOGGER.debug("[{}] Captured registration of registry entry '{}' for type '{}'", ownerId, registrationName, registryType.location());
        if(isRegistered()) updatePostRegistration(registrationName);
        return entry;
    }

    public final RegistryEntry<T> registerOptional(ResourceLocation registryName)
    {
        var entry = new RegistryEntry<>(registryType, registryName);
        if(optionalEntries.put(registryName, entry) != null) throw new IllegalStateException("Attempt to register duplicate optional registry entry with name '%s' of type '%s'".formatted(registryName, registryType.location()));
        LOGGER.debug("[{}] Captured registration of optional registry entry '{}' for type '{}'", ownerId, registryName, registryType.location());
        if(isRegistered()) updatePostRegistration(registryName);
        return entry;
    }

    public final <R extends T> void registerCallback(RegistryEntry<R> registryEntry, Consumer<R> registerCallback)
    {
        registerCallback(() -> registerCallback.accept(registryEntry.get()));
    }

    public final void registerCallback(ResourceLocation registryName, Consumer<T> registerCallback)
    {
        registerCallback(buildRegisterCallback(registryName, registerCallback));
    }

    public final void registerCallback(Runnable registerCallback)
    {
        if(isRegistered())
        {
            registerCallback.run();
            return;
        }

        registerCallbacks.add(registerCallback);
    }

    protected final void registerEntries()
    {
        LOGGER.debug("[{}] Registering registry entries of type '{}'", ownerId, registryType.location());
        factories.forEach(this::registerEntry);
        factories.clear();
    }

    private <R extends T> void registerEntry(String registrationName, Supplier<R> registryEntryFactory)
    {
        LOGGER.info("[{}] registered registry entry '{}' of type '{}'", ownerId, registrationName, registryType.location());
        var registryEntry = (RegistryEntry<R>) Objects.requireNonNull(entries.get(registrationName));
        registryEntry(registryEntry, registryEntryFactory);
    }

    protected final void updateReferences()
    {
        LOGGER.debug("[{}] updating references for registry entries of type '{}'", ownerId, registryType.location());
        entries.forEach((registrationName, registryEntry) -> updateReference(registryEntry));
        optionalEntries.forEach((registryName, registryEntry) -> updateReference(registryEntry));

        LOGGER.debug("[{}] firing register callbacks of type '{}'", ownerId, registryType.location());
        registerCallbacks.forEach(Runnable::run);
        registerCallbacks.clear();
    }

    public abstract void register();
    protected abstract boolean isRegistered();
    protected abstract <R extends T> void registryEntry(RegistryEntry<R> registryEntry, Supplier<R> registryEntryFactory);
    protected abstract Runnable buildRegisterCallback(ResourceLocation registryName, Consumer<T> registerCallback);

    protected void updateReference(RegistryEntry<? extends T> registryEntry)
    {
        LOGGER.debug("[{}] updating reference for registry entry '{}' of type '{}'", ownerId, registryEntry.registryName, registryType.location());
        var registry = findRegistry(registryType).orElseThrow();
        registryEntry.updateReference(registry.asLookup());
    }

    protected void updatePostRegistration(ResourceLocation registryName)
    {
        var entry = optionalEntries.get(registryName);
        if(entry == null || entry.isEmpty() || entry.isPresent()) return;
        LOGGER.debug("[{}] appending registry entry '{}' post registration for type '{}'", ownerId, registryName, registryType.location());
        updateReference(entry);
    }

    protected <R extends T> void updatePostRegistration(String registrationName)
    {
        var factory = factories.get(registrationName);
        if(factory == null) return; // already registered
        var entry = entries.get(registrationName);
        Validate.notNull(entry); // should never be null
        if(entry.isEmpty() || entry.isPresent()) return;
        LOGGER.debug("[{}] appending registry entry '{}' post registration for type '{}'", ownerId, registrationName, registryType.location());
        registryEntry((RegistryEntry<R>) entry, (Supplier<R>) factory);
        updateReference(entry);
    }

    public static <T> DeferredRegister<T> create(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return RegistryManager.get(ownerId).getRegistry(registryType);
    }

    public static <T> Optional<Registry<T>> findRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        return BuiltInRegistries.REGISTRY.getOptional((ResourceKey) registryType);
    }
}
