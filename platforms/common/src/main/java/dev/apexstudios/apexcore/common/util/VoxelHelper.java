package dev.apexstudios.apexcore.common.util;

import net.covers1624.quack.collection.FastStream;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Map;
import java.util.function.Function;

public interface VoxelHelper
{
    Vector3dc VANILLA_ORIGIN_OFFSET = new Vector3d(.5D);
    Direction DEFAULT_INITIAL_FACING = Direction.UP;

    static Map<Direction, VoxelShape> rotateAll(@Nullable Direction initialFacing, VoxelShape initialShape, Vector3dc originOffset)
    {
        // initialFacing allows pre-rotating the initial shape
        // the rotation logic assumes shapes are facing UP
        // but some directional shapes assume the initial shape is NORTH facing
        // null is assumed to be UP
        var initialRotatedShape = rotate(initialFacing, initialShape, originOffset);
        return FastStream.of(Direction.values()).toImmutableMap(Function.identity(), direction -> rotate(direction, initialRotatedShape, originOffset));
    }

    static Map<Direction, VoxelShape> rotateAll(@Nullable Direction initialFacing, VoxelShape initialShape, double originOffsetX, double originOffsetY, double originOffsetZ)
    {
        return rotateAll(initialFacing, initialShape, new Vector3d(originOffsetX, originOffsetY, originOffsetZ));
    }

    static Map<Direction, VoxelShape> rotateAll(@Nullable Direction initialFacing, VoxelShape initialShape)
    {
        return rotateAll(initialFacing, initialShape, VANILLA_ORIGIN_OFFSET);
    }

    static Map<Direction, VoxelShape> rotateAll(VoxelShape initialShape, Vector3dc originOffset)
    {
        return rotateAll(null, initialShape, originOffset);
    }

    static Map<Direction, VoxelShape> rotateAll(VoxelShape initialShape, double originOffsetX, double originOffsetY, double originOffsetZ)
    {
        return rotateAll(null, initialShape, new Vector3d(originOffsetX, originOffsetY, originOffsetZ));
    }

    static Map<Direction, VoxelShape> rotateAll(VoxelShape initialShape)
    {
        return rotateAll(null, initialShape, VANILLA_ORIGIN_OFFSET);
    }

    static Map<Direction, VoxelShape> rotateVertical(@Nullable Direction initialFacing, VoxelShape initialShape, Vector3dc originOffset)
    {
        // initialFacing allows pre-rotating the initial shape
        // the rotation logic assumes shapes are facing UP
        // but some directional shapes assume the initial shape is NORTH facing
        // null is assumed to be UP
        var initialRotatedShape = rotate(initialFacing, initialShape, originOffset);
        return FastStream.of(Direction.Plane.VERTICAL).toImmutableMap(Function.identity(), direction -> rotate(direction, initialRotatedShape, originOffset));
    }

    static Map<Direction, VoxelShape> rotateVertical(@Nullable Direction initialFacing, VoxelShape initialShape, double originOffsetX, double originOffsetY, double originOffsetZ)
    {
        return rotateVertical(initialFacing, initialShape, new Vector3d(originOffsetX, originOffsetY, originOffsetZ));
    }

    static Map<Direction, VoxelShape> rotateVertical(@Nullable Direction initialFacing, VoxelShape initialShape)
    {
        return rotateVertical(initialFacing, initialShape, VANILLA_ORIGIN_OFFSET);
    }

    static Map<Direction, VoxelShape> rotateVertical(VoxelShape initialShape, Vector3dc originOffset)
    {
        return rotateVertical(null, initialShape, originOffset);
    }

    static Map<Direction, VoxelShape> rotateVertical(VoxelShape initialShape, double originOffsetX, double originOffsetY, double originOffsetZ)
    {
        return rotateVertical(null, initialShape, new Vector3d(originOffsetX, originOffsetY, originOffsetZ));
    }

    static Map<Direction, VoxelShape> rotateVertical(VoxelShape initialShape)
    {
        return rotateVertical(null, initialShape, VANILLA_ORIGIN_OFFSET);
    }

    static Map<Direction, VoxelShape> rotateHorizontal(@Nullable Direction initialFacing, VoxelShape initialShape, Vector3dc originOffset)
    {
        // initialFacing allows pre-rotating the initial shape
        // the rotation logic assumes shapes are facing UP
        // but some directional shapes assume the initial shape is NORTH facing
        // null is assumed to be UP
        var initialRotatedShape = rotate(initialFacing, initialShape, originOffset);
        return FastStream.of(Direction.Plane.HORIZONTAL).toImmutableMap(Function.identity(), direction -> rotate(direction, initialRotatedShape, originOffset));
    }

    static Map<Direction, VoxelShape> rotateHorizontal(@Nullable Direction initialFacing, VoxelShape initialShape, double originOffsetX, double originOffsetY, double originOffsetZ)
    {
        return rotateHorizontal(initialFacing, initialShape, new Vector3d(originOffsetX, originOffsetY, originOffsetZ));
    }

    static Map<Direction, VoxelShape> rotateHorizontal(@Nullable Direction initialFacing, VoxelShape initialShape)
    {
        return rotateHorizontal(initialFacing, initialShape, VANILLA_ORIGIN_OFFSET);
    }

    static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape initialShape, Vector3dc originOffset)
    {
        return rotateHorizontal(null, initialShape, originOffset);
    }

    static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape initialShape, double originOffsetX, double originOffsetY, double originOffsetZ)
    {
        return rotateHorizontal(null, initialShape, new Vector3d(originOffsetX, originOffsetY, originOffsetZ));
    }

    static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape initialShape)
    {
        return rotateHorizontal(null, initialShape, VANILLA_ORIGIN_OFFSET);
    }

    static VoxelShape rotate(@Nullable Direction direction, VoxelShape shape, Vector3dc originOffset)
    {
        // logic assumes shape is facing UP
        // null | UP directions return the same shape as no rotation is needed
        if(direction == null || direction == DEFAULT_INITIAL_FACING)
            return shape;

        var result = new VoxelShape[] { Shapes.empty() };
        var rotation = direction.getRotation();

        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            var start = rotation.transform(new Vector3d(minX, minY, minZ).sub(originOffset)).add(originOffset);
            var end = rotation.transform(new Vector3d(maxX, maxY, maxZ).sub(originOffset)).add(originOffset);
            result[0] = Shapes.or(result[0], box(start, end));
        });

        return result[0];
    }

    static VoxelShape rotate(@Nullable Direction direction, VoxelShape shape, double originOffsetX, double originOffsetY, double originOffsetZ)
    {
        return rotate(direction, shape, new Vector3d(originOffsetX, originOffsetY, originOffsetZ));
    }

    static VoxelShape rotate(@Nullable Direction direction, VoxelShape shape)
    {
        return rotate(direction, shape, VANILLA_ORIGIN_OFFSET);
    }

    static VoxelShape box(double startX, double startY, double startZ, double endX, double endY, double endZ)
    {
        return Shapes.box(
                Math.min(startX, endX),
                Math.min(startY, endY),
                Math.min(startZ, endZ),
                Math.max(startX, endX),
                Math.max(startY, endY),
                Math.max(startZ, endZ)
        );
    }

    static VoxelShape box(Vector3dc start, Vector3dc end)
    {
        return box(start.x(), start.y(), start.z(), end.x(), end.y(), end.z());
    }
}
