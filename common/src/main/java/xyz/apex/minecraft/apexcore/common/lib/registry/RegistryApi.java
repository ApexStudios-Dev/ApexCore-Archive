package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.Services;

import java.util.Optional;
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
     * Registers a new registry entry with the given registry name and factory.
     * <p>
     * On Fabric this immediately tries to register to the matching vanilla registry, while
     * on Forge this queues up the registration to be registered later on during their RegisterEvent.
     *
     * @param registryType         Type of registry to register the entry into.
     * @param registryName         Name of the entry to be registered.
     * @param registryEntryFactory Factory used to construct the registry entry value.
     * @param <T>                  Base type of registry entry to be registered.
     */
    <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<? extends T> registryEntryFactory);

    /**
     * Looks up a matching registry for given registry type and searches for matching holder instance for given registry key.
     *
     * @param registryType Type of registry to be looked up.
     * @param registryKey  Name of registry entry to be looked up.
     * @param <T>          Base type of registry entry to be looked up.
     * @return Matching holder instance for given registry key in matching registry or empty.
     */
    <T> Optional<Holder<T>> getDelegate(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey);
}
