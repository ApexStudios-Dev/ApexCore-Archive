package xyz.apex.minecraft.apexcore.shared.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.UnaryOperator;

// src: https://github.com/mekanism/Mekanism/blob/1.19.x/src/main/java/mekanism/common/util/VoxelShapeUtils.java
public final class VoxelShapeHelper
{
    public static AABB rotate(AABB box, Direction side)
    {
        return switch(side) {
            case DOWN -> box;
            case UP -> new AABB(box.minX, -box.minY, -box.minZ, box.maxX, -box.maxY, -box.maxZ);
            case NORTH -> new AABB(box.minX, -box.minZ, box.minY, box.maxX, -box.maxZ, box.maxY);
            case SOUTH -> new AABB(-box.minX, -box.minZ, -box.minY, -box.maxX, -box.maxZ, -box.maxY);
            case WEST -> new AABB(box.minY, -box.minZ, -box.minX, box.maxY, -box.maxZ, -box.maxX);
            case EAST -> new AABB(-box.minY, -box.minZ, box.minX, -box.maxY, -box.maxZ, box.maxX);
        };
    }

    public static AABB rotate(AABB box, Rotation rotation)
    {
        return switch(rotation) {
            case NONE -> box;
            case CLOCKWISE_90 -> new AABB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
            case CLOCKWISE_180 -> new AABB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
            case COUNTERCLOCKWISE_90 -> new AABB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
        };
    }

    public static AABB rotateHorizontal(AABB box, Direction side)
    {
        return switch(side) {
            case NORTH -> rotate(box, Rotation.NONE);
            case SOUTH -> rotate(box, Rotation.CLOCKWISE_180);
            case WEST -> rotate(box, Rotation.COUNTERCLOCKWISE_90);
            case EAST -> rotate(box, Rotation.CLOCKWISE_90);
            default -> box;
        };
    }

    public static VoxelShape rotate(VoxelShape shape, Direction side)
    {
        return rotate(shape, box -> rotate(box, side));
    }

    public static VoxelShape rotate(VoxelShape shape, Rotation rotation)
    {
        return rotate(shape, box -> rotate(box, rotation));
    }

    public static VoxelShape rotateHorizontal(VoxelShape shape, Direction side)
    {
        return rotate(shape, box -> rotateHorizontal(box, side));
    }

    public static VoxelShape rotate(VoxelShape shape, UnaryOperator<AABB> rotateFunction)
    {
        var sourceBoxes = shape.toAabbs();
        var rotatedPieces = sourceBoxes
                .stream()
                .map(box -> box.move(-.5D, -.5D, -.5D))
                .map(rotateFunction)
                .map(box -> box.move(.5D, .5D, .5D))
                .map(Shapes::create)
        .toList();

        return combine(rotatedPieces);
    }

    public static VoxelShape combine(VoxelShape... shapes)
    {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    public static VoxelShape combine(Collection<VoxelShape> shapes)
    {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    public static VoxelShape exclude(VoxelShape... shapes)
    {
        return batchCombine(Shapes.block(), BooleanOp.ONLY_FIRST, true, shapes);
    }

    public static VoxelShape exclude(Collection<VoxelShape> shapes)
    {
        return batchCombine(Shapes.block(), BooleanOp.ONLY_FIRST, true, shapes);
    }

    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, VoxelShape... shapes)
    {
        var combinedShape = Arrays.stream(shapes).reduce(initial, (a, b) -> Shapes.joinUnoptimized(a, b, function));
        return simplify ? combinedShape.optimize() : combinedShape;
    }

    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, Collection<VoxelShape> shapes)
    {
        var combinedShape = shapes.stream().reduce(initial, (a, b) -> Shapes.joinUnoptimized(a, b, function));
        return simplify ? combinedShape.optimize() : combinedShape;
    }
}
