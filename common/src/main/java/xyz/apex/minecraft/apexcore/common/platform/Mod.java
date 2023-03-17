package xyz.apex.minecraft.apexcore.common.platform;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Mod
{
    String id();

    default String namespace()
    {
        return id();
    }

    String version();

    String displayName();

    Collection<String> authors();

    Collection<String> license();

    List<Path> filePaths();

    Optional<Path> findResource(String... path);
}
