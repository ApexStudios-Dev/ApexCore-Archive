package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.Services;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * API for registering & looking up entries to platform specific registries.
 * <p>
 * Recommended to never use this directly, but make use of {@link Registrar Registrars}.
 */
@ApiStatus.Internal
public interface RegistryApi extends Services.Service
{
    /**
     * Main Logger instance used for all Registry related logging.
     */
    Logger LOGGER = LogManager.getLogger();

    /**
     * Registers the given registrar.
     *
     * @param registrar Registrar to be registered.
     * @param <T>       Type of registrar.
     */
    <T> void register(Registrar<T> registrar);

    /**
     * Registers a new registry entry with the given registry name and factory.
     *
     * @param registrar            Registrar the finalized registry entry will be bound to.
     * @param registrationName     Registration name of the registered entry.
     * @param registryEntryFactory Factory used to construct the registry entry value.
     * @param <T>                  Base type of registry entry to be registered.
     */
    <T, R extends T> RegistryEntry<R> register(Registrar<T> registrar, String registrationName, Supplier<R> registryEntryFactory);

    /**
     * Looks up a matching registry for given registry type and searches for matching holder instance for given registry key.
     *
     * @param registryType Type of registry to be looked up.
     * @param registryKey  Name of registry entry to be looked up.
     * @param <T>          Base type of registry entry to be looked up.
     * @return Matching holder instance for given registry key in matching registry or empty.
     */
    <T> Optional<Holder<T>> getDelegate(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey);

    /**
     * Registers a listener to be invoked after the registrar matching the given registry type has been registered.
     *
     * @param registryType Type of registry.
     * @param ownerId      Owner id.
     * @param listener     Listener to be invoked.
     * @param <T>          Type of registry.
     */
    <T> void addListener(ResourceKey<? extends Registry<T>> registryType, String ownerId, Consumer<Registrar<T>> listener);

    /**
     * Registers a listener to be invoked after the registry entry matching the given registration type and name has been registered.
     *
     * @param registryType Type of registry.
     * @param ownerId      Owner id.
     * @param registryName Name of registry entry.
     * @param listener     Listener to be invoked.
     * @param <T>          Base type of registry entry.
     * @param <R>          Type of registry entry.
     */
    <T, R extends T> void addListener(ResourceKey<? extends Registry<T>> registryType, String ownerId, ResourceLocation registryName, Consumer<R> listener);

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T> Optional<Registry<T>> findVanillaRegistry(ResourceKey<? extends Registry<T>> registryType)
    {
        return BuiltInRegistries.REGISTRY.getOptional((ResourceKey) registryType);
    }

    /**
     * @return Global instance.
     */
    static RegistryApi get()
    {
        return Services.REGISTRIES;
    }
}
