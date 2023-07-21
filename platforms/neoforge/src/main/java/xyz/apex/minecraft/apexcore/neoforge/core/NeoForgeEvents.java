package xyz.apex.minecraft.apexcore.neoforge.core;

import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ServerEvents;

import java.util.Map;
import java.util.function.BiConsumer;

@ApiStatus.Internal
final class NeoForgeEvents
{
    private static final ResourceLocation CAPABILITY_NAME = new ResourceLocation(ApexCore.ID, "capabilities");

    public static void register()
    {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, NeoForgeEvents::onAttachBlockEntityCapability);

        wrapEvent(EntityJoinLevelEvent.class, MinecraftForge.EVENT_BUS, EntityEvents.JOIN_LEVEL, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getLevel()));
        wrapEvent(EntityLeaveLevelEvent.class, MinecraftForge.EVENT_BUS, EntityEvents.LEAVE_LEVEL, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getLevel()));

        wrapEvent(PlayerEvent.Clone.class, MinecraftForge.EVENT_BUS, PlayerEvents.COPY, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getOriginal(), forgeEvent.getEntity(), forgeEvent.isWasDeath()));
        wrapEvent(PlayerEvent.PlayerChangedDimensionEvent.class, MinecraftForge.EVENT_BUS, PlayerEvents.CHANGE_DIMENSION, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getFrom(), forgeEvent.getTo()));
        wrapEvent(PlayerEvent.PlayerRespawnEvent.class, MinecraftForge.EVENT_BUS, PlayerEvents.RESPAWN, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.isEndConquered()));
        wrapEvent(PlayerEvent.PlayerLoggedInEvent.class, MinecraftForge.EVENT_BUS, PlayerEvents.LOGGED_IN, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity()));
        wrapEvent(PlayerEvent.PlayerLoggedOutEvent.class, MinecraftForge.EVENT_BUS, PlayerEvents.LOGGED_OUT, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity()));
        wrapCancelableEvent(EntityItemPickupEvent.class, MinecraftForge.EVENT_BUS, PlayerEvents.PICKUP_ITEM, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getItem()));
        wrapCancelableEvent(PlayerXpEvent.PickupXp.class, MinecraftForge.EVENT_BUS, PlayerEvents.PICKUP_EXPERIENCE, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getEntity(), forgeEvent.getOrb()));

        wrapEvent(ServerStartingEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STARTING, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));
        wrapEvent(ServerStartedEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STARTED, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));
        wrapEvent(ServerStoppedEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STOPPING, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));
        wrapEvent(ServerStoppedEvent.class, MinecraftForge.EVENT_BUS, ServerEvents.STOPPED, (forgeEvent, ourEvent) -> ourEvent.handle(forgeEvent.getServer()));

        wrapCancelableEvent(LivingDeathEvent.class, MinecraftForge.EVENT_BUS, EntityEvents.LIVING_DEATH, (forgeEvent, ourEvent) -> {
            var entity = forgeEvent.getEntity();
            var damageSource = forgeEvent.getSource();

            if(entity instanceof Player player && PlayerEvents.DEATH.post().handle(player, damageSource))
                return true;
            return EntityEvents.LIVING_DEATH.post().handle(entity, damageSource);
        });
    }

    public static <F extends Event, E extends xyz.apex.minecraft.apexcore.common.lib.event.Event> void wrapEvent(Class<F> forgeEventType, IEventBus forgeEventBus, EventType<E> ourEventType, BiConsumer<F, E> ourHandler)
    {
        forgeEventBus.addListener(EventPriority.NORMAL, false, forgeEventType, forgeEvent -> ourHandler.accept(forgeEvent, ourEventType.post()));
    }

    public static <F extends Event, E extends xyz.apex.minecraft.apexcore.common.lib.event.Event> void wrapCancelableEvent(Class<F> forgeEventType, IEventBus forgeEventBus, EventType<E> ourEventType, ToBooleanBiFunction<F, E> ourHandler)
    {
        forgeEventBus.addListener(EventPriority.NORMAL, false, forgeEventType, forgeEvent -> {
            var cancelled = ourHandler.applyAsBoolean(forgeEvent, ourEventType.post());

            if(forgeEvent.isCancelable() && cancelled)
                forgeEvent.setCanceled(true);
        });
    }

    private static void onAttachBlockEntityCapability(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if(!(event.getObject() instanceof BlockEntityComponentHolder componentHolder))
            return;

        class ComponentCapabilityProvider implements ICapabilityProvider
        {
            private final Map<Capability<?>, LazyOptional<?>> capabilities = Maps.newHashMap();
            private boolean lookedUp = false;

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side)
            {
                lookupCapabilities();
                var cached = capabilities.get(capability);

                if(cached != null)
                    return cached.cast();

                return LazyOptional.empty();
            }

            private void lookupCapabilities()
            {
                if(lookedUp)
                    return;

                componentHolder.findComponent(BlockEntityComponentTypes.INVENTORY).ifPresent(component -> capabilities.put(ForgeCapabilities.ITEM_HANDLER, LazyOptional.of(() -> new InvWrapper(component))));

                lookedUp = true;
            }

            private void invalidate()
            {
                capabilities.values().forEach(LazyOptional::invalidate);
                capabilities.clear();
                lookedUp = false;
            }
        }

        var provider = new ComponentCapabilityProvider();
        event.addCapability(CAPABILITY_NAME, provider);
        event.addListener(provider::invalidate);
    }
}
