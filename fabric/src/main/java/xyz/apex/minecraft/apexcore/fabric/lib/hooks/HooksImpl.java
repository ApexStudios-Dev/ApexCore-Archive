package xyz.apex.minecraft.apexcore.fabric.lib.hooks;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.Hooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterRendererHooks;

@ApiStatus.Internal
public final class HooksImpl implements Hooks
{
    private final RegisterRendererHooks registerRendererHooks = new RegisterRendererHooksImpl();
    private final RegisterColorHandlerHooks colorHandlerHooks = new RegisterColorHandlerHooksImpl();

    @Override
    public RegisterRendererHooks registerRenderer()
    {
        return registerRendererHooks;
    }

    @Override
    public RegisterColorHandlerHooks registerColorHandler()
    {
        return colorHandlerHooks;
    }
}
