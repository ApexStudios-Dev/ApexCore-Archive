package xyz.apex.minecraft.apexcore.fabric.hooks;

import xyz.apex.minecraft.apexcore.common.hooks.Hooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

public final class FabricHooks extends FabricPlatformHolder implements Hooks
{
    private final FabricPackRepositoryHooks packRepository;
    private final FabricRegistryHooks registry;
    private final FabricRendererHooks renderer;
    private final FabricCreativeModeTabHooks creativeModeTab;
    private final FabricItemHooks item;
    private final FabricEntityHooks entity;

    public FabricHooks(FabricPlatform platform)
    {
        super(platform);

        packRepository = new FabricPackRepositoryHooks(platform);
        registry = new FabricRegistryHooks(platform);
        renderer = new FabricRendererHooks(platform);
        creativeModeTab = new FabricCreativeModeTabHooks(platform);
        item = new FabricItemHooks(platform);
        entity = new FabricEntityHooks(platform);
    }

    @Override
    public FabricPackRepositoryHooks packRepository()
    {
        return packRepository;
    }

    @Override
    public FabricRegistryHooks registry()
    {
        return registry;
    }

    @Override
    public FabricRendererHooks renderer()
    {
        return renderer;
    }

    @Override
    public FabricCreativeModeTabHooks creativeModeTab()
    {
        return creativeModeTab;
    }

    @Override
    public FabricItemHooks item()
    {
        return item;
    }

    @Override
    public FabricEntityHooks entity()
    {
        return entity;
    }
}
