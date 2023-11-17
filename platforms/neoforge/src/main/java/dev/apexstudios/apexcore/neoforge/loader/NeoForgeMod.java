package dev.apexstudios.apexcore.neoforge.loader;

import dev.apexstudios.apexcore.common.loader.Mod;
import net.neoforged.fml.ModContainer;

record NeoForgeMod(ModContainer mod) implements Mod
{
    @Override
    public String id()
    {
        return mod.getModId();
    }

    @Override
    public String displayName()
    {
        return mod.getModInfo().getDisplayName();
    }

    @Override
    public String version()
    {
        return mod.getModInfo().getVersion().toString();
    }
}
