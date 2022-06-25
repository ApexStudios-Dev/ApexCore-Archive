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
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import xyz.apex.forge.apexcore.revamp.block.IMultiBlock;
import xyz.apex.forge.commonality.Mods;

import java.util.IdentityHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Mods.APEX_CORE, value = Dist.CLIENT)
public final class MultiBlockVisualizer
{
	@Nullable private static MultiBufferSource.BufferSource ghostBuffers = null;

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelLastEvent event)
	{
		try
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
		catch(Exception e)
		{
			System.out.println(e.getLocalizedMessage());
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
				var renderPos = result.getBlockPos().relative(result.getDirection());

				var placeContext = new BlockPlaceContext(level, player, hand, stack, result);
				var placeState = block.getStateForPlacement(placeContext);

				if(placeState == null)
					placeState = block.defaultBlockState();

				if(placeState.canSurvive(level, renderPos))
				{
					var position = mc.getEntityRenderDispatcher().camera.getPosition();

					pose.pushPose();
					pose.translate(-position.x, -position.y, -position.z);

					var buffers = mc.renderBuffers().bufferSource();

					var type = RenderType.lines();

					if(ghostBuffers == null)
						ghostBuffers = initBuffers(buffers);

					var consumer = ghostBuffers.getBuffer(type);

					renderBlock(mc, pose, ghostBuffers, consumer, level, renderPos, placeState, multiBlock);

					// ghostBuffers.endBatch();
					pose.popPose();
					return true;
				}
			}
		}

		return false;
	}

	private static void renderBlock(Minecraft mc, PoseStack pose, MultiBufferSource.BufferSource bufferSource, VertexConsumer vertexConsumer, ClientLevel level, BlockPos pos, BlockState blockState, IMultiBlock multiBlock)
	{
		var origin = multiBlock.getMultiBlockOriginPos(blockState, pos);

		for(var localPos : multiBlock.getMultiBlockLocalPositions())
		{
			var worldPos = multiBlock.getMultiBlockWorldSpaceFromLocalSpace(blockState, origin, localPos);
			renderBlockState(mc, pose, bufferSource, vertexConsumer, level, worldPos, blockState, multiBlock);
		}
	}

	private static void renderBlockState(Minecraft mc, PoseStack pose, MultiBufferSource.BufferSource bufferSource, VertexConsumer vertexConsumer, ClientLevel level, BlockPos pos, BlockState blockState, IMultiBlock multiBlock)
	{
		var x = pos.getX();
		var y = pos.getY();
		var z = pos.getZ();

		pose.translate(x, y, z);

		if(multiBlock.isMultiBlockOrigin(blockState))
		{
			var shape = blockState.getVisualShape(level, pos, CollisionContext.empty());
			var aabbs = shape.toAabbs();
			aabbs.forEach(aabb -> LevelRenderer.renderLineBox(pose, vertexConsumer, aabb, .65F, .65F, .65F, .65F));
		}

		if(blockState.getRenderShape() == RenderShape.MODEL)
			mc.getBlockRenderer().renderSingleBlock(blockState, pose, bufferSource, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

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