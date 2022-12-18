package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformEvents;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class FabricPlatform implements Platform
{
    private final FabricEvents events = new FabricEvents(this);
    private final Logger logger = LogManager.getLogger();

    @Override
    public PlatformEvents events()
    {
        return events;
    }

    @Override
    public boolean isDevelopment()
    {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isDataGeneration()
    {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public boolean isClient()
    {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isDedicatedServer()
    {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    @Override
    public Type getPlatformType()
    {
        return Type.FABRIC;
    }

    @Override
    public Logger getLogger()
    {
        return logger;
    }

    @Override
    public Path getGameDir()
    {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getModsDir()
    {
        return getGameDir().resolve("mods");
    }

    @Override
    public Set<String> getMods()
    {
        return FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).map(ModMetadata::getId).collect(Collectors.toSet());
    }

    @Override
    public boolean isModInstalled(String modId)
    {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public <T, R extends T> Supplier<R> register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey, Supplier<R> factory)
    {
        return events.register(registryType, registryKey, factory);
    }
}
