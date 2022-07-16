package xyz.apex.forge.apexcore.core.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import xyz.apex.forge.apexcore.lib.block.BaseMultiBlock;
import xyz.apex.forge.apexcore.lib.block.IMultiBlock;
import xyz.apex.forge.apexcore.lib.block.MultiBlockPattern;
import xyz.apex.forge.commonality.Mods;

import java.util.IdentityHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Mods.APEX_CORE, value = Dist.CLIENT)
public final class MultiBlockVisualizer
{
	@Nullable private static MultiBufferSource.BufferSource ghostBuffers = null;
	private static boolean renderOutline =  false;

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelLastEvent event)
	{
		var mc = Minecraft.getInstance();
		var player = mc.player;
		var level = mc.level;

		if(player != null && level != null)
		{
			var pose = event.getPoseStack();

			for(var hand : InteractionHand.values())
			{
				if(renderBlockFromHand(mc, pose, level, hand, player))
					break;
			}
		}
	}

	private static boolean renderBlockFromHand(Minecraft mc, PoseStack pose, ClientLevel level, InteractionHand hand, LocalPlayer player)
	{
		var stack = player.getItemInHand(hand);
		var block = Block.byItem(stack.getItem());

		if(block instanceof IMultiBlock multiBlock)
		{
			if(mc.hitResult instanceof BlockHitResult result)
			{
				var blockPos = result.getBlockPos();

				if(level.isEmptyBlock(blockPos))
					return false;

				var renderPos = blockPos.relative(result.getDirection());

				var placeContext = new BlockPlaceContext(level, player, hand, stack, result);
				var placeState = block.getStateForPlacement(placeContext);

				if(placeState == null)
				{
					var horizontalFacing = placeContext.getHorizontalDirection().getOpposite();
					var facing = block instanceof BaseMultiBlock base ? base.getFourWayFacing(placeContext) : horizontalFacing;
					facing = facing == null ? horizontalFacing : facing;

					placeState = block.defaultBlockState();
					placeState = BaseMultiBlock.setFacing(placeState, facing);
					placeState = multiBlock.setMultiBlockIndex(placeState, MultiBlockPattern.INDEX_ORIGIN);
				}

				placeState = BaseMultiBlock.setWaterLogged(placeState, false);

				var position = mc.getEntityRenderDispatcher().camera.getPosition();

				var buffers = mc.renderBuffers().bufferSource();

				var type = RenderType.lines();

				if(ghostBuffers == null)
					ghostBuffers = initBuffers(buffers);

				pose.pushPose();
				pose.translate(-position.x, -position.y, -position.z);

				if(renderOutline)
				{
					var consumer = buffers.getBuffer(type);
					renderBlockWithOutline(mc, pose, ghostBuffers, consumer, level, renderPos, placeState, multiBlock);
					buffers.endBatch(type);
				}
				else
					renderBlockWithoutOutline(mc, pose, ghostBuffers, level, renderPos, placeState, multiBlock);

				ghostBuffers.endBatch();
				pose.popPose();
				return true;
			}
		}

		return false;
	}

	private static void renderBlockWithOutline(Minecraft mc, PoseStack pose, MultiBufferSource.BufferSource bufferSource, VertexConsumer vertexConsumer, ClientLevel level, BlockPos pos, BlockState blockState, IMultiBlock multiBlock)
	{
		var origin = multiBlock.getMultiBlockOriginPos(blockState, pos);
		var localPositions = multiBlock.getMultiBlockLocalPositions();

		var isValid = true;

		for(var i = 0; i < localPositions.size(); i++)
		{
			var localPos = localPositions.get(i);
			var worldPos = multiBlock.getMultiBlockWorldSpaceFromLocalSpace(blockState, origin, localPos);
			var renderState = multiBlock.setMultiBlockIndex(blockState, i);
			var worldState = level.getBlockState(worldPos);

			if(!multiBlock.getMultiBlockPattern().passesPlacementTests(multiBlock, level, worldPos, renderState, worldState))
			{
				isValid = false;
				break;
			}
		}

		var overlay = isValid ? OverlayTexture.NO_OVERLAY : OverlayTexture.pack(OverlayTexture.NO_WHITE_U, OverlayTexture.RED_OVERLAY_V);

		for(var i = 0; i < localPositions.size(); i++)
		{
			var localPos = localPositions.get(i);
			var worldPos = multiBlock.getMultiBlockWorldSpaceFromLocalSpace(blockState, origin, localPos);
			var renderState = multiBlock.setMultiBlockIndex(blockState, i);
			renderBlockStateWithOutline(mc, pose, bufferSource, vertexConsumer, level, overlay, worldPos, renderState, multiBlock);
		}
	}

	private static void renderBlockWithoutOutline(Minecraft mc, PoseStack pose, MultiBufferSource.BufferSource bufferSource, ClientLevel level, BlockPos pos, BlockState blockState, IMultiBlock multiBlock)
	{
		var origin = multiBlock.getMultiBlockOriginPos(blockState, pos);
		var localPositions = multiBlock.getMultiBlockLocalPositions();

		var isValid = true;

		for(var i = 0; i < localPositions.size(); i++)
		{
			var localPos = localPositions.get(i);
			var worldPos = multiBlock.getMultiBlockWorldSpaceFromLocalSpace(blockState, origin, localPos);
			var renderState = multiBlock.setMultiBlockIndex(blockState, i);
			var worldState = level.getBlockState(worldPos);

			if(!multiBlock.getMultiBlockPattern().passesPlacementTests(multiBlock, level, worldPos, renderState, worldState))
			{
				isValid = false;
				break;
			}
		}

		var overlay = isValid ? OverlayTexture.NO_OVERLAY : OverlayTexture.pack(OverlayTexture.NO_WHITE_U, OverlayTexture.RED_OVERLAY_V);

		for(var i = 0; i < localPositions.size(); i++)
		{
			var localPos = localPositions.get(i);
			var worldPos = multiBlock.getMultiBlockWorldSpaceFromLocalSpace(blockState, origin, localPos);
			var renderState = multiBlock.setMultiBlockIndex(blockState, i);
			renderBlockStateWithoutOutline(mc, pose, bufferSource, overlay, worldPos, renderState);
		}
	}

	private static void renderBlockStateWithOutline(Minecraft mc, PoseStack pose, MultiBufferSource.BufferSource bufferSource, VertexConsumer vertexConsumer, ClientLevel level, int overlay, BlockPos pos, BlockState blockState, IMultiBlock multiBlock)
	{
		var x = pos.getX();
		var y = pos.getY();
		var z = pos.getZ();

		if(multiBlock.isMultiBlockOrigin(blockState))
		{
			// this does not render during Fabulous graphics due to Forge Issue #8603 (https://github.com/MinecraftForge/MinecraftForge/pull/8603)
			pose.pushPose();
			renderHitOutline(pose, vertexConsumer, level, pos, blockState, 0F, 0F, 0F, .4F);
			pose.popPose();
		}

		pose.translate(x, y, z);

		if(blockState.getRenderShape() == RenderShape.MODEL)
		{
			pose.pushPose();
			mc.getBlockRenderer().renderSingleBlock(blockState, pose, bufferSource, LightTexture.FULL_BLOCK, overlay, EmptyModelData.INSTANCE);
			pose.popPose();
		}

		pose.translate(-x, -y, -z);
	}

	private static void renderBlockStateWithoutOutline(Minecraft mc, PoseStack pose, MultiBufferSource.BufferSource bufferSource, int overlay, BlockPos pos, BlockState blockState)
	{
		var x = pos.getX();
		var y = pos.getY();
		var z = pos.getZ();

		pose.translate(x, y, z);

		if(blockState.getRenderShape() == RenderShape.MODEL)
		{
			pose.pushPose();
			mc.getBlockRenderer().renderSingleBlock(blockState, pose, bufferSource, LightTexture.FULL_BLOCK, overlay, EmptyModelData.INSTANCE);
			pose.popPose();
		}

		pose.translate(-x, -y, -z);
	}

	private static MultiBufferSource.BufferSource initBuffers(MultiBufferSource.BufferSource original)
	{
		var remapped = new Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>();

		for(var entry : original.fixedBuffers.entrySet())
		{
			remapped.put(GhostRenderLayer.remap(entry.getKey()), entry.getValue());
		}

		return new GhostBuffers(original.builder, remapped);
	}

	private static void renderHitOutline(PoseStack pose, VertexConsumer consumer, ClientLevel level, BlockPos pos, BlockState blockState, float r, float g, float b, float a)
	{
		var last = pose.last();

		blockState.getShape(level, pos).forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) -> {
			var f = (float) (maxX - minX);
			var f1 = (float) (maxY - minY);
			var f2 = (float) (maxZ - minZ);
			var f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
			f /= f3;
			f1 /= f3;
			f2 /= f3;
			consumer.vertex(last.pose(), (float) (minX + pos.getX()), (float) (minY + pos.getY()), (float) (minZ + pos.getZ())).color(r, g, b, a).normal(last.normal(), f, f1, f2).endVertex();
			consumer.vertex(last.pose(), (float) (maxX + pos.getX()), (float) (maxY + pos.getY()), (float) (maxZ + pos.getZ())).color(r, g, b, a).normal(last.normal(), f, f1, f2).endVertex();
		});
	}

	private static final class GhostBuffers extends MultiBufferSource.BufferSource
	{
		private GhostBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> buffers)
		{
			super(fallback, buffers);
		}

		@Override
		public VertexConsumer getBuffer(RenderType renderType)
		{
			return super.getBuffer(GhostRenderLayer.remap(renderType));
		}
	}

	private static final class GhostRenderLayer extends RenderType
	{
		private static final Map<RenderType, RenderType> remappedTypes = new IdentityHashMap<>();

		public GhostRenderLayer(RenderType original)
		{
			super("%s_%s_ghost".formatted(original, Mods.APEX_CORE), original.format(), original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
				original.setupRenderState();

				RenderSystem.disableDepthTest();
				RenderSystem.enableBlend();
				RenderSystem.setShaderColor(1F, 1F, 1F, .65F);
			}, () -> {
				RenderSystem.setShaderColor(1F,1F, 1F, 1F);
				RenderSystem.disableBlend();
				RenderSystem.enableDepthTest();

				original.clearRenderState();
			});
		}

		public static RenderType remap(RenderType in)
		{
			if(in instanceof GhostRenderLayer)
				return in;

			return remappedTypes.computeIfAbsent(in, GhostRenderLayer::new);
		}
	}
}