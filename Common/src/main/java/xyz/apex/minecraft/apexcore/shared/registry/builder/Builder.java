package xyz.apex.minecraft.apexcore.shared.registry.builder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;
import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.function.LazyLike;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface Builder<T, R extends T, O extends AbstractRegistrar<O>, P, B extends Builder<T, R, O, P, B>> extends LazyLike<RegistryEntry<R>>, PlatformHolder
{
    RegistryEntry<R> register();

    O getOwner();

    P getParent();

    default String getModId()
    {
        return getOwner().getModId();
    }

    default String getRegistrationName()
    {
        return getRegistryName().getPath();
    }

    default ResourceLocation getRegistryName()
    {
        return getRegistryKey().location();
    }

    ResourceKey<? extends Registry<T>> getRegistryType();

    ResourceKey<T> getRegistryKey();

    @Override
    default RegistryEntry<R> get()
    {
        return getOwner().get(getRegistryType(), getRegistrationName());
    }

    default R getEntry()
    {
        return get().get();
    }

    Supplier<R> asSupplier();

    default B onRegister(Consumer<R> callback)
    {
        getOwner().addRegisterCallback(getRegistryType(), getRegistrationName(), callback);
        return self();
    }

    default <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> callback)
    {
        return onRegister(value -> {
            if(getOwner().isRegistered(dependencyType)) callback.accept(value);
            else getOwner().addRegisterCallback(dependencyType, () -> callback.accept(value));
        });
    }

    default <T1, R1 extends T1, O1 extends AbstractRegistrar<O1>, P1, B1 extends Builder<T1, R1, O1, P1, B1>> B1 transform(Function<B, B1> transformer)
    {
        return transformer.apply(self());
    }

    default P build()
    {
        return getParent();
    }

    default B self()
    {
        return (B) this;
    }

    @Override
    default GamePlatform platform()
    {
        return getOwner().platform();
    }
}
