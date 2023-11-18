package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.RegistrationHelper;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

public final class FabricPlatform implements Platform, RegistrationHelper
{
    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType())
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new FabricModLoader();

    @Override
    public ModLoader modLoader()
    {
        return modLoader;
    }

    @Override
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public boolean isProduction()
    {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean runningDataGen()
    {
        return FabricDataGenHelper.ENABLED;
    }

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
