package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base interface for all Builders.
 *
 * @param <O> Type of Registrar.
 * @param <P> Type of Parent.
 * @param <T> Type of Registry.
 * @param <R> Type of Entry.
 * @param <B> Type of Builder.
 * @param <E> Type of RegistryEntry.
 */
public interface Builder<O extends AbstractRegistrar<O>, P, T, R extends T, B extends Builder<O, P, T, R, B, E>, E extends RegistryEntry<R>> extends Supplier<E>
{
    /**
     * @return Owning Registrar.
     */
    @ApiStatus.NonExtendable
    O registrar();

    /**
     * @return Parent object.
     */
    @ApiStatus.NonExtendable
    P parent();

    /**
     * @return Type of Registry.
     */
    @ApiStatus.NonExtendable
    ResourceKey<? extends Registry<T>> registryType();

    /**
     * @return Registration key for the finalized entry.
     */
    @ApiStatus.NonExtendable
    ResourceKey<R> registryKey();

    /**
     * @return Registry name for the finalized entry.
     */
    @ApiStatus.NonExtendable
    ResourceLocation registryName();

    /**
     * @return Registration name for the finalized entry.
     */
    @ApiStatus.NonExtendable
    String registrationName();

    /**
     * @return Owning mod id.
     */
    @ApiStatus.NonExtendable
    String ownerId();

    /**
     * @return Registry entry built by this builder. Prefer using {@link #asSupplier()}.
     */
    @ApiStatus.NonExtendable
    @Override
    E get();

    /**
     * @return Entry built by this builder.
     */
    @ApiStatus.NonExtendable
    R getEntry();

    /**
     * @return Supplier for the entry built by this builder.
     */
    @ApiStatus.NonExtendable
    Supplier<R> asSupplier();

    /**
     * Complete the current entry, and return the {@link RegistryEntry} that will supply the built entry once it is available.
     *
     * @return The {@link RegistryEntry} supplying the built entry.
     */
    @ApiStatus.NonExtendable
    E register();

    /**
     * Complete the current entry, and return the {@linkplain #parent() parent object}.
     *
     * @return The {@linkplain #parent() parent object}.
     * @see #parent()
     */
    @ApiStatus.NonExtendable
    P build();

    /**
     * Adds a listener to be invoked when this entry is registered.
     *
     * @param listener The listener to be invoked.
     * @return This Builder.
     */
    @ApiStatus.NonExtendable
    B onRegister(Consumer<R> listener);

    /**
     * Adds a listener to be invoked when this entry is registered, but only after some other Registry has been registered as well.
     *
     * @param dependencyType The dependency registry type.
     * @param listener The listener to be invoked.
     * @return This Builder.
     * @param <OR> The dependency registry type.
     */
    @ApiStatus.NonExtendable
    <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> listener);

    @ApiStatus.NonExtendable
    B with(Consumer<B> consumer);

    @ApiStatus.NonExtendable
    B self();
}
