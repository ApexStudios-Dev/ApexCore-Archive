package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.loader.Mod;
import net.fabricmc.loader.api.ModContainer;

record FabricMod(ModContainer mod) implements Mod
{
    @Override
    public String id()
    {
        return mod.getMetadata().getId();
    }

    @Override
    public String displayName()
    {
        return mod.getMetadata().getName();
    }

    @Override
    public String version()
    {
        return mod.getMetadata().getVersion().getFriendlyString();
    }
}
