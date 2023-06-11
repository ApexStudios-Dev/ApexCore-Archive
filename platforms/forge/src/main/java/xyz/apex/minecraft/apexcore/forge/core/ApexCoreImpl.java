package xyz.apex.minecraft.apexcore.forge.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ServerEvents;
import xyz.apex.minecraft.apexcore.common.lib.hook.Hooks;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.forge.lib.EventBuses;
import xyz.apex.minecraft.apexcore.forge.lib.hook.HooksImpl;
import xyz.apex.minecraft.apexcore.forge.lib.modloader.ModLoaderImpl;
import xyz.apex.minecraft.apexcore.forge.lib.network.NetworkManagerImpl;

import java.util.function.BiConsumer;

@Mod(ApexCore.ID)
public final class ApexCoreImpl extends ApexCore
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist) {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    private final ModLoader modLoader = new ModLoaderImpl();
    private final Hooks hooks = new HooksImpl();

    public ApexCoreImpl()
    {
        super();

        bootstrap();
    }

    @Override
    protected void bootstrap()
    {
        // check if entity is instance of forges fake player class
        // register before the one in common
        // to ensure forge specific check happens first
        EntityEvents.IS_FAKE_PLAYER.addListener(FakePlayer.class::isInstance);

        super.bootstrap();

        EventBuses.registerForJavaFML();
        setupEvents();
        PhysicalSide.CLIENT.runWhenOn(() -> ApexCoreClientImpl::new);
    }

    private void setupEvents()
    {
        wrapEvent(EntityJoinLevelEvent.class, MinecraftForge.EVENT_BUS, EntityEvents.JOIN_LEVEL, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getLevel()));
        wrapEvent(EntityLeaveLevelEvent.class, MinecraftForge.EVENT_BUS, EntityEvents.LEAVE_LEVEL, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getLevel()));

        wrapEvent(PlayerEvent.Clone.class, MinecraftForge.EVENT_BUS, PlayerEvents.COPY, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getOriginal(), forgeEvent.getEntity(), forgeEvent.isWasDeath()));
        wrapEvent(PlayerEvent.PlayerChangedDimensionEvent.class, MinecraftForge.EVENT_BUS, PlayerEvents.CHANGE_DIMENSION, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getFrom(), forgeEvent.getTo()));
        wrapEvent(PlayerEvent.PlayerRespawnEvent.class, MinecraftForge.EVENT_BUS, PlayerEvents.RESPAWN, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.isEndConquered()));

        wrapEvent(ServerStartingEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STARTING, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));
        wrapEvent(ServerStartedEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STARTED, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));
        wrapEvent(ServerStoppedEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STOPPING, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));
        wrapEvent(ServerStoppedEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STOPPED, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));

        ForgeEvents.register();
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

    static <F extends Event, E extends xyz.apex.minecraft.apexcore.common.lib.event.Event> void wrapEvent(Class<F> forgeEventType, IEventBus forgeEventBus, EventType<E> ourEventType, BiConsumer<F, E> ourHandler)
    {
        forgeEventBus.addListener(EventPriority.NORMAL, false, forgeEventType, forgeEvent -> ourHandler.accept(forgeEvent, ourEventType.post()));
    }
}
