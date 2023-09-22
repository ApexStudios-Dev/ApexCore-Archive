package xyz.apex.minecraft.apexcore.mcforge.lib.hook;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.GenericHooks;
import xyz.apex.minecraft.apexcore.mcforge.core.ModEvents;
import xyz.apex.minecraft.apexcore.mcforge.lib.EventBusHelper;

@ApiStatus.Internal
public final class GenericHooksImpl implements GenericHooks
{
    @Override
    public void registerReloadListener(PackType packType, ResourceLocation id, PreparableReloadListener reloadListener)
    {
        switch(packType) {
            case CLIENT_RESOURCES -> PhysicalSide.CLIENT.runWhenOn(() -> () -> ModEvents.active().addListener(RegisterClientReloadListenersEvent.class, event -> event.registerReloadListener(reloadListener)));
            case SERVER_DATA -> EventBusHelper.addListener(MinecraftForge.EVENT_BUS, AddReloadListenerEvent.class, event -> event.addListener(reloadListener));
        }
    }
}
