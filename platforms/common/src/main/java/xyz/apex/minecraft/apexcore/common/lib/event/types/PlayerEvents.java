package xyz.apex.minecraft.apexcore.common.lib.event.types;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

@ApiStatus.NonExtendable
public interface PlayerEvents
{
    EventType<StartTrackingEntity> START_TRACKING_ENTITY = EventType.create(listeners -> (trackedEntity, player) -> listeners.forEach(listener -> listener.handle(trackedEntity, player)));
    EventType<EndTrackingEntity> END_TRACKING_ENTITY = EventType.create(listeners -> (trackedEntity, player) -> listeners.forEach(listener -> listener.handle(trackedEntity, player)));
    EventType<Copy> COPY = EventType.create(listeners -> (oldPlayer, newPlayer, conqueredEnd) -> listeners.forEach(listener -> listener.handle(oldPlayer, newPlayer, conqueredEnd)));
    EventType<ChangeDimension> CHANGE_DIMENSION = EventType.create(listeners -> (player, oldDimension, newDimension) -> listeners.forEach(listener -> listener.handle(player, oldDimension, newDimension)));
    EventType<Respawn> RESPAWN = EventType.create(listeners -> (player, conqueredEnd) -> listeners.forEach(listener -> listener.handle(player, conqueredEnd)));
    EventType<LoggedIn> LOGGED_IN = EventType.create(listeners -> player -> listeners.forEach(listener -> listener.handle(player)));
    EventType<LoggedOut> LOGGED_OUT = EventType.create(listeners -> player -> listeners.forEach(listener -> listener.handle(player)));
    EventType<PickupItem> PICKUP_ITEM = EventType.create(listeners -> (player, entity) -> listeners.stream().anyMatch(listener -> listener.handle(player, entity)));
    EventType<CraftItem> CRAFT_ITEM = EventType.create(listeners -> (player, stack, inventory) -> listeners.forEach(listener -> listener.handle(player, stack, inventory)));
    EventType<PickupExperience> PICKUP_EXPERIENCE = EventType.create(listeners -> (player, entity) -> listeners.stream().anyMatch(listener -> listener.handle(player, entity)));
    EventType<Death> DEATH = EventType.create(listeners -> (player, source) -> listeners.stream().anyMatch(listener -> listener.handle(player, source)));

    @ApiStatus.Internal
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface StartTrackingEntity extends Event
    {
        void handle(Entity trackedEntity, Player player);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface EndTrackingEntity extends Event
    {
        void handle(Entity trackedEntity, Player player);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Copy extends Event
    {
        void handle(Player oldPlayer, Player newPlayer, boolean conqueredEnd);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface ChangeDimension extends Event
    {
        void handle(Player player, ResourceKey<Level> oldDimension, ResourceKey<Level> newDimension);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Respawn extends Event
    {
        void handle(Player player, boolean conqueredEnd);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface LoggedIn extends Event
    {
        void handle(Player player);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface LoggedOut extends Event
    {
        void handle(Player player);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface PickupItem extends Event
    {
        boolean handle(Player player, ItemEntity entity);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface CraftItem extends Event
    {
        void handle(Player player, ItemStack stack, Container inventory);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface PickupExperience extends Event
    {
        boolean handle(Player player, ExperienceOrb orb);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Death extends Event
    {
        boolean handle(Player player, DamageSource source);
    }
}
