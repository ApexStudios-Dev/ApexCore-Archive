package xyz.apex.minecraft.apexcore.neoforge.core;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.neoforge.lib.EventBuses;
import xyz.apex.minecraft.apexcore.neoforge.lib.network.NetworkManagerImpl;

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
}
