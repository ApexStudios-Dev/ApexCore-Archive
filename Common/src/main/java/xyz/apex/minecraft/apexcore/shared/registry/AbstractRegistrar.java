package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.*;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.builder.Builder;
import xyz.apex.minecraft.apexcore.shared.registry.builder.NoConfigBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.function.Lazy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@SuppressWarnings({ "UnusedReturnValue", "unchecked", "SuspiciousMethodCalls" })
public class AbstractRegistrar<S extends AbstractRegistrar<S>>
{
    private final Table<ResourceKey<? extends Registry<?>>, String, Registration<?, ?>> registrations = HashBasedTable.create();
    private final Multimap<Pair<ResourceKey<? extends Registry<?>>, String>, Consumer<?>> registerCallbacks = HashMultimap.create();
    private final Multimap<ResourceKey<? extends Registry<?>>, Runnable> afterRegisterCallbacks = HashMultimap.create();
    private final Set<ResourceKey<? extends Registry<?>>> completedRegistrations = Sets.newHashSet();
    private final Supplier<RegistrarManager> registrarManager;
    private final String modId;
    @Nullable private ModPlatform mod;
    private boolean skipErrors = false;

    protected AbstractRegistrar(String modId)
    {
        this.modId = modId;

        registrarManager = Lazy.of(() -> RegistrarManager.get(modId));
    }

    public final ResourceLocation registryName(String registrationName)
    {
        return new ResourceLocation(modId, registrationName);
    }

    public final String getModId()
    {
        return modId;
    }

    protected final S self()
    {
        return (S) this;
    }

    public final S skipErrors(boolean skipErrors)
    {
        if(skipErrors && !GamePlatform.INSTANCE.isDevelopmentEnvironment()) getLogger().error("Ignoring skipErrors(true) as this is not a development environment!");
        else this.skipErrors = skipErrors;
        return self();
    }

    public final S skipErrors()
    {
        return skipErrors(true);
    }

    public final S transform(UnaryOperator<S> transformer)
    {
        return transformer.apply(self());
    }

    public final <T, R extends T, O extends AbstractRegistrar<O>, P, B extends Builder<T, R, O, P, B>> B map(Function<S, B> mapper)
    {
        return mapper.apply(self());
    }

    public final <T, R extends T> RegistryEntry<R> simple(ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        return simple(self(), registryType, registrationName, entryFactory);
    }

    public final <T, R extends T, P> RegistryEntry<R> simple(P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        return new NoConfigBuilder<>(self(), parent, registryType, registrationName, entryFactory).register();
    }

    @ApiStatus.Internal
    public final <T, R extends T, P, B extends Builder<T, R, S, P, B>> RegistryEntry<R> accept(B builder, Supplier<R> entryFactory, Supplier<RegistryEntry<R>> registryEntryFactory)
    {
        var registrationName = builder.getRegistrationName();
        var registryType = builder.getRegistryType();
        var registration = new Registration<T, R>(registryEntryFactory.get(), entryFactory);
        getLogger().debug("Captured registration for entry {} of type {}", registrationName, registryType);
        registerCallbacks.removeAll(Pair.of(registryType, registrationName)).forEach(callback -> registration.addCallback((Consumer<R>) callback));
        registrations.put(registryType, registrationName, registration);
        return registration.registryEntry;
    }

    public final <T, R extends T> RegistryEntry<R> get(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return this.<T, R>getRegistrationOrThrow(registryType, registrationName).registryEntry;
    }

    public final <T, R extends T> Optional<RegistryEntry<R>> getOptional(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        return registration == null ? Optional.empty() : Optional.of(registration.registryEntry);
    }

    public final <T> Collection<RegistryEntry<T>> getAll(ResourceKey<? extends Registry<T>> registryType)
    {
        return stream(registryType).toList();
    }

    public final <T> Stream<RegistryEntry<T>> stream(ResourceKey<? extends Registry<T>> registryType)
    {
        return registrations.row(registryType).values().stream().map(registration -> (RegistryEntry<T>) registration.registryEntry);
    }

