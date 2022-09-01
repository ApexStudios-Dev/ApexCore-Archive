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
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.lib.block.BaseBlock;
import xyz.apex.forge.apexcore.lib.block.IMultiBlock;
import xyz.apex.forge.apexcore.lib.block.MultiBlockPattern;
import xyz.apex.forge.apexcore.lib.event.client.BlockVisualizerEvent;
import xyz.apex.forge.apexcore.lib.util.RegistryHelper;
import xyz.apex.forge.commonality.Mods;
import xyz.apex.forge.commonality.SideOnly;

import java.util.IdentityHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Mods.APEX_CORE, value = Dist.CLIENT)
@SideOnly(SideOnly.Side.CLIENT)
public final class BlockVisualizer
{
	@Nullable private static MultiBufferSource.BufferSource ghostBuffers = null;

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event)
	{
		if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES)
		{
			var mc = Minecraft.getInstance();
			var pose = event.getPoseStack();

			for(var hand : InteractionHand.values())
			{
				if(tryRenderBlock(mc, pose, hand))
					break;
			}
		}
	}

	private static boolean tryRenderBlock(Minecraft mc, PoseStack pose, InteractionHand hand)
	{
		if(mc.level == null || mc.player == null)
			return false;

		var stack = mc.player.getItemInHand(hand);
		var block = Block.byItem(stack.getItem());

		if(block == Blocks.AIR)
			return false;
		if(!RegistryHelper.hasTag(ForgeRegistries.BLOCKS, ACRegistry.TAG_VISUALIZER, block))
			return false;
		if(!(mc.hitResult instanceof BlockHitResult result))
			return false;

		var pos = result.getBlockPos();

		if(mc.level.isEmptyBlock(pos))
			return false;

		var ctx = getRenderBlockState(mc.level, mc.player, hand, stack, block, pos, result);

		if(ctx.blockState.getRenderShape() != RenderShape.MODEL)
			return false;

		// pre
		var position = mc.getEntityRenderDispatcher().camera.getPosition();
		var buffers = mc.renderBuffers().bufferSource();
		var type = RenderType.lines();

		if(ghostBuffers == null)
			ghostBuffers = initBuffers(buffers);

		pose.pushPose();
		pose.translate(-position.x, -position.y, -position.z);
		MinecraftForge.EVENT_BUS.post(new BlockVisualizerEvent.Render.Pre(ctx));

		// render
		renderBlock(mc, ctx, pose, ghostBuffers);

		// post
		MinecraftForge.EVENT_BUS.post(new BlockVisualizerEvent.Render.Post(ctx));
		buffers.endBatch(type);

		ghostBuffers.endBatch();
		pose.popPose();
		return true;
	}

	private static Context getRenderBlockState(ClientLevel level, LocalPlayer player, InteractionHand hand, ItemStack stack, Block block, BlockPos pos, BlockHitResult result)
	{
		var placeContext = new BlockPlaceContext(new UseOnContext(level, player, hand, stack, result));
		var placeState = block.getStateForPlacement(placeContext);
		var defaultState = false;

		if(placeState == null)
		{
			defaultState = true;
			placeState = block.defaultBlockState();

			if(placeState.getBlock() instanceof BaseBlock base && BaseBlock.supportsFacing(placeState))
			{
				var facing = base.getFourWayFacing(placeContext);

				if(facing != null)
					placeState = BaseBlock.setFacing(placeState, facing);
			}
		}

		var direction = result.getDirection();
		var renderPos = pos.relative(direction);
		var ctx = new BlockVisualizer.Context(placeState, level, renderPos, player, hand, stack, direction, 0);

		if(defaultState)
			ctx = modifyBlockState(ctx, BlockVisualizerEvent.ModifyContext.Reason.DEFAULT_BLOCKSTATE);

		ctx = modifyBlockState(ctx, BlockVisualizerEvent.ModifyContext.Reason.EXISTING_BLOCKSTATE);
		placeState = BaseBlock.setWaterLogged(ctx.blockState, false);
		return ctx.with(placeState);
	}

	private static Context modifyBlockState(Context ctx, BlockVisualizerEvent.ModifyContext.Reason reason)
	{
		var event = new BlockVisualizerEvent.ModifyContext(ctx, reason);
		MinecraftForge.EVENT_BUS.post(event);
		ctx = event.getContext();

		if(reason == BlockVisualizerEvent.ModifyContext.Reason.DEFAULT_BLOCKSTATE && ctx.blockState.getBlock() instanceof IMultiBlock multiBlock)
			ctx = ctx.with(multiBlock.setMultiBlockIndex(ctx.blockState, MultiBlockPattern.INDEX_ORIGIN));

		return ctx;
	}

	private static void renderBlock(Minecraft mc, BlockVisualizer.Context ctx, PoseStack pose, MultiBufferSource.BufferSource bufferSource)
	{
		if(ctx.blockState.getBlock() instanceof IMultiBlock multiBlock)
		{
			var origin = multiBlock.getMultiBlockOriginPos(ctx.blockState, ctx.pos);
			var localPositions = multiBlock.getMultiBlockLocalPositions();

			var isValid = true;

			for(var i = 0; i < localPositions.size(); i++)
			{
				var localPos = localPositions.get(i);
				var worldPos = multiBlock.getMultiBlockWorldSpaceFromLocalSpace(ctx.blockState, origin, localPos);
				var renderState = multiBlock.setMultiBlockIndex(ctx.blockState, i);
				var worldState = ctx.level.getBlockState(worldPos);

				if(!renderState.canSurvive(ctx.level, worldPos))
				{
					isValid = false;
					break;
				}

				if(!multiBlock.getMultiBlockPattern().passesPlacementTests(multiBlock, ctx.level, worldPos, renderState, worldState))
				{
					isValid = false;
					break;
				}
			}

			var overlay = isValid ? OverlayTexture.NO_OVERLAY : OverlayTexture.pack(OverlayTexture.NO_WHITE_U, OverlayTexture.RED_OVERLAY_V);

			for(var i = 0; i < localPositions.size(); i++)
			{
				var localPos = localPositions.get(i);
				var worldPos = multiBlock.getMultiBlockWorldSpaceFromLocalSpace(ctx.blockState, origin, localPos);
				var renderState = multiBlock.setMultiBlockIndex(ctx.blockState, i);
				var newRenderCtx = ctx.with(worldPos).with(renderState);
				renderBlockState(mc, newRenderCtx, pose, bufferSource, overlay);
			}
		}
		else
			renderBlockState(mc, ctx, pose, bufferSource, OverlayTexture.NO_OVERLAY);
	}

	private static void renderBlockState(Minecraft mc, BlockVisualizer.Context ctx, PoseStack pose, MultiBufferSource.BufferSource bufferSource, int overlay)
	{
		var x = ctx.pos.getX();
		var y = ctx.pos.getY();
		var z = ctx.pos.getZ();

		pose.translate(x, y, z);
		pose.pushPose();

		var blockRenderer = mc.getBlockRenderer();

		if(ctx.blockState.getRenderShape() == RenderShape.MODEL)
		{
			var model = blockRenderer.getBlockModel(ctx.blockState);
			var blockColor = mc.getBlockColors().getColor(ctx.blockState, null, null, ctx.tintIndex);
			var r = FastColor.ARGB32.red(blockColor) / 255F;
			var g = FastColor.ARGB32.green(blockColor) / 255F;
			var b = FastColor.ARGB32.blue(blockColor) / 255F;

			for(var renderType : model.getRenderTypes(ctx.blockState, RandomSource.create(42), ModelData.EMPTY))
			{
				var last = pose.last();
				var buffer = bufferSource.getBuffer(RenderTypeHelper.getEntityRenderType(renderType, false));

				blockRenderer.getModelRenderer().renderModel(
						last, buffer, ctx.blockState, model,
						r, g, b,
						LightTexture.FULL_BLOCK, overlay,
						ModelData.EMPTY, renderType
				);
			}
		}
		else
			blockRenderer.renderSingleBlock(ctx.blockState, pose, bufferSource, LightTexture.FULL_BLOCK, overlay, ModelData.EMPTY, null);

		pose.popPose();
		pose.translate(-x, -y, -z);
	}

	public record Context(BlockState blockState, ClientLevel level, BlockPos pos, LocalPlayer player, InteractionHand hand, ItemStack stack, Direction face, int tintIndex)
	{
		public Context with(BlockState blockState)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}

		public Context with(ClientLevel level)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}

		public Context with(BlockPos pos)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}

		public Context with(LocalPlayer player)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}

		public Context with(InteractionHand hand)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}

		public Context with(ItemStack stack)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}

		public Context with(Direction face)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}

		public Context with(int tintIndex)
		{
			return new Context(blockState, level, pos, player, hand, stack, face, tintIndex);
		}
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