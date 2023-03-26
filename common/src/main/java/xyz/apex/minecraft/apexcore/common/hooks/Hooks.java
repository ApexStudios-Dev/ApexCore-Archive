package xyz.apex.minecraft.apexcore.common.hooks;

import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;

public interface Hooks extends PlatformHolder
{
    PackRepositoryHooks packRepository();

    RegistryHooks registry();

    RendererHooks renderer();

    CreativeModeTabHooks creativeModeTab();

    ItemHooks item();

    static Hooks getInstance()
    {
        return Platform.INSTANCE.hooks();
    }
}
