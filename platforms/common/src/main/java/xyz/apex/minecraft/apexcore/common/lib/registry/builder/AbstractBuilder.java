package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import com.google.common.base.Suppliers;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<O extends AbstractRegistrar<O>, P, T, R extends T, B extends AbstractBuilder<O, P, T, R, B, E>, E extends RegistryEntry<R>> implements Builder<O, P, T, R, B, E>
{
    protected final O registrar;
    protected final P parent;
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceKey<R> registryKey;
    protected final B self = (B) this;

    private final Supplier<R> entrySupplier = Suppliers.memoize(this::getEntry);

    protected AbstractBuilder(O registrar, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        this.registrar = registrar;
        this.parent = parent;
        this.registryType = registryType;
        registryKey = (ResourceKey<R>) ResourceKey.create(registryType, new ResourceLocation(registrar.getOwnerId(), registrationName));

        onRegister(this::onRegister);
    }

    @ApiStatus.OverrideOnly
    protected void onRegister(R entry)
    {
    }

    @ApiStatus.Internal
    @DoNotCall
    protected abstract E createRegistryEntry();

    @ApiStatus.Internal
    @DoNotCall
    protected abstract R createEntry();

    @Override
    public final O registrar()
    {
        return registrar;
    }

    @Override
    public final P parent()
    {
        return parent;
    }

    @Override
    public final ResourceKey<? extends Registry<T>> registryType()
    {
        return registryType;
    }

    @Override
    public final ResourceKey<R> registryKey()
    {
        return registryKey;
    }

    @Override
    public final ResourceLocation registryName()
    {
        return registryKey.location();
    }

    @Override
    public final String registrationName()
    {
        return registryName().getPath();
    }

    @Override
    public String ownerId()
    {
        return registrar.getOwnerId();
    }

    @Override
    public final E get()
    {
        return (E) registrar.get(registryType, registrationName());
    }

    @Override
    public final R getEntry()
    {
        return get().get();
    }

    @Override
    public final Supplier<R> asSupplier()
    {
        return entrySupplier;
    }

    @Override
    public final E register()
    {
        return registrar.register(registryType, registrationName(), this::createEntry, this::createRegistryEntry);
    }

    @Override
    public final P build()
    {
        register();
        return parent;
    }

    @Override
    public final B onRegister(Consumer<R> callback)
    {
        registrar.addRegisterListener(registryType, registrationName(), callback);
        return self;
    }

    @Override
    public final <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> callback)
    {
        return onRegister(entry -> {
            if(registrar.isRegistered(dependencyType))
                callback.accept(entry);
            else
                registrar.addRegisterListener(dependencyType, () -> callback.accept(entry));
        });
    }

    @Override
    public final B with(Consumer<B> consumer)
    {
        consumer.accept(self);
        return self;
    }

    @Override
    public final B self()
    {
        return self;
    }
}
