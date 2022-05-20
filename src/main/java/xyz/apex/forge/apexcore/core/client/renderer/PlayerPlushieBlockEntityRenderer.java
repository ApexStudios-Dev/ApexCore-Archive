package xyz.apex.forge.apexcore.core.client.renderer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import xyz.apex.forge.apexcore.core.block.PlayerPlushieBlock;
import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.lib.util.SkinHelper;
import xyz.apex.forge.apexcore.lib.util.reflection.MethodHelper;

import java.util.Map;
import java.util.UUID;

public class PlayerPlushieBlockEntityRenderer extends TileEntityRenderer<PlayerPlushieBlockEntity>
{
	private final PlayerModel<PlayerEntity> playerModelSlim;
	private final Iterable<ModelRenderer> headPartsSlim;
	private final Iterable<ModelRenderer> bodyPartsSlim;

	private final PlayerModel<PlayerEntity> playerModelThick;
	private final Iterable<ModelRenderer> headPartsThick;
	private final Iterable<ModelRenderer> bodyPartsThick;

	private final Map<UUID, ResourceLocation> playerSkins = Maps.newHashMap();

	public boolean renderLayers = true;

	public PlayerPlushieBlockEntityRenderer(TileEntityRendererDispatcher rendererDispatcher)
	{
		super(rendererDispatcher);

		playerModelSlim = new PlayerModel<>(0F, true);
		playerModelThick = new PlayerModel<>(0F, false);

		headPartsSlim = getHeadParts(playerModelSlim);
		headPartsThick = getHeadParts(playerModelThick);

		bodyPartsSlim = getBodyParts(playerModelSlim);
		bodyPartsThick = getBodyParts(playerModelThick);
	}

	@Override
	public void render(PlayerPlushieBlockEntity blockEntity, float partialTick, MatrixStack pose, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay)
	{
		GameProfile profile = blockEntity.getGameProfile();
		UUID playerId = profile.getId();
		String playerName = profile.getName();

		SkinHelper.getSkins(playerId, playerName, texture -> playerSkins.put(playerId, texture));

		PlayerModel<PlayerEntity> playerModel;
		Iterable<ModelRenderer> headParts;
		Iterable<ModelRenderer> bodyParts;

		if(SkinHelper.isSlim(playerId, playerName))
		{
			playerModel = playerModelSlim;
			headParts = headPartsSlim;
			bodyParts = bodyPartsSlim;
		}
		else
		{
			playerModel = playerModelThick;
			headParts = headPartsThick;
			bodyParts = bodyPartsThick;
		}

		ResourceLocation skin;

		if(playerSkins.containsKey(playerId))
			skin = playerSkins.get(playerId);
		else
			skin = DefaultPlayerSkin.getDefaultSkin(playerId);

		RenderType renderType = RenderType.entityTranslucent(skin);
		IVertexBuilder buffer = renderTypeBuffer.getBuffer(renderType);

		pose.pushPose();

		setupPlayerModel(playerModel, renderLayers);
		renderPlayerModel(blockEntity, playerModel, headParts, bodyParts, pose, buffer, combinedLight, combinedOverlay);

		pose.popPose();
	}

	private static void poseForPlushie(PlayerModel<PlayerEntity> playerModel)
	{
		// rot
		{
			playerModel.head.xRot = 0F;
			playerModel.head.yRot = 0F;
			playerModel.head.zRot = 0F;

			playerModel.body.xRot = -.4F;
			playerModel.body.yRot = 0F;
			playerModel.body.zRot = 0F;

			playerModel.leftArm.xRot = 0F;
			playerModel.leftArm.yRot = 0F;
			playerModel.leftArm.zRot = -.5F;

			playerModel.rightArm.xRot = 0F;
			playerModel.rightArm.yRot = 0F;
			playerModel.rightArm.zRot = .5F;

			playerModel.leftLeg.xRot = -1.6F;
			playerModel.leftLeg.yRot = -.5F;
			playerModel.leftLeg.zRot = 0F;

			playerModel.rightLeg.xRot = -1.6F;
			playerModel.rightLeg.yRot = .5F;
			playerModel.rightLeg.zRot = 0F;
		}

		// pos
		{
			playerModel.head.x = 0F;
			playerModel.head.y = 0F;
			playerModel.head.z = 0F;

			playerModel.body.x = 0F;
			playerModel.body.y = 0F;
			playerModel.body.z = 0F;

			playerModel.leftArm.x = 4.1F;
			playerModel.leftArm.y = 2F;
			playerModel.leftArm.z = 0F;

			playerModel.rightArm.x = -4.2F;
			playerModel.rightArm.y = 2F;
			playerModel.rightArm.z = 0F;

			playerModel.leftLeg.x = 1F;
			playerModel.leftLeg.y = 12F;
			playerModel.leftLeg.z = -3.5F;

			playerModel.rightLeg.x = -1F;
			playerModel.rightLeg.y = 12F;
			playerModel.rightLeg.z = -3F;
		}
	}

