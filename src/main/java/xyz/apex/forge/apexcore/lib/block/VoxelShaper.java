package xyz.apex.forge.apexcore.lib.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableObject;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;

import xyz.apex.forge.apexcore.lib.util.VectorHelper;
import xyz.apex.java.utility.nullness.NonnullFunction;

import java.util.Arrays;
import java.util.Map;

public final class VoxelShaper
{
	public static final NonnullFunction<Direction, Vector3d> DEFAULT_ROTATION_VALUES = dir ->
			new Vector3d(
					dir == Direction.UP ? 0D : (Direction.Plane.VERTICAL.test(dir) ? 180D : 90D),
					-horizontalAngleFromDirection(dir),
					0D
			);

	public static final NonnullFunction<Direction, Vector3d> HORIZONTAL_ROTATION_VALUES = dir ->
			new Vector3d(
					0D,
					-horizontalAngleFromDirection(dir),
					0D
			);

	private final Map<Direction, VoxelShape> shapes = Maps.newEnumMap(Direction.class);

	public VoxelShape get(Direction facing)
	{
		return shapes.containsKey(facing) ? shapes.get(facing) : VoxelShapes.block();
	}

	public VoxelShape get(Direction.Axis axis)
	{
		return get(axisAsFace(axis));
	}

	public VoxelShaper withVerticalShapes(VoxelShape source)
	{
		return with(source, Direction.UP).with( rotatedCopy(source, new Vector3d(180D, 0D, 0D)), Direction.DOWN);
	}

	public VoxelShaper with(VoxelShape source, Direction facing)
	{
		shapes.put(facing, source);
		return this;
	}

	public static Direction axisAsFace(Direction.Axis axis)
	{
		return Direction.get(Direction.AxisDirection.POSITIVE, axis);
	}

	public static float horizontalAngleFromDirection(Direction direction)
	{
		return (float) ((Math.max(direction.get2DDataValue(), 0) & 3) * 90);
	}

	public static VoxelShaper forHorizontal(VoxelShape source, Direction facing)
	{
		return forDirectionsWithRotation(source, facing, Direction.Plane.HORIZONTAL, HORIZONTAL_ROTATION_VALUES);
	}

	public static VoxelShaper forHorizontalAxis(VoxelShape source, Direction.Axis along)
	{
		return forDirectionsWithRotation(source, axisAsFace(along), Arrays.asList(Direction.SOUTH, Direction.EAST), HORIZONTAL_ROTATION_VALUES);
	}

	public static VoxelShaper forDirectional(VoxelShape source, Direction facing)
	{
		return forDirectionsWithRotation(source, facing, Lists.newArrayList(Direction.values()), DEFAULT_ROTATION_VALUES);
	}

	public static VoxelShaper forAxis(VoxelShape source, Direction.Axis axis)
	{
		return forDirectionsWithRotation(source, axisAsFace(axis), Arrays.asList(Direction.SOUTH, Direction.EAST, Direction.UP), DEFAULT_ROTATION_VALUES);
	}

	public static VoxelShaper forDirectionsWithRotation(VoxelShape source, Direction facing, Iterable<Direction> directions, NonnullFunction<Direction, Vector3d> usingValues)
	{
		VoxelShaper shaper = new VoxelShaper();

		for(Direction direction : directions)
		{
			shaper.with(rotate(source, facing, direction, usingValues), direction);
		}

		return shaper;
	}

	public static VoxelShape rotate(VoxelShape source, Direction from, Direction to, NonnullFunction<Direction, Vector3d> usingValues)
	{
		if(from == to)
			return source;

		return rotatedCopy(source, usingValues.apply(from).reverse().add(usingValues.apply(to)));
	}

	public static VoxelShape rotatedCopy(VoxelShape source, Vector3d rotation)
	{
		if(rotation.equals(Vector3d.ZERO))
			return source;

		MutableObject<VoxelShape> result = new MutableObject<>(VoxelShapes.empty());
		Vector3d center = new Vector3d(8, 8, 8);

		source.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
			Vector3d v1 = new Vector3d(x1, y1, z1).scale(16D).subtract(center);
			Vector3d v2 = new Vector3d(x2, y2, z2).scale(16D).subtract(center);

			v1 = VectorHelper.rotate(v1, (float) rotation.x, Direction.Axis.X);
			v1 = VectorHelper.rotate(v1, (float) rotation.y, Direction.Axis.Y);
			v1 = VectorHelper.rotate(v1, (float) rotation.z, Direction.Axis.Z).add(center);

			v2 = VectorHelper.rotate(v2, (float) rotation.x, Direction.Axis.X);
			v2 = VectorHelper.rotate(v2, (float) rotation.y, Direction.Axis.Y);
			v2 = VectorHelper.rotate(v2, (float) rotation.z, Direction.Axis.Z).add(center);

			VoxelShape rotated = Block.box(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
			result.setValue(VoxelShapes.or(result.getValue(), rotated));
		});

		return result.getValue();
	}

	public static VoxelShape join(IBooleanFunction function, VoxelShape... shapes)
	{
		VoxelShape result = VoxelShapes.empty();

		for(VoxelShape shape : shapes)
		{
			result = VoxelShapes.join(result, shape, function);
		}

		return result.optimize();
	}

	public static VoxelShape or(VoxelShape... shapes)
	{
		return join(IBooleanFunction.OR, shapes);
	}
}
