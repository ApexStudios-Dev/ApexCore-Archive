package xyz.apex.minecraft.apexcore.shared.registry;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class RegistryEntryBuilder<T, E extends T>
{
    protected final String name;
    protected final ModdedRegistry<T> registry;

    protected RegistryEntryBuilder(ModdedRegistry<T> registry, String name)
    {
        this.registry = registry;
        this.name = name;
    }

    protected abstract E build();

    @OverridingMethodsMustInvokeSuper
    public RegistryEntry<E> register()
    {
        return registry.register(name, this::build);
    }
}
