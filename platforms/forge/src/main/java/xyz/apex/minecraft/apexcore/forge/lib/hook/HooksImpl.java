package xyz.apex.minecraft.apexcore.forge.lib.hook;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.*;

@ApiStatus.Internal
public final class HooksImpl implements Hooks
{
    private final RendererHooks rendererHooks = new RendererHooksImpl();
    private final ColorHandlerHooks colorHandlerHooks = new ColorHandlerHooksImpl();
    private final EntityHooks entityHooks = new EntityHooksImpl();
    private final GameRuleHooks gameRuleHooks = new GameRuleHooksImpl();
    private final CreativeModeTabHooks creativeModeTabHooks = new CreativeModeTabHooksImpl();
    private final MenuHooks menuHooks = new MenuHooksImpl();
    private final RegistryHooks registryHooks = new RegistryHooksImpl();

    @Override
    public EntityHooks entity()
    {
        return entityHooks;
    }

    @Override
    public GameRuleHooks gameRules()
    {
        return gameRuleHooks;
    }

    @Override
    public CreativeModeTabHooks creativeModeTabs()
    {
        return creativeModeTabHooks;
    }

    @Override
    public MenuHooks menu()
    {
        return menuHooks;
    }

    @Override
    public RegistryHooks registry()
    {
        return registryHooks;
    }

    @Override
    public RendererHooks renderer()
    {
        return rendererHooks;
    }

    @Override
    public ColorHandlerHooks colorHandler()
    {
        return colorHandlerHooks;
    }
}
