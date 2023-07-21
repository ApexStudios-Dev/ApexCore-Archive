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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ServerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.TickEvents;

import java.util.Map;

@ApiStatus.Internal
final class NeoForgeEvents
{
    private static final ResourceLocation CAPABILITY_NAME = new ResourceLocation(ApexCore.ID, "capabilities");

    public static void register()
    {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, NeoForgeEvents::onAttachBlockEntityCapability);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.StartTracking.class, event -> PlayerEvents.START_TRACKING_ENTITY.post().handle(event.getTarget(), event.getEntity()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.StopTracking.class, event -> PlayerEvents.END_TRACKING_ENTITY.post().handle(event.getTarget(), event.getEntity()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, EntityJoinLevelEvent.class, event -> EntityEvents.JOIN_LEVEL.post().handle(event.getEntity(), event.getLevel()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, EntityLeaveLevelEvent.class, event -> EntityEvents.LEAVE_LEVEL.post().handle(event.getEntity(), event.getLevel()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.Clone.class, event -> PlayerEvents.COPY.post().handle(event.getOriginal(), event.getEntity(), event.isWasDeath()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerChangedDimensionEvent.class, event -> PlayerEvents.CHANGE_DIMENSION.post().handle(event.getEntity(), event.getFrom(), event.getTo()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerRespawnEvent.class, event -> PlayerEvents.RESPAWN.post().handle(event.getEntity(), event.isEndConquered()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerLoggedInEvent.class, event -> PlayerEvents.LOGGED_IN.post().handle(event.getEntity()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerLoggedOutEvent.class, event -> PlayerEvents.LOGGED_OUT.post().handle(event.getEntity()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.ItemCraftedEvent.class, event -> PlayerEvents.CRAFT_ITEM.post().handle(event.getEntity(), event.getCrafting(), event.getInventory()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ServerStartingEvent.class, event -> ServerEvents.STARTING.post().handle(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ServerStartedEvent.class, event -> ServerEvents.STARTED.post().handle(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ServerStoppedEvent.class, event -> ServerEvents.STOPPING.post().handle(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ServerStoppedEvent.class, event -> ServerEvents.STOPPED.post().handle(event.getServer()));

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TickEvent.ServerTickEvent.class, event -> {
            var server = event.getServer();

            switch(event.phase)
            {
                case START -> TickEvents.START_SERVER.post().handle(server);
                case END -> TickEvents.END_SERVER.post().handle(server);
            }
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TickEvent.PlayerTickEvent.class, event -> {
            switch(event.phase)
            {
                case START -> TickEvents.START_PLAYER.post().handle(event.player);
                case END -> TickEvents.END_PLAYER.post().handle(event.player);
            }
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, LivingEvent.LivingTickEvent.class, event -> TickEvents.START_LIVING_ENTITY.post().handle(event.getEntity()));

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, EntityItemPickupEvent.class, event -> {
            if(PlayerEvents.PICKUP_ITEM.post().handle(event.getEntity(), event.getItem()))
                event.setCanceled(true);
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerXpEvent.PickupXp.class, event -> {
            if(PlayerEvents.PICKUP_EXPERIENCE.post().handle(event.getEntity(), event.getOrb()))
                event.setCanceled(true);
        });

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, LivingDeathEvent.class, event -> {
            var entity = event.getEntity();
            var damageSource = event.getSource();

            if(entity instanceof Player player)
            {
                if(PlayerEvents.DEATH.post().handle(player, damageSource))
                    event.setCanceled(true);
            }

            if(EntityEvents.LIVING_DEATH.post().handle(entity, damageSource))
                event.setCanceled(true);
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
