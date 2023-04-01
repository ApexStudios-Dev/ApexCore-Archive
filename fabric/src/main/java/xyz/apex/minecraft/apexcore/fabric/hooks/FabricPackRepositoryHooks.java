package xyz.apex.minecraft.apexcore.fabric.hooks;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import xyz.apex.minecraft.apexcore.common.hooks.PackRepositoryHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

public final class FabricPackRepositoryHooks extends FabricPlatformHolder implements PackRepositoryHooks
{
    FabricPackRepositoryHooks(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public void registerPackRepository(PackRepository repository, RepositorySource source)
    {
        if(repository.sources instanceof ImmutableSet) repository.sources = Sets.newHashSet(repository.sources);
        repository.sources.add(source);
    }
}
