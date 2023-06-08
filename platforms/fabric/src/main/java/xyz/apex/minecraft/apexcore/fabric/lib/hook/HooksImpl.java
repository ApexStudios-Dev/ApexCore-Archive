package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.*;

@ApiStatus.Internal
public final class HooksImpl implements Hooks
{
    private final RendererHooks registerRendererHooks = new RendererHooksImpl();
    private final ColorHandlerHooks colorHandlerHooks = new ColorHandlerHooksImpl();
    private final EntityHooks entityHooks = new EntityHooksImpl();
    private final GameRuleHooks gameRuleHooks = new GameRuleHooksImpl();
    private final CreativeModeTabHooks creativeModeTabHooks = new CreativeModeTabHooksImpl();
    private final MenuHooks menuHooks = new MenuHooksImpl();

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
    public RendererHooks renderer()
    {
        return registerRendererHooks;
    }

    @Override
    public ColorHandlerHooks colorHandler()
    {
        return colorHandlerHooks;
    }
}
