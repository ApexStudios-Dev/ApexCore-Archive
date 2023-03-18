package xyz.apex.minecraft.apexcore.fabric.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

import java.util.function.Consumer;
import java.util.function.Supplier;

class FabricDeferredRegister<T> extends DeferredRegister<T>
{
    private boolean registered = false;

    FabricDeferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        super(ownerId, registryType);
    }

    void register()
    {
        if(registered) return;
        registerEntries();
        updateReferences();
        registered = true;
    }

    @Override
    protected boolean isRegistered()
    {
        return registered;
    }

    @Override
    protected <R extends T> void registryEntry(RegistryEntry<R> registryEntry, Supplier<R> registryEntryFactory)
    {
        var registry = findRegistry(registryType).orElseThrow();
        Registry.registerForHolder(registry, registryEntry.getRegistryName(), registryEntryFactory.get());
    }

    @Override
    protected Runnable buildRegisterCallback(ResourceLocation registryName, Consumer<T> registerCallback)
    {
        return () -> findRegistry(registryType).flatMap(registry -> registry.getOptional(registryName)).ifPresent(registerCallback);
    }
}
