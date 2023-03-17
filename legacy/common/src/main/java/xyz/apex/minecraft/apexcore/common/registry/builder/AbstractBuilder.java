package xyz.apex.minecraft.apexcore.common.registry.builder;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.common.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.common.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.util.function.Lazy;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBuilder<T, R extends T, O extends AbstractRegistrar<O>, P, B extends AbstractBuilder<T, R, O, P, B>> implements Builder<T, R, O, P, B>
{
    protected final O owner;
    protected final P parent;
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceKey<T> registryKey;
    protected final Supplier<R> safeSupplier = Lazy.of(this::getEntry);

    protected AbstractBuilder(O owner, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        this.owner = owner;
        this.registryType = registryType;
        this.parent = parent;

        registryKey = ResourceKey.create(registryType, owner.registryName(registrationName));
    }

    protected abstract R createEntry();

    protected RegistryEntry<R> createRegistryEntry(RegistrySupplier<R> delegate)
    {
        return new RegistryEntry<>(owner, delegate, registryType, registryKey);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public RegistryEntry<R> register()
    {
        return owner.accept(self(), this::createEntry, this::createRegistryEntry);
    }

    @Override
    public final O getOwner()
    {
        return owner;
    }

    @Override
    public final P getParent()
    {
        return parent;
    }

    @Override
    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    @Override
    public final ResourceKey<T> getRegistryKey()
    {
        return registryKey;
    }

    @Override
    public final Supplier<R> asSupplier()
    {
        return safeSupplier;
    }

    @Override
    public final String getModId()
    {
        return Builder.super.getModId();
    }

    @Override
    public final String getRegistrationName()
    {
        return Builder.super.getRegistrationName();
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return Builder.super.getRegistryName();
    }

    @Override
    public final RegistryEntry<R> get()
    {
        return Builder.super.get();
    }

    @Override
    public final R getEntry()
    {
        return Builder.super.getEntry();
    }

    @Override
    public final B onRegister(Consumer<R> callback)
    {
        return Builder.super.onRegister(callback);
    }

    @Override
    public final <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> callback)
    {
        return Builder.super.onRegisterAfter(dependencyType, callback);
    }

    @Override
    public final <T1, R1 extends T1, O1 extends AbstractRegistrar<O1>, P1, B1 extends Builder<T1, R1, O1, P1, B1>> B1 transform(Function<B, B1> transformer)
    {
        return Builder.super.transform(transformer);
    }

    @Override
    public final P build()
    {
        return Builder.super.build();
    }

    @Override
    public final B self()
    {
        return Builder.super.self();
    }

    @Override
    public final GamePlatform platform()
    {
        return Builder.super.platform();
    }
}
