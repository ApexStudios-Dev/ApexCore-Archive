package xyz.apex.minecraft.apexcore.shared.registry.builder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

import java.util.function.Supplier;

public final class NoConfigBuilder<T, R extends T, O extends AbstractRegistrar<O>, P> extends AbstractBuilder<T, R, O, P, NoConfigBuilder<T, R, O, P>>
{
    private final Supplier<R> entryFactory;

    public NoConfigBuilder(O owner, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        super(owner, parent, registryType, registrationName);

        this.entryFactory = entryFactory;
    }

    @Override
    protected R createEntry()
    {
        return entryFactory.get();
    }
}
