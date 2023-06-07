package xyz.apex.minecraft.apexcore.common.lib.modloader;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Represents basic information about the current ModLoader.
 */
@ApiStatus.NonExtendable
public interface ModLoader
{
    /**
     * @return ID for this mod loader.
     */
    String id();

    /**
     * @return Display name for this mod loader.
     */
    String displayName();

    /**
     * @return Current version for this mod loader.
     */
    String version();

    /**
     * @return Mod representation for the base game.
     */
    Mod vanilla();

    /**
     * Returns mod for the given id if loaded, or empty.
     *
     * @param id Id of mod to look up.
     * @return Mod for the given id if loaded, or empty.
     */
    Optional<Mod> findMod(String id);

    /**
     * Returns true if mod for given id is loaded.
     *
     * @param id Id of mod to look up.
     * @return True if mod for given id is loaded.
     */
    boolean isLoaded(String id);

    /**
     * @return Set of all currently loaded mod ids.
     */
    Set<String> getLoadedModIds();

    /**
     * @return Collection of all currently loaded mods.
     */
    Collection<Mod> getLoadedMods();

    static ModLoader get()
    {
        return ApexCore.get().getModLoader();
    }
}
