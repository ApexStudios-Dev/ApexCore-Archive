package xyz.apex.forge.apexcore.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.network.PacketDistributor;

import xyz.apex.forge.apexcore.core.command.CommandApex;
import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.core.net.ClientSyncSupportersPacket;
import xyz.apex.forge.apexcore.lib.net.NetworkManager;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.apexcore.lib.util.EventBusHelper;
import xyz.apex.forge.apexcore.lib.util.InterModUtil;
import xyz.apex.forge.apexcore.lib.util.ProfileHelper;
import xyz.apex.forge.apexcore.lib.util.SkinHelper;
import xyz.apex.forge.apexcore.revamp.net.packet.SyncContainerPacket;

@Mod(ApexCore.ID)
public final class ApexCore
{
	public static final String ID = "apexcore";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final NetworkManager NETWORK = new NetworkManager(ID, "main", "1");

	public ApexCore()
	{
		ProfileHelper.setup();
		ACRegistry.bootstrap();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> Client::new);

		EventBusHelper.addEnqueuedListener(FMLCommonSetupEvent.class, event -> {
			SupporterManager.loadSupporters();
			NETWORK.registerPackets(ClientSyncSupportersPacket.class, SyncContainerPacket.class);
		});

		EventBusHelper.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> {
			var player = event.getPlayer();
			LOGGER.info("Player ('{}') Connected! Syncing Supporters to Client Side!", player.getDisplayName().getString());
			NETWORK.sendTo(new ClientSyncSupportersPacket(SupporterManager.getSupporters()), PacketDistributor.PLAYER.with(() -> (ServerPlayer) player));
		});

		EventBusHelper.addListener(RegisterCommandsEvent.class, event -> CommandApex.register(event.getDispatcher()));

		EventBusHelper.addEnqueuedListener(InterModEnqueueEvent.class, event -> PlayerPlushie.getPlushieItems().forEach(plushieItem -> InterModUtil.sendFurnitureStationResult(ID, plushieItem)));
	}

	public static final class Client
	{
		private Client()
		{
			EventBusHelper.addEnqueuedListener(FMLLoadCompleteEvent.class, event -> SupporterManager.precacheSupporterSkins());
			EventBusHelper.addListener(ClientPlayerNetworkEvent.LoggedInEvent.class, event -> SkinHelper.invalidateCaches());
		}
	}
}
