package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;

public interface PackRepositoryHooks extends PlatformHolder
{
    void registerPackRepository(PackRepository repository, RepositorySource source);

    static PackRepositoryHooks getInstance()
    {
        return Hooks.getInstance().packRepository();
    }
}
