package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ServerEvents;
import xyz.apex.minecraft.apexcore.common.lib.event.types.TickEvents;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryHelper;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.MenuFactory;
import xyz.apex.minecraft.apexcore.fabric.lib.network.NetworkManagerImpl;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class ApexCoreImpl implements ApexCore, RegistryHelper
{
    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType()) {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    private final List<ResourceKey<? extends Registry<?>>> registryOrder = List.of(
            Registries.ENTITY_TYPE,
            Registries.FLUID,
            Registries.BLOCK,
            Registries.ITEM
    );

    @Override
    public void bootstrap()
    {
        // check if entity is instance of fabrics fake player class
        // register before the one in common
        // to ensure fabric specific check happens first
        EntityEvents.IS_FAKE_PLAYER.addListener(FakePlayer.class::isInstance);

        ApexCore.super.bootstrap();

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

        CommandRegistrationCallback.EVENT.register((dispatcher, context, environment) -> ServerEvents.REGISTER_COMMANDS.post().handle(dispatcher, environment, context));
    }

    @Override
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public NetworkManager createNetworkManager(String ownerId)
    {
        return NetworkManagerImpl.getOrCreate(ownerId);
    }

    @Override
    public void register(AbstractRegistrar<?> registrar)
    {
        var registryTypes = BuiltInRegistries.REGISTRY.registryKeySet().stream().filter(Predicate.not(registryOrder::contains)).toList();

        registryOrder.forEach(registryType -> registrar.onRegisterPre(registryType, this));
        registryTypes.forEach(registryType -> registrar.onRegisterPre(registryType, this));
        registryOrder.forEach(registrar::onRegisterPost);
        registryTypes.forEach(registrar::onRegisterPost);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T, R extends T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> entryFactory)
    {
        var registry = (Registry<T>) BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryType);
        Registry.registerForHolder(registry, registryName, entryFactory.get());
    }

    @Override
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties)
    {
        return new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, properties);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> menuFactory, Supplier<MenuType<T>> selfSupplier)
    {
        return new ExtendedScreenHandlerType<>((syncId, inventory, buffer) -> menuFactory.create(selfSupplier.get(), syncId, inventory, buffer));
    }
}
