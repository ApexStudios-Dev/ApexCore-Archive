package xyz.apex.minecraft.apexcore.forge.hooks;

import xyz.apex.minecraft.apexcore.common.hooks.Hooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

public final class ForgeHooks extends ForgePlatformHolder implements Hooks
{
    private final ForgePackRepositoryHooks packRepository;
    private final ForgeRegistryHooks registry;
    private final ForgeRendererHooks renderer;
    private final ForgeCreativeModeTabHooks creativeModeTab;
    private final ForgeItemHooks item;
    private final ForgeEntityHooks entity;

    public ForgeHooks(ForgePlatform platform)
    {
        super(platform);

        packRepository = new ForgePackRepositoryHooks(platform);
        registry = new ForgeRegistryHooks(platform);
        renderer = new ForgeRendererHooks(platform);
        creativeModeTab = new ForgeCreativeModeTabHooks(platform);
        item = new ForgeItemHooks(platform);
        entity = new ForgeEntityHooks(platform);
    }

    @Override
    public ForgePackRepositoryHooks packRepository()
    {
        return packRepository;
    }

    @Override
    public ForgeRegistryHooks registry()
    {
        return registry;
    }

    @Override
    public ForgeRendererHooks renderer()
    {
        return renderer;
    }

    @Override
    public ForgeCreativeModeTabHooks creativeModeTab()
    {
        return creativeModeTab;
    }

    @Override
    public ForgeItemHooks item()
    {
        return item;
    }

    @Override
    public ForgeEntityHooks entity()
    {
        return entity;
    }
}
