package xyz.apex.minecraft.apexcore.common.core;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import xyz.apex.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.ColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.GenericHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.ParticleHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;

@ApiStatus.Internal
@ApiStatus.NonExtendable
@SideOnly(PhysicalSide.CLIENT)
public interface ApexCoreClient
{
    ApexCoreClient INSTANCE = Services.singleton(ApexCoreClient.class);

    ColorHandlerHooks COLOR_HANDLER_HOOKS = Services.singleton(ColorHandlerHooks.class);
    RendererHooks RENDERER_HOOKS = Services.singleton(RendererHooks.class);
    GenericHooks CLIENT_HOOKS = Services.singleton(GenericHooks.class);
    ParticleHooks PARTICLE_HOOKS = Services.singleton(ParticleHooks.class);

    @MustBeInvokedByOverriders
    default void bootstrap()
    {
    }
}
