package dev.apexstudios.apexcore.common.platform;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ModLoader
{
    boolean isLoaded(String id);

    Mod get(String id);

    Optional<Mod> find(String id);

    Collection<Mod> getLoaded();

    Set<String> getLoadedIds();

    static ModLoader get()
    {
        return Platform.INSTANCE.modLoader();
    }
}
