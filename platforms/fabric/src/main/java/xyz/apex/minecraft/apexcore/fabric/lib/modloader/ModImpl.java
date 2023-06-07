package xyz.apex.minecraft.apexcore.fabric.lib.modloader;

import net.fabricmc.loader.api.ModContainer;
import xyz.apex.minecraft.apexcore.common.lib.modloader.Mod;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

record ModImpl(ModContainer modContainer) implements Mod
{
    @Override
    public String id()
    {
        return modContainer.getMetadata().getId();
    }

    @Override
    public String displayName()
    {
        return modContainer.getMetadata().getName();
    }

    @Override
    public String version()
    {
        return modContainer.getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public Optional<Path> findResource(String... paths)
    {
        return modContainer.findPath(String.join(File.separator, paths));
    }
}
