package xyz.apex.minecraft.apexcore.common.platform;

import xyz.apex.minecraft.apexcore.common.hooks.Hooks;

import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public interface Platform
{
    Platform INSTANCE = load();

    String FORGE = "forge";
    String FABRIC = "fabricloader";
    String QUILT = "quilt";

    String getMinecraftVersion();

    boolean isSnapshot();

    boolean isDevelopment();

    boolean isDataGenActive();

    Side getPhysicalSide();

    Hooks hooks();

    ModLoader modLoader();

    Path getGameDir();

    Path getConfigDir();

    static void bootstrap() {}

    private static Platform load()
    {
        var providers = ServiceLoader.load(Platform.class).stream().toList();
        if(providers.size() == 1) return providers.get(0).get();
        var names = providers.stream().map(ServiceLoader.Provider::type).map(Class::getName).collect(Collectors.joining(",", "[", "]"));
        throw new IllegalStateException("There should be exactly one implementation of %s on the classpath. Found: %s".formatted(Platform.class.getName(), names));
    }
}
