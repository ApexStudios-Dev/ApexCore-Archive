package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import xyz.apex.minecraft.apexcore.common.platform.Platform;

public interface PackRepositoryHooks
{
    static void registerPackRepository(PackRepository repository, RepositorySource source)
    {
        Platform.INSTANCE.internals().registerPackRepository(repository, source);
    }
}
