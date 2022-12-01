package xyz.apex.minecraft.apexcore.shared.registry;

import org.apache.commons.compress.utils.Lists;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;

public abstract class RegistryEntryBuilder<T, E extends T, B extends RegistryEntryBuilder<T, E, B>>
{
    protected final String name;
    protected final ModdedRegistry<T> registry;
    private final List<ModdedRegistry.RegisterCallback<T>> registerCallbacks = Lists.newArrayList();

    protected RegistryEntryBuilder(ModdedRegistry<T> registry, String name)
    {
        this.registry = registry;
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public final B onRegister(ModdedRegistry.RegisterCallback<T> registerCallback)
    {
        registerCallbacks.add(registerCallback);
        return (B) this;
    }

    protected abstract E build();

    @SuppressWarnings("unchecked")
    @OverridingMethodsMustInvokeSuper
    public RegistryEntry<E> register()
    {
        var entry = registry.register(name, this::build);
        registerCallbacks.forEach(callback -> entry.addOnRegisterCallback((ModdedRegistry.RegisterCallback<E>) callback));
        return entry;
    }
}
