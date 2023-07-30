package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Builder<O extends AbstractRegistrar<O>, P, T, R extends T, B extends Builder<O, P, T, R, B, E>, E extends RegistryEntry<R>> extends Supplier<E>
{
    @ApiStatus.NonExtendable
    O registrar();

    @ApiStatus.NonExtendable
    P parent();

    @ApiStatus.NonExtendable
    ResourceKey<? extends Registry<T>> registryType();

    @ApiStatus.NonExtendable
    ResourceKey<R> registryKey();

    @ApiStatus.NonExtendable
    ResourceLocation registryName();

    @ApiStatus.NonExtendable
    String registrationName();

    @ApiStatus.NonExtendable
    String ownerId();

    @ApiStatus.NonExtendable
    @Override
    E get();

    @ApiStatus.NonExtendable
    R getEntry();

    @ApiStatus.NonExtendable
    Supplier<R> asSupplier();

    @ApiStatus.NonExtendable
    E register();

    @ApiStatus.NonExtendable
    P build();

    @ApiStatus.NonExtendable
    B onRegister(Consumer<R> callback);

    @ApiStatus.NonExtendable
    <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> callback);

    @ApiStatus.NonExtendable
    B with(Consumer<B> consumer);

    @ApiStatus.NonExtendable
    B self();
}
