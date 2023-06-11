package xyz.apex.minecraft.apexcore.common.lib.event.types;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

@ApiStatus.NonExtendable
@SideOnly(PhysicalSide.CLIENT)
public interface LevelRendererEvents
{
    EventType<AfterEntities> AFTER_ENTITIES = EventType.create(listeners -> (renderer, pose, projection, partialTick, camera, frustum) -> listeners.forEach(listener -> listener.handle(renderer, pose, projection, partialTick, camera, frustum)));
    EventType<AfterTranslucent> AFTER_TRANSLUCENT = EventType.create(listeners -> (renderer, pose, projection, partialTick, camera, frustum) -> listeners.forEach(listener -> listener.handle(renderer, pose, projection, partialTick, camera, frustum)));
    EventType<BlockHighlight> BLOCK_HIGHLIGHT = EventType.create(listeners -> (renderer, pose, buffer, partialTick, camera) -> listeners.stream().anyMatch(listener -> listener.handle(renderer, pose, buffer, partialTick, camera)));

    @ApiStatus.Internal
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface BlockHighlight extends Event
    {
        boolean handle(LevelRenderer renderer, PoseStack pose, MultiBufferSource buffer, float partialTick, Camera camera);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface AfterEntities extends Event
    {
        void handle(LevelRenderer renderer, PoseStack pose, Matrix4f projection, float partialTick, Camera camera, Frustum frustum);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface AfterTranslucent extends Event
    {
        void handle(LevelRenderer renderer, PoseStack pose, Matrix4f projection, float partialTick, Camera camera, Frustum frustum);
    }
}
