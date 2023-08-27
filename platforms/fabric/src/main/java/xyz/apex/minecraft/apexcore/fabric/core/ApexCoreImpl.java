package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryHelper;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.MenuFactory;
import xyz.apex.minecraft.apexcore.common.lib.support.SupportManager;
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
        ApexCore.super.bootstrap();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> SupportManager.INSTANCE.sync(handler.player));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> SupportManager.INSTANCE.loadFromRemote());
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

    @Override
    public boolean isFakePlayer(@Nullable Entity entity)
    {
        return entity instanceof FakePlayer;
    }
}
