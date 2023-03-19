package xyz.apex.minecraft.apexcore.forge.platform;

import net.minecraftforge.fml.ModContainer;
import xyz.apex.minecraft.apexcore.common.platform.Mod;
import xyz.apex.minecraft.apexcore.common.platform.ModLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class ForgeMod extends ForgePlatformHolder implements Mod
{
    private final ModContainer modContainer;

    ForgeMod(ForgePlatform platform, ModContainer modContainer)
    {
        super(platform);

        this.modContainer = modContainer;
    }

    @Override
    public String id()
    {
        return modContainer.getModId();
    }

    @Override
    public String namespace()
    {
        return modContainer.getNamespace();
    }

    @Override
    public String version()
    {
        return modContainer.getModInfo().getVersion().toString();
    }

    @Override
    public String displayName()
    {
        return modContainer.getModInfo().getDisplayName();
    }

    @Override
    public Collection<String> authors()
    {
        return modContainer.getModInfo().getConfig().getConfigElement("authors").map(String::valueOf).map(Collections::singleton).orElseGet(Collections::emptySet);
    }

    @Override
    public Collection<String> license()
    {
        return Collections.singleton(modContainer.getModInfo().getOwningFile().getLicense());
    }

    @Override
    public List<Path> filePaths()
    {
        return List.of(modContainer.getModInfo().getOwningFile().getFile().getSecureJar().getRootPath());
    }

    @Override
    public Optional<Path> findResource(String... path)
    {
        return Optional.of(modContainer.getModInfo().getOwningFile().getFile().findResource(path)).filter(Files::exists);
    }

    @Override
    public ModLoader modLoader()
    {
        return platform.modLoader();
    }
}
