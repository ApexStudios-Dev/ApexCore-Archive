package xyz.apex.minecraft.apexcore.common.platform;

import java.util.Collection;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Stream;

public interface ModLoader
{
    ModLoader INSTANCE = ServiceLoader.load(ModLoader.class).findFirst().orElseThrow();

    String version();

    String id();

    String displayName();

    boolean isModLoaded(String modId);

    Collection<Mod> getMods();

    Stream<Mod> mods();

    Set<String> getModIdSet();

    Optional<Mod> getMod(String modId);
}
