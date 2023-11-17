package dev.apexstudios.apexcore.mcforge.loader;

import dev.apexstudios.apexcore.common.loader.Mod;
import net.minecraftforge.fml.ModContainer;

record McForgeMod(ModContainer mod) implements Mod
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
