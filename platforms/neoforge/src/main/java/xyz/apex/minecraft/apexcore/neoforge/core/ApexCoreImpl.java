package xyz.apex.minecraft.apexcore.neoforge.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryHelper;
import xyz.apex.minecraft.apexcore.neoforge.lib.EventBuses;
import xyz.apex.minecraft.apexcore.neoforge.lib.network.NetworkManagerImpl;

import java.util.function.Supplier;

@ApiStatus.Internal
public final class ApexCoreImpl implements ApexCore
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist) {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };

    @Override
    public void bootstrap()
    {
        // check if entity is instance of neoforges fake player class
        // register before the one in common
        // to ensure neoforge specific check happens first
        EntityEvents.IS_FAKE_PLAYER.addListener(FakePlayer.class::isInstance);

        ApexCore.super.bootstrap();

        EventBuses.registerForJavaFML();
        NeoForgeEvents.register();
        PhysicalSide.CLIENT.runWhenOn(() -> ApexCoreClient.INSTANCE::bootstrap);
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
        // we could make use of `ModEvents` but that does not currently support priorities,
        // so we register a listener to be invoked when the matching IEventBus is registered
        EventBuses.addListener(registrar.getOwnerId(), modBus -> {
            modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, event -> registrar.onRegisterPre(event.getRegistryKey(), new RegistryHelper() {
                @Override
                public <T, R extends T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> entryFactory)
                {
                    event.register(registryType, helper -> helper.register(registryName, entryFactory.get()));
                }
            }));

            modBus.addListener(EventPriority.LOWEST, false, RegisterEvent.class, event -> registrar.onRegisterPost(event.getRegistryKey()));
        });
    }

    @Override
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties)
    {
        return new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, properties);
    }
}
