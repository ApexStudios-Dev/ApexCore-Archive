package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class AbstractBuilder<P, T, R extends T, E extends RegistryEntry<R>, B extends AbstractBuilder<P, T, R, E, B>> implements Builder<P, T, R, E, B>
{
    private final P parent;
    protected final BuilderManager builderManager;
    protected ResourceKey<T> registryKey;
    protected final ResourceKey<? extends Registry<T>> registryType;
    private final Function<RegistryEntry<R>, E> registryEntryFactory;
    private final Supplier<R> safeSupplier = Suppliers.memoize(this::createObject);
    private final List<Consumer<R>> listeners = Lists.newLinkedList();

    protected AbstractBuilder(P parent, BuilderManager builderManager, ResourceKey<? extends Registry<T>> registryType, String registrationName, Function<RegistryEntry<R>, E> registryEntryFactory)
    {
        this.parent = parent;
        this.builderManager = builderManager;
        this.registryType = registryType;
        this.registryEntryFactory = Util.memoize(registryEntryFactory);

        registryKey = ResourceKey.create(registryType, new ResourceLocation(builderManager.getOwnerId(), registrationName));
    }

    protected abstract R createObject();

    @Override
    public final BuilderManager getBuilderManager()
    {
        return builderManager;
    }

    @Override
    public final RegistrarManager getRegistrarManager()
    {
        return builderManager.getRegistrarManager();
    }

    @Override
    public final Registrar<T> getRegistrar()
    {
        return getRegistrarManager().get(registryType);
    }

    @Override
    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    @Override
    public final String getOwnerId()
    {
        return builderManager.getOwnerId();
    }

    @Override
    public final String getRegistrationName()
    {
        return getRegistryName().getPath();
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return registryKey.location();
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
    public final P getParent()
    {
        return parent;
    }

    @Override
    public final B registrationModifier(UnaryOperator<String> registrationNameModifier)
    {
        var registrationName = registrationNameModifier.apply(getRegistrationName());
        registryKey = ResourceKey.create(registryType, new ResourceLocation(getOwnerId(), registrationName));
        return self();
    }

    @Override
    public final B withRegistrationNamePrefix(String prefix)
    {
        return registrationModifier(registrationName -> prefix + registrationName);
    }

    @Override
    public final B withRegistrationNameSuffix(String suffix)
    {
        return registrationModifier(registrationName -> registrationName + suffix);
    }

    @Override
    public final B addListener(Runnable listener)
    {
        return addListener(value -> listener.run());
    }

    @Override
    public final B addListener(Consumer<R> listener)
    {
        listeners.add(listener);
        return self();
    }

    @Override
    public final P end()
    {
        register();
        return parent;
    }

    @Override
    public final E register()
    {
        var delegate = getRegistrar().register(getRegistrationName(), safeSupplier);
        listeners.forEach(delegate::addListener);
        listeners.clear();
        return registryEntryFactory.apply(delegate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final B self()
    {
        return (B) this;
    }
}
