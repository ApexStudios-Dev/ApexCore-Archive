package dev.apexstudios.apexcore.neoforge.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.loader.ApexBootstrapper;
import dev.apexstudios.apexcore.common.loader.Mod;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.util.Services;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class NeoForgeModLoader implements ModLoader, Services.BootStrapped
{
    private final ApexBootstrapper bootstrapper = new NeoForgeApexBootstrapper();
    private final Map<String, Mod> mods = Maps.newHashMap();
    private final Set<String> modIdView = Collections.unmodifiableSet(mods.keySet());
    private final Collection<Mod> modView = Collections.unmodifiableCollection(mods.values());
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist)
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    @Override
    public void bootstrap()
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
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public boolean runningDataGen()
    {
        return DatagenModLoader.isRunningDataGen();
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
    public boolean isModLoaded(String id)
    {
        return mods.containsKey(id);
    }

    @Override
    public Mod getMod(String id)
    {
        return mods.get(id);
    }

    @Override
    public Set<String> getLoadedModIds()
    {
        return modIdView;
    }

    @Override
    public Collection<Mod> getLoadedMods()
    {
        return modView;
    }

    @Override
    public boolean isProduction()
    {
        return FMLEnvironment.production;
    }

    @Override
    public ApexBootstrapper bootstrapper()
    {
        return bootstrapper;
    }
}
