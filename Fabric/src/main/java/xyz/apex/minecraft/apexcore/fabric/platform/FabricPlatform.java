package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.Entity;

import xyz.apex.minecraft.apexcore.shared.platform.Environment;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistries;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformType;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public final class FabricPlatform implements Platform
{
    private final Logger logger = LogManager.getLogger("ApexCore/Fabric");
    private final FabricPlatformRegistries registries = new FabricPlatformRegistries(this);
    protected final FabricPlatformModEvents modEvents = new FabricPlatformModEvents(this);

    public FabricPlatform()
    {
        logger.debug("Initializing FabricPlatform...");
    }

    @Override
    public PlatformRegistries registries()
    {
        return registries;
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
    public PlatformType getPlatformType()
    {
        return PlatformType.FORGE;
    }

    @Override
    public Environment getEnvironment()
    {
        var environmentType = FabricLoader.getInstance().getEnvironmentType();

        return switch(environmentType) {
            case CLIENT -> Environment.CLIENT;
            case SERVER -> Environment.DEDICATED_SERVER;
            default -> throw new IllegalStateException("Unknown FML Environment: %s".formatted(environmentType));
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
        return FabricLoader.getInstance().getGameDir();
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
    public boolean isFakePlayer(Entity entity)
    {
        // everything in fabric is implemented on a mod by mod basis
        // so theres no real way of checking of a given entity is a
        // *FakePlayer* - some entity to automate player based tasks without needing a player
        // deployers, harvesters, planters, milkers etc
        // fabric also does not currently have a entity type tag for this
        return false;
    }
}
