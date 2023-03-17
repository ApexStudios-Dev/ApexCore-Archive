package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.Person;
import xyz.apex.minecraft.apexcore.common.platform.Mod;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

record FabricMod(ModContainer modContainer) implements Mod
{
    @Override
    public String id()
    {
        return modContainer.getMetadata().getId();
    }

    @Override
    public String version()
    {
        return modContainer.getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public String displayName()
    {
        return modContainer.getMetadata().getName();
    }

    @Override
    public Collection<String> authors()
    {
        return modContainer.getMetadata().getAuthors().stream().map(Person::getName).collect(Collectors.toList());
    }

    @Override
    public Collection<String> license()
    {
        return modContainer.getMetadata().getLicense();
    }

    @Override
    public List<Path> filePaths()
    {
        return modContainer.getRootPaths();
    }

    @Override
    public Optional<Path> findResource(String... path)
    {
        return modContainer.findPath(String.join(File.separator, path));
    }
}
