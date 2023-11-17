package dev.apexstudios.apexcore.mcforge.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.loader.ApexBootstrapper;
import dev.apexstudios.apexcore.common.loader.Mod;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.util.Services;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class McForgeModLoader implements ModLoader, Services.BootStrapped
{
    private final ApexBootstrapper bootstrapper = new McForgeApexBootstrapper();
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
        ModList.get().forEachModContainer((id, mod) -> mods.put(id, new McForgeMod(mod)));
    }

    @Override
    public String id()
    {
        return ForgeVersion.MOD_ID;
    }

    @Override
    public String displayName()
    {
        return "MinecraftForge";
    }

    @Override
    public String version()
    {
        return ForgeVersion.getVersion();
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