	private static void setupPlayerModel(PlayerModel<PlayerEntity> playerModel, boolean renderLayers)
	{
		playerModel.setAllVisible(true);

		playerModel.crouching = false;
		playerModel.young = true;

		/*Minecraft mc = Minecraft.getInstance();

		if(mc.player != null)
			playerModel.young = mc.player.isShiftKeyDown();*/

		poseForPlushie(playerModel);

		// ModelHelper.bobArms(playerModel.rightArm, playerModel.leftArm, ageInTicks);
		// ModelHelper.bobArms(playerModel.rightLeg, playerModel.leftLeg, ageInTicks);

		playerModel.leftPants.copyFrom(playerModel.leftLeg);
		playerModel.rightPants.copyFrom(playerModel.rightLeg);
		playerModel.leftSleeve.copyFrom(playerModel.leftArm);
		playerModel.rightSleeve.copyFrom(playerModel.rightArm);
		playerModel.jacket.copyFrom(playerModel.body);
		playerModel.hat.copyFrom(playerModel.head);

		playerModel.leftPants.visible = renderLayers;
		playerModel.rightPants.visible = renderLayers;
		playerModel.leftSleeve.visible = renderLayers;
		playerModel.rightSleeve.visible = renderLayers;
		playerModel.jacket.visible = renderLayers;
		playerModel.hat.visible = renderLayers;
	}

	private static void renderPlayerModel(PlayerPlushieBlockEntity blockEntity, PlayerModel<PlayerEntity> playerModel, Iterable<ModelRenderer> headParts, Iterable<ModelRenderer> bodyParts, MatrixStack pose, IVertexBuilder buffer, int combinedLight, int combinedOverlay)
	{
		if(blockEntity.hasLevel())
		{
			pose.translate(.5D, .5D, .5D);
			BlockState blockState = blockEntity.getBlockState();
			Direction facing = blockState.getValue(PlayerPlushieBlock.FACING);
			pose.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
			pose.mulPose(Vector3f.XP.rotationDegrees(180F));
			pose.translate(0D, 0D, .2D);
		}
		else
		{
			pose.translate(.5D, .5D, .5D);
			pose.mulPose(Vector3f.ZP.rotationDegrees(180F));
		}

		double yOffset = -.35D;

		if(playerModel.young)
			yOffset = -.65D;

		pose.translate(0D, yOffset, 0D);

		if(playerModel.young)
		{
			pose.pushPose();

			boolean scaleHead = true;
			float babyHeadScale = 2F;
			float babyBodyScale = 2F;
			float yHeadOffset = 16F;
			float zHeadOffset = 0F;
			float bodyYOffset = 24F;

			if(scaleHead)
			{
				float f = 1.5F / babyHeadScale;
				pose.scale(f, f, f);
			}

			pose.translate(0D, yHeadOffset / 16F, zHeadOffset / 16F);

			headParts.forEach(renderer -> renderer.render(pose, buffer, combinedLight, combinedOverlay, 1F, 1F, 1F, 1F));
			playerModel.hat.render(pose, buffer, combinedLight, combinedOverlay, 1F, 1F, 1F, 1F);

			pose.popPose();
			pose.pushPose();

			float f1 = 1F / babyBodyScale;

			pose.scale(f1, f1, f1);
			pose.translate(0D, bodyYOffset / 16F, 0D);

			bodyParts.forEach(renderer -> renderer.render(pose, buffer, combinedLight, combinedOverlay, 1F, 1F, 1F, 1F));

			pose.popPose();
		}
		else
		{
			headParts.forEach(renderer -> renderer.render(pose, buffer, combinedLight, combinedOverlay, 1F, 1F, 1F, 1F));
			bodyParts.forEach(renderer -> renderer.render(pose, buffer, combinedLight, combinedOverlay, 1F, 1F, 1F, 1F));
		}

		// playerModel.renderToBuffer(pose, buffer, combinedLight, combinedOverlay, 1F, 1F, 1F, 1F);
	}

	private static Iterable<ModelRenderer> getHeadParts(PlayerModel<PlayerEntity> playerModel)
	{
		try
		{
			return MethodHelper.invokeMethod(AgeableModel.class, playerModel, "headParts", new Class[0], new Object[0]);
		}
		catch(Exception e)
		{
			return ImmutableList.of(playerModel.head);
		}
	}

	private static Iterable<ModelRenderer> getBodyParts(PlayerModel<PlayerEntity> playerModel)
	{
		try
		{
			return MethodHelper.invokeMethod(AgeableModel.class, playerModel, "bodyParts", new Class[0], new Object[0]);
		}
		catch(Exception e)
		{
			return ImmutableList.of(
					playerModel.leftPants, playerModel.rightPants, playerModel.leftSleeve, playerModel.rightSleeve, playerModel.jacket,
					playerModel.body, playerModel.rightArm, playerModel.leftArm, playerModel.rightLeg, playerModel.leftLeg, playerModel.hat
			);
		}
	}
}
