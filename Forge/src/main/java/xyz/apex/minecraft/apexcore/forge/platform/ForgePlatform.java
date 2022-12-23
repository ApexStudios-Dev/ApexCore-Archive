package xyz.apex.minecraft.apexcore.forge.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;

import xyz.apex.minecraft.apexcore.shared.platform.Environment;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistries;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformType;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public final class ForgePlatform implements Platform
{
    private final Logger logger = LogManager.getLogger("ApexCore/Forge");
    private final ForgePlatformRegistries registries = new ForgePlatformRegistries(this);
    protected final ForgePlatformModEvents modEvents = new ForgePlatformModEvents(this);

    public ForgePlatform()
    {
        logger.debug("Initializing ForgePlatform...");
    }

    @Override
    public PlatformRegistries registries()
    {
        return registries;
    }

    @Override
    public boolean isDevelopment()
    {
        // return !FMLEnvironment.production;
        return FMLEnvironment.naming.equals("mcp");
    }

    @Override
    public boolean isProduction()
    {
        return FMLEnvironment.production;
    }

    @Override
    public boolean isDataGeneration()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public PlatformType getPlatformType()
    {
        return PlatformType.FORGE;
    }

    @Override
    public Environment getEnvironment()
    {
        return switch(FMLEnvironment.dist) {
            case CLIENT -> Environment.CLIENT;
            case DEDICATED_SERVER -> Environment.DEDICATED_SERVER;
            default -> throw new IllegalStateException("Unknown FML Environment: %s".formatted(FMLEnvironment.dist));
        };
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
}
