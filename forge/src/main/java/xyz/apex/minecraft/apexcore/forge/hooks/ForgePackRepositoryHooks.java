package xyz.apex.minecraft.apexcore.forge.hooks;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import xyz.apex.minecraft.apexcore.common.hooks.PackRepositoryHooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

public final class ForgePackRepositoryHooks extends ForgePlatformHolder implements PackRepositoryHooks
{
    ForgePackRepositoryHooks(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public void registerPackRepository(PackRepository repository, RepositorySource source)
    {
        repository.addPackFinder(source);
    }
}
