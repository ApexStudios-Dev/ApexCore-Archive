package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.versions.forge.ForgeVersion;
import xyz.apex.minecraft.apexcore.common.platform.Mod;
import xyz.apex.minecraft.apexcore.common.platform.ModLoader;

import java.util.*;
import java.util.stream.Stream;

public final class ForgeModLoader extends ForgePlatformHolder implements ModLoader
{
    private final Map<String, Mod> mods = Maps.newHashMap();
    private final Set<String> modIdView = Collections.unmodifiableSet(mods.keySet());
    private final Collection<Mod> modView = Collections.unmodifiableCollection(mods.values());
    private boolean init = false;

    ForgeModLoader(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public String version()
    {
        return ForgeVersion.getVersion();
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
    public boolean isModLoaded(String modId)
    {
        lookupMods();
        return ModList.get().isLoaded(modId);
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
        ModList.get().forEachModContainer((modId, mod) -> mods.putIfAbsent(modId, new ForgeMod(platform, mod)));
        init = true;
    }
}
