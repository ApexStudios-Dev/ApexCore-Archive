package xyz.apex.minecraft.apexcore.neoforge.core;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.hook.Hooks;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.neoforge.lib.EventBuses;
import xyz.apex.minecraft.apexcore.neoforge.lib.hook.HooksImpl;
import xyz.apex.minecraft.apexcore.neoforge.lib.modloader.ModLoaderImpl;
import xyz.apex.minecraft.apexcore.neoforge.lib.network.NetworkManagerImpl;

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
        // check if entity is instance of neoforges fake player class
        // register before the one in common
        // to ensure neoforge specific check happens first
        EntityEvents.IS_FAKE_PLAYER.addListener(FakePlayer.class::isInstance);

        super.bootstrap();

        EventBuses.registerForJavaFML();
        NeoForgeEvents.register();
        PhysicalSide.CLIENT.runWhenOn(() -> ApexCoreClientImpl::new);
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
}
