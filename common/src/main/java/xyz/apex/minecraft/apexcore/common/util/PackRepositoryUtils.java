package xyz.apex.minecraft.apexcore.common.util;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import xyz.apex.minecraft.apexcore.common.platform.Platform;

public interface PackRepositoryUtils
{
    static void registerPackRepository(PackRepository repository, RepositorySource source)
    {
        Platform.INSTANCE.internals().registerPackRepository(repository, source);
    }
}
