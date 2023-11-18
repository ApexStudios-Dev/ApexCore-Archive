package dev.apexstudios.apexcore.neoforge.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.loader.Mod;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

final class NeoForgeModLoader implements ModLoader
{
    private final Map<String, Mod> mods = Maps.newHashMap();
    private final Set<String> modIdView = Collections.unmodifiableSet(mods.keySet());
    private final Collection<Mod> modView = Collections.unmodifiableCollection(mods.values());

    NeoForgeModLoader()
    {
        ModList.get().forEachModContainer((id, mod) -> mods.put(id, new NeoForgeMod(mod)));
    }

    @Override
    public String id()
    {
        return NeoForgeVersion.MOD_ID;
    }

    @Override
    public String displayName()
    {
        return "MinecraftForge";
    }

    @Override
    public String version()
    {
        return NeoForgeVersion.getVersion();
    }

    @Override
    public boolean isNeo()
    {
        return false;
    }

    @Override
    public boolean isForge()
    {
        return true;
    }

    @Override
    public boolean isFabric()
    {
        return false;
    }

    @Override
    public boolean isLoaded(String id)
    {
        return mods.containsKey(id);
    }

    @Override
    public Mod get(String id)
    {
        return mods.get(id);
    }

    @Override
    public Set<String> getLoadedIds()
    {
        return modIdView;
    }

    @Override
    public Collection<Mod> getLoaded()
    {
        return modView;
    }
}
