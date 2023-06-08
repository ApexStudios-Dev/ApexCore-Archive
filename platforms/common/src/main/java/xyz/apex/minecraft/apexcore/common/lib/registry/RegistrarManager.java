package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Consumer;

/**
 * Manager used to create & maintain {@link Registrar} instances.
 * <p>
 * Only notable thing in here is {@link #register(String)}. This <b>should</b> be called somewhere during mod initialization.
 * Does not matter when or where, can be before {@link Registrar} / {@link RegistryEntry} registration or after, so long as its called
 * during your mods initialization.
 */
public sealed interface RegistrarManager permits RegistrarManagerImpl
{
    /**
     * Marker used for all registry related logging
     */
    Marker MARKER = MarkerManager.getMarker("Registries");

    /**
     * Returns registrar instance matching the given registry type.
     * <p>
     * Returns the same instance if called more than once.
     *
     * @param registryType Type of registrar to be returned.
     * @param <T>          Type of registrar.
     * @return Registrar instance machine the given regitry type.
     */
    <T> Registrar<T> get(ResourceKey<? extends Registry<T>> registryType);

    /**
     * @return The ID of the mod owning this registrar manager.
     */
    String getOwnerId();

    /**
     * Registers a new listener to be invoked after this registrar manager has been registered.
     * <p>
     * Invokes listener immediately if this registrar manager has already been registered.
     *
     * @param registryType Type of registrar to be passed to the listener.
     * @param listener     Listener to be registered.
     * @param <T>          Type of registrar.
     */
    <T> void addListener(ResourceKey<? extends Registry<T>> registryType, Consumer<Registrar<T>> listener);

    /**
     * @return True if this registrar manager has been registered, false otherwise.
     */
    boolean isRegistered();

    /**
     * Registers this registrar manager.
     * <p>
     * This is required to be called somewhere during your mods initialization.
     */
    void register();

    /**
     * Returns a registrar manager for the given mod.
     * <p>
     * Returns the same instance if called more than once.
     *
     * @param ownerId ID of mod the newly created registrar manager will be bound to.
     * @return Registrar manager for the given mod.
     */
    static RegistrarManager get(String ownerId)
    {
        return RegistrarManagerImpl.get(ownerId);
    }

    /**
     * Looks up matching registrar manager for given mod id and registers it.
     *
     * @param ownerId ID of mod to look up a registrar manager for.
     * @see #register()
     */
    static void register(String ownerId)
    {
        get(ownerId).register();
    }
}
