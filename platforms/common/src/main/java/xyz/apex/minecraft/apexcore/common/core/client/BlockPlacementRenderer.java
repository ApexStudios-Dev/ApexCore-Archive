package xyz.apex.minecraft.apexcore.common.core.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.core.ApexTags;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.types.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.event.types.LevelRendererEvents;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockComponent;

import java.util.OptionalDouble;

import static net.minecraft.client.renderer.RenderStateShard.*;
import static org.lwjgl.opengl.GL11.GL_NOTEQUAL;

@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.Internal
public final class BlockPlacementRenderer
{
    public static final BlockPlacementRenderer INSTANCE = new BlockPlacementRenderer();

    private final RenderType entityTranslucentNoDepth;
    private final RenderType linesNoDepth;

    private int customOverlayTint = -1;
    private boolean useCustomTint = false;

    private BlockPlacementRenderer()
    {
        var depthTestNotEqual = new DepthTestStateShard("%s:not_equal".formatted(ApexCore.ID), GL_NOTEQUAL);

        entityTranslucentNoDepth = RenderType.create(
                "%s:entity_translucent_no_depth".formatted(ApexCore.ID),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256, false, true,
                RenderType.CompositeState.builder()
                        .setTextureState(new TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(new CustomOverlay())
                        .setCullState(CULL)
                        .setWriteMaskState(COLOR_WRITE)
                        .setDepthTestState(depthTestNotEqual)
                        .setLayeringState(POLYGON_OFFSET_LAYERING)
                .createCompositeState(false)
        );

        linesNoDepth = RenderType.create(
                "%s:lines_no_depth".formatted(ApexCore.ID),
                DefaultVertexFormat.POSITION_COLOR_NORMAL,
                VertexFormat.Mode.LINES,
                256,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_LINES_SHADER)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLineState(new LineStateShard(OptionalDouble.empty()))
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                        .setOutputState(ITEM_ENTITY_TARGET)
                        .setCullState(NO_CULL)
                        .setWriteMaskState(COLOR_WRITE)
                        .setDepthTestState(depthTestNotEqual)
                        .setLayeringState(POLYGON_OFFSET_LAYERING)
                .createCompositeState(false)
        );
    }

