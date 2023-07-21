package xyz.apex.minecraft.apexcore.fabric.core;

import com.google.errorprone.annotations.DoNotCall;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ServerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.TickEvents;
import xyz.apex.minecraft.apexcore.common.lib.hook.Hooks;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.fabric.lib.hook.HooksImpl;
import xyz.apex.minecraft.apexcore.fabric.lib.modloader.ModLoaderImpl;
import xyz.apex.minecraft.apexcore.fabric.lib.network.NetworkManagerImpl;

@ApiStatus.Internal
final class ApexCoreImpl extends ApexCore
{
    private static final ApexCoreImpl INSTANCE = new ApexCoreImpl();

    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType()) {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    private final ModLoader modLoader = new ModLoaderImpl();
    private final Hooks hooks = new HooksImpl();

    private ApexCoreImpl()
    {
        super();
    }

    @Override
    protected void bootstrap()
    {
        // check if entity is instance of fabrics fake player class
        // register before the one in common
        // to ensure fabric specific check happens first
        EntityEvents.IS_FAKE_PLAYER.addListener(FakePlayer.class::isInstance);

        super.bootstrap();

        setupEvents();
    }

    private void setupEvents()
    {
        ServerTickEvents.START_SERVER_TICK.register(server -> TickEvents.START_SERVER.post().handle(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> TickEvents.END_SERVER.post().handle(server));

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> PlayerEvents.START_TRACKING_ENTITY.post().handle(trackedEntity, player));
        EntityTrackingEvents.STOP_TRACKING.register((trackedEntity, player) -> PlayerEvents.END_TRACKING_ENTITY.post().handle(trackedEntity, player));

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
    public Hooks hooks()
    {
        return hooks;
    }

    @Override
    public NetworkManager createNetworkManager(String ownerId)
    {
        return NetworkManagerImpl.getOrCreate(ownerId);
    }

    @DoNotCall
    static void bootstrap0()
    {
        Validate.isTrue(ApexCore.get() == INSTANCE); // THIS SHOULD NEVER FAIL
        INSTANCE.bootstrap();
    }
}
