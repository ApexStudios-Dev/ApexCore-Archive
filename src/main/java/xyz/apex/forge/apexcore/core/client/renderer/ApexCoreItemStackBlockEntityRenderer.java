package xyz.apex.forge.apexcore.core.client.renderer;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.apexcore.lib.util.ProfileHelper;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public final class ApexCoreItemStackBlockEntityRenderer extends ItemStackTileEntityRenderer
{
	private final PlayerPlushieBlockEntityRenderer playerPlushieBlockEntityRenderer;
	private final Map<UUID, PlayerPlushieBlockEntity> playerPlushieBlockEntityMap = Maps.newHashMap();

	public ApexCoreItemStackBlockEntityRenderer()
	{
		super();

		playerPlushieBlockEntityRenderer = new PlayerPlushieBlockEntityRenderer(TileEntityRendererDispatcher.instance);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack pose, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
	{
		float partialTick = Minecraft.getInstance().getDeltaFrameTime();

		if(PlayerPlushie.PLAYER_PLUSHIE_BLOCK.isInStack(stack))
		{
			SupporterManager.SupporterInfo supporterInfo = PlayerPlushie.getSupporterInfo(stack);

			if(supporterInfo != null)
			{
				PlayerPlushieBlockEntity blockEntity = getPlayerPlushieBlockEntity(supporterInfo);

				if(blockEntity != null)
				{
					playerPlushieBlockEntityRenderer.renderLayers = false;
					playerPlushieBlockEntityRenderer.render(blockEntity, partialTick, pose, renderTypeBuffer, light, overlay);
					return;
				}
			}
		}

		super.renderByItem(stack, transformType, pose, renderTypeBuffer, light, overlay);
	}

	@Nullable
	private PlayerPlushieBlockEntity getPlayerPlushieBlockEntity(SupporterManager.SupporterInfo supporterInfo)
	{
		UUID playerId = supporterInfo.getPlayerId();

		if(playerPlushieBlockEntityMap.containsKey(playerId))
			return playerPlushieBlockEntityMap.get(playerId);

		PlayerPlushieBlockEntity blockEntity = PlayerPlushie.PLAYER_PLUSHIE_BLOCK_ENTITY.createBlockEntity();

		if(blockEntity != null)
		{
			GameProfile profile = ProfileHelper.getGameProfile(playerId, supporterInfo.getUsername());
			blockEntity.setGameProfile(profile);
			playerPlushieBlockEntityMap.put(playerId, blockEntity);
			return blockEntity;
		}

		return null;
	}
}