    private void renderBlockPlacement(LevelRenderer renderer, PoseStack pose, MultiBufferSource buffer, float partialTick, Camera camera)
    {
        var client = Minecraft.getInstance();

        // only if player is looking at a block
        if(!(client.hitResult instanceof BlockHitResult result))
            return;

        // ensure player and level exists
        if(client.player == null || client.level == null)
            return;

        // check if should render here
        if(!shouldRenderAt(client.level, result))
            return;

        // lookup correct item for render
        // players main hand first then offhand
        var hand = InteractionHand.MAIN_HAND;
        var stack = client.player.getItemInHand(hand);

        if(stack.isEmpty())
        {
            hand = InteractionHand.OFF_HAND;
            stack = client.player.getItemInHand(hand);
        }

        // ensure item has an associated block
        if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem item))
            return;

        final var stackFinal = stack;
        var block = item.getBlock();

        // only render for blocks marked with our tag
        if(!block.builtInRegistryHolder().is(ApexTags.Blocks.PLACEMENT_VISUALIZER))
            return;

        var placeContext = new BlockPlaceContext(client.player, hand, stackFinal, result);

        // lookup correct position to render at
        var renderPos = getRenderPos(block, placeContext);

        // lookup correct blockstate for render
        var blockState = getRenderBlockState(block, placeContext);

        // only if block state is set for json model rendering
        // and not invisible render type
        if(blockState.getRenderShape() != RenderShape.MODEL)
            return;

        pose.pushPose();
        var camPosition = camera.getPosition();
        pose.translate(-camPosition.x, -camPosition.y, -camPosition.z);

        var canBePlaced = canBePlaced(placeContext, renderPos, blockState);

        // render for every block in the multi block
        // TODO: maybe extract this out into some form of registry, to allow other mods to register their own placement visualizers and override the default rendering
        //  also move multiblocks to use said registry system rather than hard wired here
        BlockComponentHolder.runAsComponent(blockState, BlockComponentTypes.MULTI_BLOCK, component -> {
            var rendered = false;
            var multiBlockType = component.getMultiBlockType();

            for(var i = 0; i < multiBlockType.size(); i++)
            {
                var newBlockState = MultiBlockComponent.setIndex(multiBlockType, blockState, i);

                if(newBlockState.getRenderShape() != RenderShape.MODEL)
                    continue;

                var worldPosition = MultiBlockComponent.worldPosition(multiBlockType, renderPos, newBlockState);
                renderBlock(client, pose, buffer, stackFinal, newBlockState, worldPosition, canBePlaced);
            }

            // render single block, if multi block did not render
            if(!rendered)
                renderBlock(client, pose, buffer, stackFinal, blockState, renderPos, canBePlaced);

        }, () -> renderBlock(client, pose, buffer, stackFinal, blockState, renderPos, canBePlaced));

        pose.popPose();
    }

    private boolean canBePlaced(BlockPlaceContext context, BlockPos pos, BlockState blockState)
    {
        return BlockComponentHolder.mapAsComponent(blockState, MultiBlockComponent.COMPONENT_TYPE, component -> {
            var multiBlockType = component.getMultiBlockType();

            for(var i = 0; i < multiBlockType.size(); i++)
            {
                var newBlockState = MultiBlockComponent.setIndex(multiBlockType, blockState, i);
                var worldPosition = MultiBlockComponent.worldPosition(multiBlockType, pos, newBlockState);

                if(!MultiBlockComponent.canPlaceAt(context, worldPosition, newBlockState))
                    return false;
            }

            return true;
        }).orElseGet(() -> MultiBlockComponent.canPlaceAt(context, pos, blockState));
    }

    @SuppressWarnings("DataFlowIssue")
    private void renderBlock(Minecraft client, PoseStack pose, MultiBufferSource buffer, ItemStack stack, BlockState blockState, BlockPos pos, boolean canBePlaced)
    {
        // translate to correct render position
        var blockOffset = blockState.getOffset(client.level, pos);
        pose.pushPose();
        pose.translate(
                pos.getX() + blockOffset.x,
                pos.getY() + blockOffset.y,
                pos.getZ() + blockOffset.z
        );

        renderBlockState(client, pose, buffer, stack, blockState, pos, canBePlaced);
        renderBlockHighlight(client, pose, buffer, blockState, pos, canBePlaced);

        pose.popPose();
    }

    @SuppressWarnings("DataFlowIssue")
    private void renderBlockState(Minecraft client, PoseStack pose, MultiBufferSource buffer, ItemStack stack, BlockState blockState, BlockPos pos, boolean canBePlaced)
    {
        var consumer = new GhostVertexConsumer(buffer.getBuffer(entityTranslucentNoDepth));
        var blockColor = getRenderBlockColor(client, stack, blockState, pos);
        var light = LevelRenderer.getLightColor(client.level, blockState, pos);
        var blockRenderer = client.getBlockRenderer();
        var model = blockRenderer.getBlockModel(blockState);

        var r = FastColor.ARGB32.red(blockColor) / 255F;
        var g = FastColor.ARGB32.green(blockColor) / 255F;
        var b = FastColor.ARGB32.blue(blockColor) / 255F;

        // tint ghost effect correctly
        useCustomTint = true;
        customOverlayTint = canBePlaced ? 0xFFFFFF : 0xEB3223;

        blockRenderer.getModelRenderer().renderModel(pose.last(), consumer, blockState, model, r, g, b, light, OverlayTexture.NO_OVERLAY);
    }

    @SuppressWarnings("DataFlowIssue")
    private void renderBlockHighlight(Minecraft client, PoseStack pose, MultiBufferSource buffer, BlockState blockState, BlockPos pos, boolean canBePlaced)
    {
        var consumer = buffer.getBuffer(linesNoDepth);
        var collisionContext = CollisionContext.of(client.player);
        var shape = blockState.getShape(client.level, pos, collisionContext);

        var color = canBePlaced ? 0x0 : 0xEB3223;
        var r = FastColor.ARGB32.red(color) / 255F;
        var g = FastColor.ARGB32.green(color) / 255F;
        var b = FastColor.ARGB32.blue(color) / 255F;
        var a = GhostVertexConsumer.ghostAlpha() / 255F;

        LevelRenderer.renderShape(
                pose,
                consumer,
                shape,
                0D, 0D, 0D,
                r, g, b, a
        );
    }

    private int getRenderBlockColor(Minecraft client, ItemStack stack, BlockState blockState, BlockPos pos)
    {
        var blockColor = client.getBlockColors().getColor(blockState, client.level, pos, 0);

        if(blockColor == -1)
            blockColor = client.itemColors.getColor(stack, 0);

        return blockColor;
    }

    private BlockState getRenderBlockState(Block block, BlockPlaceContext context)
    {
        var blockState = block.getStateForPlacement(context);

        if(blockState == null)
            blockState = block.defaultBlockState();

        final var blockStateFinal = blockState;
        return BlockComponentHolder.mapAsComponent(block, BlockComponentTypes.MULTI_BLOCK, component -> MultiBlockComponent.setIndex(component.getMultiBlockType(), blockStateFinal, 0)).orElse(blockStateFinal);
    }

    private BlockPos getRenderPos(Block block, BlockPlaceContext context)
    {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var face = context.getClickedFace();

        var blockState = level.getBlockState(pos);

        if(blockState.is(block) && blockState.canBeReplaced(context))
            return pos;

        return MultiBlockComponent.canPlaceAt(context, null, null) ? pos : pos.relative(face);
    }

    private boolean shouldRenderAt(Level level, BlockHitResult result)
    {
        var pos = result.getBlockPos();

        if(level.isOutsideBuildHeight(pos))
            return false;
        if(!level.getWorldBorder().isWithinBounds(pos))
            return false;
        return !level.isEmptyBlock(pos);
    }

    public void register()
    {
        LevelRendererEvents.BLOCK_HIGHLIGHT.addListener((renderer, pose, buffer, partialTick, camera) -> {
            renderBlockPlacement(renderer, pose, buffer, partialTick, camera);
            return false;
        });
    }

    private final class CustomOverlay extends OverlayStateShard
    {
        private boolean resetShaderColor = false;

        private CustomOverlay()
        {
            super(true);
        }

        @Override
        public void setupRenderState()
        {
            super.setupRenderState();

            if(useCustomTint)
            {
                var r = FastColor.ARGB32.red(customOverlayTint) / 255F;
                var g = FastColor.ARGB32.green(customOverlayTint) / 255F;
                var b = FastColor.ARGB32.blue(customOverlayTint) / 255F;
                RenderSystem.setShaderColor(r, g, b, 1F);
                customOverlayTint = -1;
                useCustomTint = false;
                resetShaderColor = true;
            }
        }

        @Override
        public void clearRenderState()
        {
            super.clearRenderState();

            if(resetShaderColor)
            {
                RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
                resetShaderColor = false;
            }
        }
    }
}