    public final <T, R extends T> S addRegisterCallback(ResourceKey<? extends Registry<T>> registryType, String registrationName, Consumer<R> callback)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);

        if(registration == null) registerCallbacks.put(Pair.of(registryType, registrationName), callback);
        else registration.addCallback(callback);

        return self();
    }

    public final <T> S addRegisterCallback(ResourceKey<? extends Registry<T>> registryType, Runnable callback)
    {
        afterRegisterCallbacks.put(registryType, callback);
        return self();
    }

    public final boolean isRegistered(ResourceKey<? extends Registry<?>> registryType)
    {
        return completedRegistrations.contains(registryType);
    }

    @Nullable
    private <T, R extends T> Registration<T, R> getRegistrationUnchecked(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return registrations.contains(registryType, registrationName) ? (Registration<T, R>) registrations.get(registryType, registrationName) : null;
    }

    private <T, R extends T> Registration<T, R> getRegistrationOrThrow(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        if(registration == null) throw new IllegalArgumentException("Unknown registration %s for type %s".formatted(registrationName, registryType));
        return registration;
    }

    @ApiStatus.Internal
    public final <T> void register(ResourceKey<? extends Registry<T>> registryType)
    {
        if(completedRegistrations.contains(registryType)) return;

        var logger = getLogger();

        if(!registerCallbacks.isEmpty())
        {
            registerCallbacks.asMap().forEach((pair, callbacks) -> logger.warn("Found {} unused register callback(s) for entry {} [{}]. Was the entry ever registered?", callbacks.size(), pair.getLeft(), pair.getRight().length()));
            registerCallbacks.clear();
            if(GamePlatform.INSTANCE.isDevelopmentEnvironment()) throw new IllegalStateException("Found unused register callbacks, see logs");
        }

        var registrationsForType = registrations.row(registryType);

        if(!registrationsForType.isEmpty())
        {
            var registry = (Registrar<T>) registrarManager.get().get((ResourceKey<Registry<T>>) registryType);
            logger.debug("Registering {} known objects of type {}", registrationsForType.size(), registryType.location());

            for(var entry : registrationsForType.entrySet())
            {
                var registryEntry = entry.getValue();
                var registryName = registryEntry.registryEntry.getRegistryName();

                try
                {
                    ((Registration<T, ?>) registryEntry).register(registry);
                    logger.debug("Registered {} to registry {}", registryName, registryType);
                }
                catch(Exception e)
                {
                    var err = logger.getMessageFactory().newMessage("Unexpected error while registering entry {} to registry {}", registryName, registryType);

                    if(skipErrors) logger.debug(err);
                    else throw new RuntimeException(e);
                }
            }
        }
    }

    @ApiStatus.Internal
    public final void register()
    {
        forEachRegistry(registry -> register(registry.key()));
    }

    @ApiStatus.Internal
    public final <T> void lateRegister(ResourceKey<? extends Registry<T>> registryType)
    {
        if(completedRegistrations.contains(registryType)) return;
        afterRegisterCallbacks.removeAll(registryType).forEach(Runnable::run);
        completedRegistrations.add(registryType);
    }

    @ApiStatus.Internal
    public final void lateRegister()
    {
        forEachRegistry(registry -> lateRegister(registry.key()));
    }

    @ApiStatus.Internal
    public final void setMod(ModPlatform mod)
    {
        if(this.mod != null) throw new IllegalStateException("AbstractRegistrar#setMod should only ever be called once! Do not manually call this method!");
        if(!modId.equals(mod.getModId())) throw new IllegalStateException("AbstractRegistrar#setMod should be called with ModPlatform of same ModID");
        this.mod = mod;
    }

    private Logger getLogger()
    {
        return mod == null ? GamePlatform.INSTANCE.getLogger() : mod.getLogger();
    }

    // mostly used for fabric, to call the #register & #lateRegister methods
    // since fabric does not have any register events to listen to
    // forge uses the register events to call these methods at correct time and in correct orders
    // order in this method might not be perfect 1:1 with forge registration order
    // but its close enough and registration order shouldn't matter as much when running on fabric
    public static void forEachRegistry(Consumer<Registry<?>> consumer)
    {
        // these must go first
        consumer.accept(BuiltInRegistries.FLUID);
        consumer.accept(BuiltInRegistries.BLOCK);
        consumer.accept(BuiltInRegistries.ITEM);

        // all other registries not specified above
        BuiltInRegistries.REGISTRY.forEach(registry -> {
            var registryType = registry.key();
            if(registryType.equals(Registries.FLUID)) return;
            if(registryType.equals(Registries.BLOCK)) return;
            if(registryType.equals(Registries.ITEM)) return;
            consumer.accept(registry);
        });
    }

    private static final class Registration<T, R extends T>
    {
        private Supplier<R> creator;
        private final RegistryEntry<R> registryEntry;
        private final List<Consumer<R>> callbacks = Lists.newArrayList();
        private boolean registered = false;

        private Registration(RegistryEntry<R> registryEntry, Supplier<R> creator)
        {
            this.registryEntry = registryEntry;
            this.creator = Lazy.of(creator);
        }

        private void register(Registrar<T> registry)
        {
            if(registered) return;
            var value = creator.get();
            creator = Lazy.of(value);
            registry.register(registryEntry.getRegistryName(), creator);
            callbacks.forEach(callback -> callback.accept(value));
            callbacks.clear();
            registered = true;
        }

        private void addCallback(Consumer<R> consumer)
        {
            if(registered) consumer.accept(creator.get());
            else callbacks.add(consumer);
        }
    }
}
