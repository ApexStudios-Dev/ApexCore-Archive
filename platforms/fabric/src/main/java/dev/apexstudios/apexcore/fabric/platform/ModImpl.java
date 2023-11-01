package dev.apexstudios.apexcore.fabric.platform;

import dev.apexstudios.apexcore.common.platform.Mod;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

record ModImpl(ModContainer mod) implements Mod
{
    @Override
    public String id()
    {
        return null;
    }

    @Override
    public String displayName()
    {
        return null;
    }

    @Override
    public String issueTrackerURL()
    {
        return null;
    }

    @Override
    public Optional<String> sourcesURL()
    {
        return Optional.empty();
    }

    @Override
    public Optional<String> displayURL()
    {
        return Optional.empty();
    }
}
