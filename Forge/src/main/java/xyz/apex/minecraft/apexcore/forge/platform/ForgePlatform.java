package xyz.apex.minecraft.apexcore.forge.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformEvents;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ForgePlatform implements Platform
{
    private final ForgeEvents events = new ForgeEvents(this);
    private final Logger logger = LogManager.getLogger();

    @Override
    public PlatformEvents events()
    {
        return events;
    }

    @Override
    public boolean isDevelopment()
    {
        // return !FMLEnvironment.production;
        return FMLEnvironment.naming.equals("mcp");
    }

    @Override
    public boolean isDataGeneration()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public boolean isClient()
    {
        return FMLEnvironment.dist.isClient();
    }

    @Override
    public boolean isDedicatedServer()
    {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    @Override
    public Type getPlatformType()
    {
        return Type.FORGE;
    }

    @Override
    public Logger getLogger()
    {
        return logger;
    }

    @Override
    public Path getGameDir()
    {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getModsDir()
    {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public Set<String> getMods()
    {
        return ModList.get().getMods().stream().map(IModInfo::getModId).collect(Collectors.toSet());
    }

    @Override
    public boolean isModInstalled(String modId)
    {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public <T, R extends T> Supplier<R> register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey, Supplier<R> factory)
    {
        var modRegistry = events.getModRegistry(registryType);
        return modRegistry.register(registryKey.location().getPath(), factory);
    }
}
