package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.loader.ApexBootstrapper;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.RegistrationHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

final class FabricApexBootstrapper implements ApexBootstrapper, RegistrationHelper
{
    @Override
    public void register(AbstractRegister<?> register)
    {
        BuiltInRegistries.REGISTRY.forEach(registry -> register.onRegister(registry.key(), this));
        BuiltInRegistries.REGISTRY.forEach(registry -> register.onRegisterLate(registry.key()));
    }

    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> valueKey, T value)
    {
        var registry = BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryType);
        Registry.registerForHolder(registry, valueKey, value);
    }
}
