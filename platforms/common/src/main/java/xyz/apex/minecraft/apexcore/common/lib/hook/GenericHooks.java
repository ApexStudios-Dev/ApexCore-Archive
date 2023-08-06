package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.PlatformOnly;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

/**
 * Hooks for various generic things.
 */
@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface GenericHooks // TODO: think up a better name for this
{
    /**
     * Registers a reload listener to the clients resource manager.
     *
     * @param packType Pack type to register reload listener.
     * @param id ID used to register this listener with (Fabric only).
     * @param reloadListener Reload listener to be registered.
     */
    void registerReloadListener(PackType packType, @PlatformOnly(PlatformOnly.FABRIC) ResourceLocation id, PreparableReloadListener reloadListener);

    /**
     * @return Global instance.
     */
    static GenericHooks get()
    {
        return ApexCoreClient.CLIENT_HOOKS;
    }
}
