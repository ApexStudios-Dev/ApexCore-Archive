package dev.apexstudios.apexcore.common.registry;

import com.google.common.collect.*;
import com.google.errorprone.annotations.DoNotCall;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.registry.builder.AbstractBuilder;
import dev.apexstudios.apexcore.common.registry.builder.BuilderHelper;
import dev.apexstudios.apexcore.common.registry.builder.ItemBuilder;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AbstractRegister<O extends AbstractRegister<O>>
{
    public static final Marker MARKER = MarkerManager.getMarker("Registries");

    protected final String ownerId;
    private final Table<ResourceKey<? extends Registry<?>>, String, Registration<?, ?>> registrations = HashBasedTable.create();
    private final Multimap<Pair<String, ResourceKey<? extends Registry<?>>>, Consumer<?>> registerListeners = HashMultimap.create();
    private final Multimap<ResourceKey<? extends Registry<?>>, Runnable> afterRegisterListeners = HashMultimap.create();
    private final Set<ResourceKey<? extends Registry<?>>> completedRegistrations = Sets.newHashSet();
    @Nullable private String currentName = null;
    private boolean skipErrors = false;

    protected AbstractRegister(String ownerId)
    {
        this.ownerId = ownerId;
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
        if(skipErrors && Platform.get().isProduction())
            ApexCore.LOGGER.error("Ignoring skipErrors(true) as this is not a development environment!");
        else
            this.skipErrors = skipErrors;

        return self();
    }

    public final O skipErrors()
    {
        return skipErrors(true);
    }

    public final O object(String valueName)
    {
        currentName = valueName;
        return self();
    }

    public final <T, R extends T> DeferredHolder<T, R> get(ResourceKey<? extends Registry<T>> registryType)
    {
        return get(registryType, currentName());
    }

    public final <T, R extends T> DeferredHolder<T, R> get(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        return this.<T, R>registration(registryType, valueName).delegate;
    }

    public final <T, R extends T> OptionalLike<DeferredHolder<T, R>> getOptional(ResourceKey<? extends Registry<T>> registryType)
    {
        return getOptional(registryType, currentName());
    }

    public final <T, R extends T> OptionalLike<DeferredHolder<T, R>> getOptional(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, valueName);
        return registration == null ? OptionalLike.empty() : () -> registration.delegate;
    }

    public final <T> Collection<Holder<T>> getAll(ResourceKey<? extends Registry<T>> registryType)
    {
        return registrations.row(registryType).values().stream().map(registration -> (Holder<T>) registration.delegate).toList();
    }

    public final <T, R extends T> O addRegisterListener(ResourceKey<? extends Registry<T>> registryType, String valueName, Consumer<R> listener)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, valueName);

        if(registration == null)
            registerListeners.put(Pair.of(valueName, registryType), listener);
        else
            registration.addListener(listener);

        return self();
    }

    public final <T> O addRegisterListener(ResourceKey<? extends Registry<T>> registryType, Runnable listener)
    {
        if(completedRegistrations.contains(registryType))
            listener.run();
        else
            afterRegisterListeners.put(registryType, listener);

        return self();
    }

    public final <T> boolean isRegistered(ResourceKey<? extends Registry<T>> registryType)
    {
        return completedRegistrations.contains(registryType);
    }

    protected final <P, T, R extends T, H extends DeferredHolder<T, R>, B extends AbstractBuilder<O, P, T, R, H, B>> B entry(Function<BuilderHelper, B> builderFactory)
    {
        return builderFactory.apply(this::register);
    }

    public final <P, T extends Item> ItemBuilder<O, P, T> item(P parent, String itemName, Function<Item.Properties, T> itemFactory)
    {
        return entry(helper -> new ItemBuilder<>(self(), parent, itemName, helper, itemFactory));
    }

    public final <P> ItemBuilder<O, P, Item> item(P parent, String itemName)
    {
        return item(parent, itemName, Item::new);
    }

    public final <P, T extends Item> ItemBuilder<O, P, T> item(P parent, Function<Item.Properties, T> itemFactory)
    {
        return item(parent, currentName(), itemFactory);
    }

    public final <P> ItemBuilder<O, P, Item> item(P parent)
    {
        return item(parent, currentName(), Item::new);
    }

    public final <T extends Item> ItemBuilder<O, O, T> item(String itemName, Function<Item.Properties, T> itemFactory)
    {
        return item(self(), itemName, itemFactory);
    }

    public final <T extends Item> ItemBuilder<O, O, T> item(Function<Item.Properties, T> itemFactory)
    {
        return item(self(), currentName(), itemFactory);
    }

    public final ItemBuilder<O, O, Item> item(String itemName)
    {
        return item(self(), itemName, Item::new);
    }

    public final ItemBuilder<O, O, Item> item()
    {
        return item(self(), currentName(), Item::new);
    }

    protected final O self()
    {
        return (O) this;
    }

    public void register()
    {
        Platform.get().register(this);
    }

    private <T, R extends T, H extends DeferredHolder<T, R>> H register(ResourceKey<? extends Registry<T>> registryType, Supplier<H> holderFactory, Supplier<R> valueFactory)
    {
        var registration = new Registration<>(registryType, valueFactory, holderFactory);
        ApexCore.LOGGER.debug(MARKER, "Captured registration for entry {} of type {}", registration.delegate.valueKey.location(), registryType.location());
        registerListeners.removeAll(Pair.of(registration.delegate.valueKey.location().getPath(), registryType)).forEach(listener -> registration.addListener((Consumer<R>) listener));
        registrations.put(registryType, registration.delegate.valueKey.location().getPath(), registration);
        return (H) registration.delegate;
    }

    private <T, R extends T> Registration<T, R> registration(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, valueName);
        return Objects.requireNonNull(registration, () -> "Unknown registration %s for type %s".formatted(valueName, registryType));
    }

    @Nullable
    private <T, R extends T> Registration<T, R> registrationUnchecked(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        var registration = registrations.get(registryType, valueName);
        return registration == null ? null : (Registration<T, R>) registration;
    }

    @ApiStatus.Internal
    @DoNotCall
    public void onRegister(@Nullable ResourceKey<? extends Registry<?>> registryType, RegistrationHelper helper)
    {
        if(registryType == null)
        {
            ApexCore.LOGGER.debug(MARKER, "Skipping invalid registry with no super type");
            return;
        }

        if(!registerListeners.isEmpty())
        {
            registerListeners.asMap().forEach((k, v) -> ApexCore.LOGGER.warn(MARKER, "Found {} unused register callback(s) for entry {} [{}]. Was the entry ever registered?", v.size(), k.getLeft(), k.getRight().location()));
            registerListeners.clear();

            if(!Platform.get().isProduction())
                throw new IllegalStateException("Found unused register callbacks, see logs");

            return;
        }

        var registrationsForType = registrations.row(registryType);

        if(!registrationsForType.isEmpty())
        {
            ApexCore.LOGGER.debug(MARKER, "({}) Registering {} known objects of type {}", ownerId, registrationsForType.size(), registryType.location());

            for(var registration : registrationsForType.values())
            {
                try
                {
                    registration.register(helper);
                    ApexCore.LOGGER.debug(MARKER, "Registered {} to registry {}", registration.delegate.valueKey.location(), registryType);
                }
                catch(Exception e)
                {
                    var msg = ApexCore.LOGGER.getMessageFactory().newMessage("Unexpected error while registering entry {} to registry {}", registration.delegate.valueKey.location(), registryType);

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
    public void onRegisterLate(@Nullable ResourceKey<? extends Registry<?>> registryType)
    {
        if(registryType == null)
            return;

        var listeners = afterRegisterListeners.get(registryType);
        listeners.forEach(Runnable::run);
        listeners.clear();
        completedRegistrations.add(registryType);
    }
}
