package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ServerEvents;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.fabric.lib.modloader.ModLoaderImpl;
import xyz.apex.minecraft.apexcore.fabric.lib.network.NetworkManagerImpl;

public final class ApexCoreImpl extends ApexCore implements ModInitializer
{
    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType()) {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    private final ModLoader modLoader = new ModLoaderImpl();

    public ApexCoreImpl()
    {
        super();
    }

    @Override
    public void onInitialize()
    {
        setupEvents();
    }

    private void setupEvents()
    {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> EntityEvents.JOIN_LEVEL.post().handle(entity, level));
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> EntityEvents.LEAVE_LEVEL.post().handle(entity, level));

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, conqueredEnd) -> PlayerEvents.COPY.post().handle(oldPlayer, newPlayer, conqueredEnd));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, oldDimension, newDimension) -> PlayerEvents.CHANGE_DIMENSION.post().handle(player, oldDimension.dimension(), newDimension.dimension()));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, conqueredEnd) -> PlayerEvents.RESPAWN.post().handle(newPlayer, conqueredEnd));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> ServerEvents.STARTING.post().handle(server));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerEvents.STARTED.post().handle(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerEvents.STOPPING.post().handle(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> ServerEvents.STOPPED.post().handle(server));
    }

    @Override
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public ModLoader modLoader()
    {
        return modLoader;
    }

    @Override
    public NetworkManager createNetworkManager(String ownerId)
    {
        return NetworkManagerImpl.getOrCreate(ownerId);
    }
}
