package xyz.apex.minecraft.apexcore.forge.hooks;

import xyz.apex.minecraft.apexcore.common.hooks.Hooks;
import xyz.apex.minecraft.apexcore.common.hooks.PackRepositoryHooks;
import xyz.apex.minecraft.apexcore.common.hooks.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

public final class ForgeHooks extends ForgePlatformHolder implements Hooks
{
    private final ForgePackRepositoryHooks packRepository;
    private final ForgeRegistryHooks registry;
    private final ForgeRendererHooks renderer;
    private final ForgeCreativeModeTabHooks creativeModeTab;

    public ForgeHooks(ForgePlatform platform)
    {
        super(platform);

        packRepository = new ForgePackRepositoryHooks(platform);
        registry = new ForgeRegistryHooks(platform);
        renderer = new ForgeRendererHooks(platform);
        creativeModeTab = new ForgeCreativeModeTabHooks(platform);
    }

    @Override
    public PackRepositoryHooks packRepository()
    {
        return packRepository;
    }

    @Override
    public RegistryHooks registry()
    {
        return registry;
    }

    @Override
    public RendererHooks renderer()
    {
        return renderer;
    }

    @Override
    public ForgeCreativeModeTabHooks creativeModeTab()
    {
        return creativeModeTab;
    }
}
