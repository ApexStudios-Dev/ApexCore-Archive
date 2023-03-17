package xyz.apex.minecraft.apexcore.forge.platform;

import net.minecraftforge.fml.ModContainer;
import xyz.apex.minecraft.apexcore.common.platform.Mod;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

record ForgeMod(String modId, ModContainer mod) implements Mod
{
    @Override
    public String id()
    {
        return modId;
    }

    @Override
    public String namespace()
    {
        return mod.getNamespace();
    }

    @Override
    public String version()
    {
        return mod.getModInfo().getVersion().toString();
    }

    @Override
    public String displayName()
    {
        return mod.getModInfo().getDisplayName();
    }

    @Override
    public Collection<String> authors()
    {
        return mod.getModInfo().getConfig().getConfigElement("authors").map(String::valueOf).map(Collections::singleton).orElseGet(Collections::emptySet);
    }

    @Override
    public Collection<String> license()
    {
        return Collections.singleton(mod.getModInfo().getOwningFile().getLicense());
    }

    @Override
    public List<Path> filePaths()
    {
        return List.of(mod.getModInfo().getOwningFile().getFile().getSecureJar().getRootPath());
    }

    @Override
    public Optional<Path> findResource(String... path)
    {
        return Optional.of(mod.getModInfo().getOwningFile().getFile().findResource(path)).filter(Files::exists);
    }
}
