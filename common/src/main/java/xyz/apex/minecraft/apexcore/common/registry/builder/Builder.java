package xyz.apex.minecraft.apexcore.common.registry.builder;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.registry.RegistryManager;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class Builder<T, R extends T, E extends RegistryEntry<R>, S extends Builder<T, R, E, S>>
{
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceLocation registryName;

    private final S self = (S) this;
    private final List<Consumer<R>> registerCallbacks = Lists.newLinkedList();

    protected final Supplier<R> safeEntrySupplier = Suppliers.memoize(this::create);

    protected Builder(ResourceKey<? extends Registry<T>> registryType, String ownerId, String registrationName)
    {
        this.registryType = registryType;

        registryName = new ResourceLocation(ownerId, registrationName);
    }

    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    public final ResourceLocation getRegistryName()
    {
        return registryName;
    }

    public final String getOwnerId()
    {
        return registryName.getNamespace();
    }

    public final String getRegistrationName()
    {
        return registryName.getPath();
    }

    public final RegistryManager getRegistryManager()
    {
        return RegistryManager.get(getOwnerId());
    }

    public final DeferredRegister<T> getRegistry()
    {
        return getRegistryManager().getRegistry(registryType);
    }

    public final Supplier<R> asSupplier()
    {
        return safeEntrySupplier;
    }

    public final S onRegister(Consumer<R> registerCallback)
    {
        registerCallbacks.add(registerCallback);
        return self;
    }

    public final S onRegister(Runnable registerCallback)
    {
        getRegistry().registerCallback(registerCallback);
        return self;
    }

    public final E register()
    {
        onPreRegistered();
        var registry = getRegistry();
        var registryEntry = registry.register(getRegistrationName(), this::createRegistryEntry, safeEntrySupplier);

        registerCallbacks.forEach(registerCallback -> registry.registerCallback(registryEntry, registerCallback));
        registerCallbacks.clear();
        onPostRegistered(registryEntry);
        return registryEntry;
    }

    protected void onPreRegistered() { }

    protected void onPostRegistered(E registryEntry) { }

    protected abstract E createRegistryEntry(ResourceLocation registryName);

    protected abstract R create();
}
