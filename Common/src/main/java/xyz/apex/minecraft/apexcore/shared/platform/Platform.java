package xyz.apex.minecraft.apexcore.shared.platform;

import org.apache.logging.log4j.Logger;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;

import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.Set;

public interface Platform
{
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    PlatformRegistries registries();

    boolean isDevelopment();

    default boolean isProduction()
    {
        return !isDevelopment();
    }

    boolean isDataGeneration();

    default boolean isClient()
    {
        return isRunningOn(Environment.CLIENT);
    }

    default boolean isDedicatedServer()
    {
        return isRunningOn(Environment.DEDICATED_SERVER);
    }

    PlatformType getPlatformType();

    Environment getEnvironment();

    Logger getLogger();

    default boolean isRunningOn(PlatformType platformType)
    {
        return getPlatformType() == platformType;
    }

    default boolean isRunningOn(Environment environment)
    {
        return getEnvironment() == environment;
    }

    default boolean isRunningOn(PlatformType platformType, Environment environment)
    {
        return isRunningOn(platformType) && isRunningOn(environment);
    }

    default boolean isForge()
    {
        return isRunningOn(PlatformType.FORGE);
    }

    default boolean isFabric()
    {
        return isRunningOn(PlatformType.FABRIC);
    }

    Path getGameDir();

    default Path getModsDir()
    {
        return getGameDir().resolve("mods");
    }

    Set<String> getMods();

    boolean isModInstalled(String modId);

    boolean isFakePlayer(Entity entity);

    default Packet<ClientGamePacketListener> getEntityClientSpawnPacket(Entity entity)
    {
        return new ClientboundAddEntityPacket(entity);
    }
}
