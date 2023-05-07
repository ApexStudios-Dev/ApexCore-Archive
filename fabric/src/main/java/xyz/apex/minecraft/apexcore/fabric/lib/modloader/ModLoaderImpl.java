package xyz.apex.minecraft.apexcore.fabric.lib.modloader;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import xyz.apex.minecraft.apexcore.common.lib.modloader.Mod;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;

import java.util.*;

public final class ModLoaderImpl implements ModLoader
{
    private final Map<String, Mod> modMap;
    private final Set<String> modIdsView;
    private final Collection<Mod> modsView;
    private final Mod vanilla;

    public ModLoaderImpl()
    {
        var loader = FabricLoader.getInstance();
        vanilla = loader.getModContainer("minecraft").map(ModImpl::new).orElseThrow();

        var builder = ImmutableMap.<String, Mod>builder();
        FabricLoader.getInstance().getAllMods().stream().map(ModImpl::new).forEach(mod -> builder.put(mod.id(), mod));
        modMap = builder.build();

        modIdsView = Collections.unmodifiableSet(modMap.keySet());
        modsView = Collections.unmodifiableCollection(modMap.values());
    }

    @Override
    public String id()
    {
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
    public Mod vanilla()
    {
        return vanilla;
    }

    @Override
    public Optional<Mod> findMod(String id)
    {
        return Optional.ofNullable(modMap.get(id));
    }

    @Override
    public boolean isLoaded(String id)
    {
        return modMap.containsKey(id);
    }

    @Override
    public Set<String> getLoadedModIds()
    {
        return modIdsView;
    }

    @Override
    public Collection<Mod> getLoadedMods()
    {
        return modsView;
    }
}
