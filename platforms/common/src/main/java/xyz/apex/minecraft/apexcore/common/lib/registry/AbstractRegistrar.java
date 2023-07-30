package xyz.apex.minecraft.apexcore.common.lib.registry;

import com.google.common.collect.*;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.registry.builder.ItemBuilder;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.ItemFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class AbstractRegistrar<O extends AbstractRegistrar<O>>
{
    public static final Marker MARKER = MarkerManager.getMarker("Register");

    protected final String ownerId;
    protected final O self = (O) this;

    private boolean registered = false;
    private final Table<ResourceKey<? extends Registry<?>>, String, Registration<?, ?>> registrations = HashBasedTable.create();
    private final Multimap<Pair<ResourceKey<? extends Registry<?>>, String>, Consumer<?>> registerListeners = HashMultimap.create();
    private final Multimap<ResourceKey<? extends Registry<?>>, Runnable> afterRegisterListeners = HashMultimap.create();
    private final Set<ResourceKey<? extends Registry<?>>> completedRegistrations = Sets.newHashSet();
    @Nullable private String currentName = null;
    private boolean skipErrors = false;

    protected AbstractRegistrar(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public final void register()
    {
        Validate.isTrue(!registered, "Duplicate Registrar [{}] registration", ownerId);
        ApexCore.INSTANCE.register(this);
        registered = true;
    }

    public final String getOwnerId()
    {
        return ownerId;
    }

    public final String currentName()
    {
        return Objects.requireNonNull(currentName, "Current name not set");
    }

    public final O skipErrors(boolean skipErrors)
    {
        // TODO
        /*if(skipErrors && !isDev())
            ApexCore.LOGGER.error("Ignoring skipErrors(true) as this is not a development environment!");
        else
            this.skipErrors = skipErrors;*/

        this.skipErrors = skipErrors;
        return self;
    }

    public final O skipErrors()
    {
        return skipErrors(true);
    }

    public final O object(String registrationName)
    {
        currentName = registrationName;
        return self;
    }

    public final <T, R extends T> RegistryEntry<R> get(ResourceKey<? extends Registry<T>> registryType)
    {
        return get(registryType, currentName());
    }

    public final <T, R extends T> RegistryEntry<R> get(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return this.<T, R>registration(registryType, registrationName).registryEntry;
    }

    public final <T> Collection<RegistryEntry<T>> getAll(ResourceKey<? extends Registry<T>> registryType)
    {
        return registrations.row(registryType).values().stream().map(registration -> (RegistryEntry<T>) registration.registryEntry).toList();
    }

    public final <T, R extends T> O addRegisterListener(ResourceKey<? extends Registry<T>> registryType, String registrationName, Consumer<R> listener)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, registrationName);

        if(registration == null)
            registerListeners.put(Pair.of(registryType, registrationName), listener);
        else
            registration.addListener(listener);

        return self;
    }

    public final <T> O addRegisterListener(ResourceKey<? extends Registry<T>> registryType, Runnable listener)
    {
        afterRegisterListeners.put(registryType, listener);
        return self;
    }

    public final <T> boolean isRegistered(ResourceKey<? extends Registry<T>> registryType)
    {
        return completedRegistrations.contains(registryType);
    }

    public final <T extends Item, P> ItemBuilder<O, T, P> item(P parent, String registrationName, ItemFactory<T> itemFactory)
    {
        return new ItemBuilder<>(self, parent, registrationName, itemFactory);
    }

    public final <T extends Item, P> ItemBuilder<O, T, P> item(P parent, ItemFactory<T> itemFactory)
    {
        return item(parent, currentName(), itemFactory);
    }

    public final <T extends Item> ItemBuilder<O, T, O> item(String registrationName, ItemFactory<T> itemFactory)
    {
        return item(self, registrationName, itemFactory);
    }

    public final <T extends Item> ItemBuilder<O, T, O> item(ItemFactory<T> itemFactory)
    {
        return item(self, currentName(), itemFactory);
    }

    public final <P> ItemBuilder<O, Item, P> item(P parent, String registrationName)
    {
        return item(parent, registrationName, Item::new);
    }

    public final <P> ItemBuilder<O, Item, P> item(P parent)
    {
        return item(parent, currentName(), Item::new);
    }

    public final ItemBuilder<O, Item, O> item(String registrationName)
    {
        return item(self, registrationName, Item::new);
    }

    public final ItemBuilder<O, Item, O> item()
    {
        return item(self, currentName(), Item::new);
    }

    private <T, R extends T> Registration<T, R> registration(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, registrationName);

        if(registration == null)
            throw new IllegalArgumentException("Unknown registration %s for type %s".formatted(registrationName, registryType));

        return registration;
    }

    @Nullable
    private <T, R extends T> Registration<T, R> registrationUnchecked(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = registrations.get(registryType, registrationName);
        return registration == null ? null : (Registration<T, R>) registration;
    }

    @ApiStatus.Internal
    @DoNotCall
    public final void onRegisterPre(@Nullable ResourceKey<? extends Registry<?>> registryType, RegistryHelper registryHelper)
    {
        if(registryType == null)
        {
            ApexCore.LOGGER.debug(MARKER, "Skipping invalid registry with no supertype");
            return;
        }

        if(!registerListeners.isEmpty())
        {
            registerListeners.asMap().forEach((k, v) -> ApexCore.LOGGER.warn(MARKER, "Found {} unused register callback(s) for entry {} [{}]. Was the entry ever registered?", v.size(), k.getValue(), k.getKey().location()));
            registerListeners.clear();

            // TODO
            /*if(isDev())
                throw new IllegalStateException("Found unused register callbacks, see logs");*/

            return;
        }

        var registrationsForType = registrations.row(registryType);

        if(!registrationsForType.isEmpty())
        {
            ApexCore.LOGGER.debug(MARKER, "Registering {} known objects of type {}", registrationsForType.size(), registryType.location());

            for(var registration : registrationsForType.values())
            {
                try
                {
                    registration.register(registryHelper);
                    ApexCore.LOGGER.debug(MARKER, "Registered {} to registry {}", registration.registryName, registryType);
                }
                catch(Exception e)
                {
                    var msg = ApexCore.LOGGER.getMessageFactory().newMessage("Unexpected error while registering entry {} to registry {}", registration.registryName, registryType);

                    if(skipErrors)
                        ApexCore.LOGGER.error(MARKER, msg, e);
                    else
                        throw new RuntimeException(msg.getFormattedMessage(), e);
                }
            }
        }
    }

    @ApiStatus.Internal
    @DoNotCall
    public final void onRegisterPost(@Nullable ResourceKey<? extends Registry<?>> registryType)
    {
        if(registryType == null)
            return;

        var listeners = afterRegisterListeners.get(registryType);
        listeners.forEach(Runnable::run);
        listeners.clear();
        completedRegistrations.add(registryType);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @ApiStatus.Internal
    @DoNotCall
    public final <T, R extends T, E extends RegistryEntry<R>> E register(ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory, Supplier<E> registryEntryFactory)
    {
        var registration = new Registration<>(registryType, new ResourceLocation(ownerId, registrationName), entryFactory, registryEntryFactory);
        ApexCore.LOGGER.debug(MARKER, "Captured registration for entry {} of type {}", registrationName, registryType.location());
        registerListeners.removeAll(Pair.of(registryType, registrationName)).forEach(listener -> registration.addListener((Consumer<R>) listener));
        registrations.put(registryType, registrationName, registration);
        return (E) registration.registryEntry;
    }

    protected final O self()
    {
        return self;
    }
}
