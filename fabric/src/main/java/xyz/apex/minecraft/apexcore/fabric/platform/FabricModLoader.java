package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.collect.Maps;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import xyz.apex.minecraft.apexcore.common.platform.Mod;
import xyz.apex.minecraft.apexcore.common.platform.ModLoader;

import java.util.*;
import java.util.stream.Stream;

public final class FabricModLoader implements ModLoader
{
    private final Map<String, Mod> mods = Maps.newHashMap();
    private final Set<String> modIdView = Collections.unmodifiableSet(mods.keySet());
    private final Collection<Mod> modView = Collections.unmodifiableCollection(mods.values());
    private boolean init = false;

    @Override
    public String version()
    {
        return FabricLoaderImpl.VERSION;
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
    public boolean isModLoaded(String modId)
    {
        lookupMods();
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Collection<Mod> getMods()
    {
        lookupMods();
        return modView;
    }

    @Override
    public Stream<Mod> mods()
    {
        lookupMods();
        return modView.stream();
    }

    @Override
    public Set<String> getModIdSet()
    {
        lookupMods();
        return modIdView;
    }

    @Override
    public Optional<Mod> getMod(String modId)
    {
        lookupMods();
        return Optional.ofNullable(mods.get(modId));
    }

    private void lookupMods()
    {
        if(init) return;
        FabricLoader.getInstance().getAllMods().forEach(mod -> mods.putIfAbsent(mod.getMetadata().getId(), new FabricMod(mod)));
        init = true;
    }
}
