package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import com.google.errorprone.annotations.DoNotCall;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface RegistryEntry<T> extends Holder<T>, Supplier<T>
{
    @ApiStatus.NonExtendable
    AbstractRegistrar<?> getRegistrar();

    @ApiStatus.NonExtendable
    Registry<T> getRegistry();

    @ApiStatus.NonExtendable
    ResourceLocation getRegistryName();

    @ApiStatus.NonExtendable
    ResourceKey<T> getRegistryKey();

    @ApiStatus.NonExtendable
    @Override
    T get();

    @ApiStatus.NonExtendable
    @Override
    T value();

    @ApiStatus.NonExtendable
    Optional<T> getOptional();

    @ApiStatus.NonExtendable
    boolean isPresent();

    @ApiStatus.NonExtendable
    @Override
    boolean isBound();

    @ApiStatus.NonExtendable
    @Override
    boolean is(ResourceLocation location);

    @ApiStatus.NonExtendable
    @Override
    boolean is(ResourceKey<T> resourceKey);

    @ApiStatus.NonExtendable
    @Override
    boolean is(Predicate<ResourceKey<T>> predicate);

    @ApiStatus.NonExtendable
    @Override
    boolean is(TagKey<T> tagKey);

    @ApiStatus.NonExtendable
    @Override
    Stream<TagKey<T>> tags();

    @ApiStatus.NonExtendable
    @Override
    Either<ResourceKey<T>, T> unwrap();

    @ApiStatus.NonExtendable
    @Override
    Optional<ResourceKey<T>> unwrapKey();

    @ApiStatus.NonExtendable
    @Override
    Kind kind();

    @ApiStatus.NonExtendable
    @Override
    boolean canSerializeIn(HolderOwner<T> owner);

    @ApiStatus.NonExtendable
    <T1, R1 extends T1> RegistryEntry<R1> getSibling(ResourceKey<? extends Registry<T1>> registryType);

    @ApiStatus.NonExtendable
    @ApiStatus.Internal
    @DoNotCall
    void bind();

    @SuppressWarnings("unchecked")
    static <E extends RegistryEntry<?>> E cast(Class<? super E> clazz, RegistryEntry<?> registryEntry)
    {
        if(clazz.isInstance(registryEntry))
            return (E) registryEntry;

        throw new IllegalArgumentException("Could not convert RegistryEntry: expecting %s, found %s".formatted(clazz, registryEntry.getClass()));
    }
}
