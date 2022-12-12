package xyz.apex.minecraft.apexcore.shared.platform;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;

public interface Platform
{
    PlatformEvents events();

    boolean isDevelopment();
    boolean isDataGeneration();
    boolean isClient();
    boolean isDedicatedServer();

    Type getPlatformType();
    Logger getLogger();

    default boolean isRunningOn(Type platform)
    {
        return getPlatformType() == platform;
    }

    default boolean isForge()
    {
        return isRunningOn(Type.FORGE);
    }

    default boolean isFabric()
    {
        return isRunningOn(Type.FABRIC);
    }

    Path getGameDir();
    Path getModsDir();

    Set<String> getMods();
    boolean isModInstalled(String modId);

    <T, R extends T> Supplier<R> register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey, Supplier<R> factory);

    enum Type
    {
        FABRIC("fabric", "c"), // for some reason fabric has all compat tags under 'c'
        FORGE("forge"),
        // Internal platform, never used in production
        // internal-private usages only
        @ApiStatus.Internal
        VANILLA("minecraft");

        public final String modId;
        private final String tagNamespace;

        Type(String modId, String tagNamespace)
        {
            this.modId = modId;
            this.tagNamespace = tagNamespace;
        }

        Type(String modId)
        {
            this(modId, modId);
        }

        public <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String name)
        {
            return TagKey.create(registryType, new ResourceLocation(tagNamespace, name));
        }
    }
}
