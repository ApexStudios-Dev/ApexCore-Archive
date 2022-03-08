package xyz.apex.forge.apexcore.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import xyz.apex.forge.apexcore.core.client.hats.LayerHat;
import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.lib.util.EventBusHelper;

@Mod(ApexCore.ID)
public final class ApexCore
{
	public static final String ID = "apexcore";
	public static final Logger LOGGER = LogManager.getLogger();

	public ApexCore()
	{
		ACRegistry.bootstrap();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> Client::new);
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
