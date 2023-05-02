package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Registrar used to create, register & maintain registry entries.
 *
 * @param <T> Type of entries to manage.
 */
public sealed interface Registrar<T> extends Iterable<RegistryEntry<T>> permits RegistrarImpl
{
    /**
     * @return Registrar manager this registrar is bound to.
     */
    RegistrarManager getRegistrarManager();

    /**
     * @return Type of entries this registrar can manage.
     */
    ResourceKey<? extends Registry<T>> getRegistryType();

    /**
     * @return ID of mod owning this registrar.
     */
    String getOwnerId();

    /**
     * @return Collection of all registry entries.
     */
    Collection<RegistryEntry<T>> getEntries();

    /**
     * @return Stream used to iterate over all registry entries.
     */
    Stream<RegistryEntry<T>> entries();

    /**
     * Registers a new listener to be invoked after this registrar is registered.
     * <p>
     * Invokes listener immediately if this registrar has already been registered.
     *
     * @param listener Listener to be registered.
     */
    void addListener(Runnable listener);

    /**
     * @return True if this registrar has been registered.
     */
    boolean isRegistered();

    /**
     * Registers this registrar and all of its containing registry entries.
     * <p>
     * So long as you call {@link RegistrarManager#register(String)} somewhere during your mods initialization,
     * this method is entirely optional and does not need to be invoked manually.
     */
    void register();

    // region: RegistryKey

    /**
     * @return Set containing all registry entry keys.
     */
    Set<ResourceKey<T>> getRegistryKeys();

    /**
     * @return Stream used to iterate over all registry entry keys.
     */
    Stream<ResourceKey<T>> registryKeys();

    /**
     * Registers an optional registry entry to this registrar using the given registry key as the registry name.
     *
     * @param registryKey Registry key to register optional registry entry with.
     * @return Optional registry entry to be registered with given registry key.
     * @see RegistryEntry Here for more information on optional registry entries.
     */
    RegistryEntry<T> registerOptional(ResourceKey<T> registryKey);

    /**
     * Returns registry entry for matching registry key or empty if none exists.
     *
     * @param registryKey Registry key to lookup registry entry for.
     * @return Registry entry for matching registry key or empty.
     */
    RegistryEntry<T> get(ResourceKey<T> registryKey);

    /**
     * Returns true if this registrar contains a registry entry matching the given registry key.
     *
     * @param registryKey Registry key to look up a registry entry for.
     * @return True if this registrar contains a registry entry matching the given registry key.
     */
    boolean containsKey(ResourceKey<T> registryKey);

    /**
     * Registers a listener to be invoked after this registrar has been registered.
     *
     * @param registryKey Registry key of registry entry to be passed to listener.
     * @param listener    Listener to be registered
     */
    void addListener(ResourceKey<T> registryKey, Consumer<? super RegistryEntry<? extends T>> listener);
    // endregion

    // region: RegistryName

    /**
     * @return Set containing all registry entry names.
     */
    Set<ResourceLocation> getRegistryNames();

    /**
     * @return Stream used to iterate over all registry entry names.
     */
    Stream<ResourceLocation> registryNames();

    /**
     * Registers an optional registry entry to this registrar with the given registry name.
     *
     * @param registryName Registry name to register optional registry entry with.
     * @return Optional registry entry to be registered with given registry name.
     * @see RegistryEntry Here for more information on optional registry entries.
     */
    RegistryEntry<T> registerOptional(ResourceLocation registryName);

    /**
     * Returns registry entry for matching registry name or empty if none exists.
     *
     * @param registryName Registry name to lookup registry entry for.
     * @return Registry entry for matching registry name or empty.
     */
    RegistryEntry<T> get(ResourceLocation registryName);

    /**
     * Returns true if this registrar contains a registry entry matching the given registry name.
     *
     * @param registryName Registry name to look up a registry entry for.
     * @return True if this registrar contains a registry entry matching the given registry name.
     */
    boolean containsKey(ResourceLocation registryName);

    /**
     * Registers a listener to be invoked after this registrar has been registered.
     *
     * @param registryName Registry name of registry entry to be passed to listener.
     * @param listener     Listener to be registered
     */
    void addListener(ResourceLocation registryName, Consumer<? super RegistryEntry<? extends T>> listener);
    // endregion

    // region: RegistrationName

    /**
     * @return Set containing all registry entry registration names.
     */
    Set<String> getRegistrationNames();

    /**
     * @return Stream used to iterate over all registry entry registration names.
     */
    Stream<String> registrationNames();

    /**
     * Registers a registry entry to this registrar with the given registration name.
     *
     * @param registrationName Registration name to register registry entry with.
     * @return Registry entry to be registered with given registration name.
     */
    <R extends T> RegistryEntry<R> register(String registrationName, Supplier<R> registryEntryFactory);

    /**
     * Returns registry entry for matching registration name or empty if none exists.
     *
     * @param registrationName Registration name to lookup registry entry for.
     * @return Registry entry for matching registration name or empty.
     */
    RegistryEntry<T> get(String registrationName);

    /**
     * Returns true if this registrar contains a registry entry matching the given registration name.
     *
     * @param registrationName Registration name to look up a registry entry for.
     * @return True if this registrar contains a registry entry matching the given registration name.
     */
    boolean containsKey(String registrationName);

    /**
     * Registers a listener to be invoked after this registrar has been registered.
     *
     * @param registrationName Registration name of registry entry to be passed to listener.
     * @param listener         Listener to be registered
     */
    void addListener(String registrationName, Consumer<? super RegistryEntry<? extends T>> listener);
    // endregion

    /**
     * Returns registrar for given mod id to manage entries of the given registry type.
     *
     * @param ownerId      ID of mod the registrar will be bound to.
     * @param registryType Type of registry entries to manage.
     * @param <T>          Type of registry entries.
     * @return Registrar bound to given mod to manage entries of given registry type.
     */
    static <T> Registrar<T> get(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return RegistrarManager.get(ownerId).get(registryType);
    }
}
