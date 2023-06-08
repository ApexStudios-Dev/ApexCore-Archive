package xyz.apex.minecraft.apexcore.common.lib.hook;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;

import java.util.function.Supplier;

/**
 * Interface holding all known hooks
 */
@ApiStatus.NonExtendable
public interface Hooks
{
    /**
     * Hooks for various entity related things.
     */
    EntityHooks entity();

    /**
     * Hooks for registering custom game rule types.
     */
    GameRuleHooks gameRules();

    /**
     * Various for registering new and modifying existing creative mode tabs.
     */
    CreativeModeTabHooks creativeModeTabs();

    /**
     * Hooks for various menu related things.
     */
    MenuHooks menu();

    /**
     * Hooks for registering & looking up entries to platform specific registries.
     * <p>
     * Recommended to never use this directly, but make use of {@link Registrar Registrars}.
     */
    RegistryHooks registry();

    /**
     * Hooks for registering various renderer elements.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    RendererHooks renderer();

    /**
     * Hooks for registering color handlers.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    ColorHandlerHooks colorHandler();

    /**
     * @return Global instance.
     */
    static Hooks get()
    {
        return ApexCore.get().hooks();
    }
}
