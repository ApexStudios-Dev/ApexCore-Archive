package xyz.apex.minecraft.apexcore.fabric.hooks;

import xyz.apex.minecraft.apexcore.common.hooks.Hooks;
import xyz.apex.minecraft.apexcore.common.hooks.PackRepositoryHooks;
import xyz.apex.minecraft.apexcore.common.hooks.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

public final class FabricHooks extends FabricPlatformHolder implements Hooks
{
    private final FabricPackRepositoryHooks packRepository;
    private final FabricRegistryHooks registry;
    private final FabricRendererHooks renderer;

    public FabricHooks(FabricPlatform platform)
    {
        super(platform);

        packRepository = new FabricPackRepositoryHooks(platform);
        registry = new FabricRegistryHooks(platform);
        renderer = new FabricRendererHooks(platform);
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
}
