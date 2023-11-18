package dev.apexstudios.apexcore.fabric.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.loader.Mod;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

final class FabricModLoader implements ModLoader
{
    private final Map<String, Mod> mods = Maps.newHashMap();
    private final Set<String> modIdView = Collections.unmodifiableSet(mods.keySet());
    private final Collection<Mod> modView = Collections.unmodifiableCollection(mods.values());

    FabricModLoader()
    {
        FabricLoader.getInstance().getAllMods().forEach(mod -> mods.put(mod.getMetadata().getId(), new FabricMod(mod)));
    }

    @Override
    public String id()
    {
        // fabric has many mod ids due to it being modular
        // there are 4 which make sense to be used here
        // `fabric` - provided by `fabric-api`
        // `fabric-api` - full fat mod jar containing all fabric api modules
        // `fabric-api-base` - base api jar containing the minimum api elements
        // `fabricloader` - the mod loader for fabric
        return FabricLoaderImpl.MOD_ID;
    }

    @Override
    public String displayName()
    {
        return "FabricLoader";
    }

    @Override
    public String version()
    {
        return FabricLoaderImpl.VERSION;
    }

    @Override
    public boolean isNeo()
    {
        return false;
    }

    @Override
    public boolean isForge()
    {
        return false;
    }

    @Override
    public boolean isFabric()
    {
        return true;
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
