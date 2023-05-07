package xyz.apex.minecraft.apexcore.common.lib.modloader;

import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Represents basic information about a loaded mod.
 */
@ApiStatus.NonExtendable
public interface Mod
{
    /**
     * @return ID forge this mod.
     */
    String id();

    /**
     * @return Display name for this mod.
     */
    String displayName();

    /**
     * @return Current version of this mod.
     */
    String version();

    /**
     * Returns path to resource within this mods jar file, or empty if file does not exist.
     * <p>
     * Fabric returns optional path, if file exists, whereas Forge returns the path regardless on if it exists or not.
     * We just wrap with a simple {@link Files#exists(Path, LinkOption...)} call.
     *
     * @param paths Path to be searched for, joined together using {@link File#separator}
     * @return Path to resource within this mods jar file, ir empty if file does not exist.
     */
    Optional<Path> findResource(String... paths);
}
