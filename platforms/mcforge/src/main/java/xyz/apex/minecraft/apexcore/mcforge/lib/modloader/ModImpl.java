package xyz.apex.minecraft.apexcore.mcforge.lib.modloader;

import net.minecraftforge.fml.ModContainer;
import xyz.apex.minecraft.apexcore.common.lib.modloader.Mod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

record ModImpl(ModContainer modContainer) implements Mod
{
    @Override
    public String id()
    {
        return modContainer.getModId();
    }

    @Override
    public String displayName()
    {
        return modContainer.getModInfo().getDisplayName();
    }

    @Override
    public String version()
    {
        return modContainer.getModInfo().getVersion().toString();
    }

    @Override
    public Optional<Path> findResource(String... paths)
    {
        try
        {
            var path = modContainer.getModInfo().getOwningFile().getFile().findResource(String.join(File.separator, paths));
            if(Files.exists(path)) return Optional.of(path);
        }
        catch(Throwable ignored)
        {
            // TODO: maybe log exception?
        }

        return Optional.empty();
    }
}
