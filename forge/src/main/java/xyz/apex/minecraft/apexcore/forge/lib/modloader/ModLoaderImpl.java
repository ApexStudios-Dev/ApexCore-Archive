package xyz.apex.minecraft.apexcore.forge.lib.modloader;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.versions.forge.ForgeVersion;
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
        var loader = ModList.get();
        vanilla = loader.getModContainerById("minecraft").map(ModImpl::new).orElseThrow();

        var builder = ImmutableMap.<String, Mod>builder();
        loader.forEachModContainer((modId, modContainer) -> builder.put(modId, new ModImpl(modContainer)));
        modMap = builder.build();

        modIdsView = Collections.unmodifiableSet(modMap.keySet());
        modsView = Collections.unmodifiableCollection(modMap.values());
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
