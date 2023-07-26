package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Main Builder interface describing all the common tasks and methods a builder can do.
 *
 * @param <P> Type of parent element.
 * @param <T> Base type of game object to be registered.
 * @param <R> Type of game object to be registered.
 * @param <E> Type of registry entry.
 * @param <B> Type of builder.
 * @param <M> Type of builder manager.
 */
@ApiStatus.NonExtendable
public sealed interface Builder<P, T, R extends T, E extends RegistryEntry<R>, B extends Builder<P, T, R, E, B, M>, M extends BuilderManager<M>> permits AbstractBuilder, FeatureElementBuilder
{
    /**
     * @return The builder manager this builder is bound to.
     */
    M getBuilderManager();

    /**
     * @return The registrar manager this builder is bound to.
     */
    RegistrarManager getRegistrarManager();

    /**
     * @return The registrar this builder is bound to and register into.
     */
    Registrar<T> getRegistrar();

    /**
     * @return Registry type for this builder.
     */
    ResourceKey<? extends Registry<T>> getRegistryType();

    /**
     * @return Owner mod id.
     */
    String getOwnerId();

    /**
     * @return Registration name for this builder.
     */
    String getRegistrationName();

    /**
     * @return Registry name for this builder.
     */
    ResourceLocation getRegistryName();

    /**
     * @return Registry key for this builder.
     */
    ResourceKey<T> getRegistryKey();

    /**
     * Returns a supplier pointing towards the finalized game object.
     * <p>
     * Using this supplier before the builder has been finalized & registered will result in {@link NullPointerException} exceptions.
     *
     * @return Supplier pointing towards the finalized game object.
     */
    Supplier<R> asSupplier();

    /**
     * @return Parent element this builder is related to.
     */
    P getParent();

    /**
     * Modifies this builders registration name using the given modifier.
     *
     * @param registrationNameModifier Modifier used to modify this builders registration name.
     * @return This builder instance.
     */
    B registrationModifier(UnaryOperator<String> registrationNameModifier);

    /**
     * Prepends this builders registration name with the given prefix.
     *
     * @param prefix Prefix to prepend to this builders registration name.
     * @return This builder instance.
     */
    B withRegistrationNamePrefix(String prefix);

    /**
     * Appends this builders registration name with the given suffix.
     *
     * @param suffix Suffix to be appended to this builders registration name.
     * @return This builder instance.
     */
    B withRegistrationNameSuffix(String suffix);

    /**
     * Registers a listener to be invoked after the finalized registry entry is registered.
     *
     * @param listener Listener to be invoked.
     * @return This builder instance.
     */
    B addListener(Runnable listener);

    /**
     * Registers a listener to be invoked after the finalized registry entry is registered.
     *
     * @param listener Listener to be invoked.
     * @return This builder instance.
     */
    B addListener(Consumer<R> listener);

    /**
     * Finalizes and registers this builder and returns the parent element of this builder.
     *
     * @return Parent element of this builder.
     */
    P end();

    /**
     * Finalizes and registers this builder and returns the finalized registry entry.
     *
     * @return Finalized registry entry after this builder has been registered.
     */
    E register();

    /**
     * @return This builder instance
     */
    B self();

    /**
     * Registers listener to be invoked during resource generation for given provider.
     *
     * @param providerType Provider type to register for.
     * @param listener Listener to be invoked.
     * @return This builder instance.
     * @param <D> Provider type.
     */
    <D extends DataProvider> B addProvider(ProviderType<D> providerType, BiConsumer<D, ProviderType.RegistryContext<T, R>> listener);

    /**
     * Registers listener to be invoked during resource generation for given provider.
     * <p>
     * This will wipe out all previously registered listeners.
     *
     * @param providerType Provider type to register for.
     * @param listener Listener to be invoked.
     * @return This builder instance.
     * @param <D> Provider type.
     */
    <D extends DataProvider> B setProvider(ProviderType<D> providerType, BiConsumer<D, ProviderType.RegistryContext<T, R>> listener);

    /**
     * Clears all previously registered listeners for given provider.
     *
     * @param providerType Provider type to register for.
     * @return This builder instance.
     * @param <D> Provider type.
     */
    <D extends DataProvider> B clearProvider(ProviderType<D> providerType);
}
