package xyz.apex.minecraft.apexcore.common.lib.hook;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

import java.util.function.Supplier;

/**
 * Interface holding all known hooks
 */
@ApiStatus.NonExtendable
public interface Hooks extends Services.Service
{
    /**
     * Hooks for registering various renderer elements.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    RegisterRendererHooks registerRenderer();

    /**
     * Hooks for registering block and item color handlers.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    RegisterColorHandlerHooks registerColorHandler();
}
