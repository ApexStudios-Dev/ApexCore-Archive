package dev.apexstudios.apexcore.fabric.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.loader.ApexBootstrapper;
import dev.apexstudios.apexcore.common.loader.Mod;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.util.Services;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class FabricModLoader implements ModLoader, Services.BootStrapped
{
    private final ApexBootstrapper bootstrapper = new FabricApexBootstrapper();
    private final Map<String, Mod> mods = Maps.newHashMap();
    private final Set<String> modIdView = Collections.unmodifiableSet(mods.keySet());
    private final Collection<Mod> modView = Collections.unmodifiableCollection(mods.values());
    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType())
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    @Override
    public void bootstrap()
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
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public boolean runningDataGen()
    {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public boolean isProduction()
    {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
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
    public ApexBootstrapper bootstrapper()
    {
        return bootstrapper;
    }
}
