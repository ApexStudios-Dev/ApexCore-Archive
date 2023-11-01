package dev.apexstudios.apexcore.fabric.platform;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.platform.Mod;
import dev.apexstudios.apexcore.common.platform.ModLoader;
import net.fabricmc.loader.api.FabricLoader;

import java.util.*;

final class ModLoaderImpl implements ModLoader
{
    private final Map<String, Mod> mods = Maps.newHashMap();
    private final Collection<Mod> modsView = Collections.unmodifiableCollection(mods.values());
    private final Set<String> idsView = Collections.unmodifiableSet(mods.keySet());

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
    public Optional<Mod> find(String id)
    {
        return Optional.ofNullable(mods.get(id));
    }

    @Override
    public Collection<Mod> getLoaded()
    {
        return modsView;
    }

    @Override
    public Set<String> getLoadedIds()
    {
        return idsView;
    }

    public void loadMods()
    {
        ApexCore.LOGGER.debug("Parsing loaded mods list");
        FabricLoader.getInstance().getAllMods().forEach(container -> mods.put(container.getMetadata().getId(), new ModImpl(container)));
    }
}
