package xyz.apex.forge.apexcore.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import xyz.apex.forge.apexcore.core.client.hats.LayerHat;
import xyz.apex.forge.apexcore.core.command.CommandApex;
import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.core.net.ClientSyncSupportersPacket;
import xyz.apex.forge.apexcore.lib.net.NetworkManager;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.apexcore.lib.util.EventBusHelper;

@Mod(ApexCore.ID)
public final class ApexCore
{
	public static final String ID = "apexcore";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final NetworkManager NETWORK = new NetworkManager(ID, "main", "1");

	public ApexCore()
	{
		ACRegistry.bootstrap();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> Client::new);

		EventBusHelper.addEnqueuedListener(FMLCommonSetupEvent.class, event -> {
			SupporterManager.loadSupporters();
			NETWORK.registerPacket(ClientSyncSupportersPacket.class);
		});

		EventBusHelper.addListener(PlayerEvent.PlayerLoggedInEvent.class, event -> {
			PlayerEntity player = event.getPlayer();
			LOGGER.info("Player ('{}') Connected! Syncing Supporters to Client Side!", player.getDisplayName().getString());
			NETWORK.sendTo(new ClientSyncSupportersPacket(SupporterManager.getSupporters()), PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player));
		});

		EventBusHelper.addListener(RegisterCommandsEvent.class, event -> CommandApex.register(event.getDispatcher()));
	}

	public static final class Client
	{
		private Client()
		{
			EventBusHelper.addEnqueuedListener(FMLLoadCompleteEvent.class, event -> {
				EntityRendererManager entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
				LayerHat layerHat = new LayerHat();
				entityRenderDispatcher.getSkinMap().values().forEach(r -> r.addLayer(layerHat));
			});
		}
	}
}
