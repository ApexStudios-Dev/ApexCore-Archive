package xyz.apex.forge.apexcore.core.client.renderer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public final class ApexCoreItemStackBlockEntityRenderer extends BlockEntityWithoutLevelRenderer
{
	private static final Lazy<BlockEntityWithoutLevelRenderer> INSTANCE = Lazy.of(() -> new ApexCoreItemStackBlockEntityRenderer(Minecraft.getInstance()));

	private final PlayerPlushieBlockEntityRenderer playerPlushieBlockEntityRenderer;
	private final Map<UUID, PlayerPlushieBlockEntity> playerPlushieBlockEntityMap = Maps.newHashMap();

	private ApexCoreItemStackBlockEntityRenderer(Minecraft mc)
	{
		super(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());

		var ctx = new BlockEntityRendererProvider.Context(mc.getBlockEntityRenderDispatcher(), mc.getBlockRenderer(), mc.getEntityModels(), mc.font);
		playerPlushieBlockEntityRenderer = new PlayerPlushieBlockEntityRenderer(ctx);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack pose, MultiBufferSource renderTypeBuffer, int light, int overlay)
	{
		var partialTick = Minecraft.getInstance().getDeltaFrameTime();

		if(PlayerPlushie.PLAYER_PLUSHIE_BLOCK.isInStack(stack))
		{
			var supporterInfo = PlayerPlushie.getSupporterInfo(stack);

			if(supporterInfo != null)
			{
				var blockEntity = getPlayerPlushieBlockEntity(supporterInfo);

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
		var playerId = supporterInfo.getPlayerId();

		if(playerPlushieBlockEntityMap.containsKey(playerId))
			return playerPlushieBlockEntityMap.get(playerId);

		var blockState = PlayerPlushie.PLAYER_PLUSHIE_BLOCK.defaultBlockState();
		var blockEntity = PlayerPlushie.PLAYER_PLUSHIE_BLOCK_ENTITY.createBlockEntity(BlockPos.ZERO, blockState);

		if(blockEntity != null)
		{
			blockEntity.setSupporterInfo(supporterInfo);
			playerPlushieBlockEntityMap.put(playerId, blockEntity);
			return blockEntity;
		}

		return null;
	}

	public static BlockEntityWithoutLevelRenderer getInstance()
	{
		return INSTANCE.get();
	}
}
